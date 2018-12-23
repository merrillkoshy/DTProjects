package dot10tech.com.dot10projects.Employee

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dot10tech.com.dot10projects.FirebaseData.ProjectsDataClass

import dot10tech.com.dot10projects.FirebaseData.TeamAssignmentDataClass
import dot10tech.com.dot10projects.MainActivity
import dot10tech.com.dot10projects.Networking.VolleySingleton
import dot10tech.com.dot10projects.R
import dot10tech.com.dot10projects.UI.EndPoints


import kotlinx.android.synthetic.main.activity_editteam.*
import org.json.JSONException
import org.json.JSONObject

class EditTeamActivity:AppCompatActivity(){
    var staffNameArray= ArrayList<String>()
    var staffAssignment= ArrayList<String>()
    var staffAffiliation= ArrayList<String>()
    var affiliation= String()
    var assignment= String()
    var staffName= String()
    var selectedPos:Int=99

    lateinit var updatableChild:HashMap<String,Any>
    lateinit var teamassignmentList:MutableList<TeamAssignmentDataClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        updatableChild= hashMapOf()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editteam)
        staffNameArray=intent.getStringArrayListExtra("staffName")
        staffAssignment=intent.getStringArrayListExtra("staffAssignment")
        staffAffiliation=intent.getStringArrayListExtra("staffAffiliation")

        loadtray()
        spinner()
        teameditProjectTab.setOnClickListener {
            onBackPressed()
        }
        edittexts()
        teamupdatebtn.setOnClickListener { addActivity() }
    }

    fun loadtray(){
        teamassignmentList= mutableListOf()

        val database= FirebaseDatabase.getInstance().getReference()
        database.child("teamassignment"). addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children){

                    val project=dataSnapshot.child(0.toString()).getValue(TeamAssignmentDataClass::class.java)
                    teamassignmentList.add(project!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("test", "Failed to read value.", error.toException())
            }
        })

    }

    fun spinner(){



        staffselectionspinner.adapter=ArrayAdapter(this,R.layout.spinner_item, staffNameArray)

        //item selected listener for spinner
        staffselectionspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(this@EditTeamActivity, staffNameArray[p2], LENGTH_LONG).show()
                selectedPos=p2
                assignment=staffAssignment[p2]
                affiliation=staffAffiliation[p2]


                staff_assignment.hint=assignment
                staff_affiliation.hint=affiliation
                staffName=staffNameArray[p2]
                staff_name.hint=staffName
            }

        }



    }

    fun edittexts(){


        staff_name.setTextColor(resources.getColor(R.color.white))
        staff_name.setHintTextColor(resources.getColor(R.color.unchanged))

        staff_assignment.setTextColor(resources.getColor(R.color.white))
        staff_assignment.setHintTextColor(resources.getColor(R.color.unchanged))

        staff_affiliation.setTextColor(resources.getColor(R.color.white))
        staff_affiliation.setHintTextColor(resources.getColor(R.color.unchanged))
    }

    fun addActivity(){

        val staffNameET=staff_name.text.toString().trim()
        val staffAssignmentET=staff_assignment.text.toString().trim()
        val staffAffiliationET=staff_affiliation.text.toString().trim()


        var getStaffname = staffNameET
        if (getStaffname=="")
        {getStaffname=staffName}

        var getStaffassignment=staffAssignmentET
        if (getStaffassignment=="")
        {getStaffassignment=assignment}

        var getAffiliation=staffAffiliationET
        if (getAffiliation=="")
        {getAffiliation=affiliation}

        val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.UPDATETEAM_URL, Response.Listener<String>{
                response ->
            try {
                val obj = JSONObject(response)
                Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()

                val database= FirebaseDatabase.getInstance().getReference("teamassignment")

                /*fetch all updates*/

                teamassignmentList[0].staffName[selectedPos]=getStaffname
                teamassignmentList[0].staffAssignment[selectedPos]=getStaffassignment
                teamassignmentList[0].affiliation[selectedPos]=getAffiliation

                /*package with change*/

                val toUpdateStaffname=teamassignmentList[0].staffName
                val toUpdateStaffAssignment=teamassignmentList[0].staffAssignment
                val toUpdateAffiliation=teamassignmentList[0].affiliation

                /*put those in table*/

                updatableChild.put("staffName", toUpdateStaffname)
                updatableChild.put("staffAssignment", toUpdateStaffAssignment)
                updatableChild.put("affiliation", toUpdateAffiliation)

                /*update in database*/

                database.updateChildren(updatableChild)

                val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
            }catch (e: JSONException){
                e.printStackTrace()
            }

        }, object : Response.ErrorListener{
            override fun onErrorResponse(volleyError: VolleyError) {
                Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
            }
        }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                Log.d("allParams","getstaffname:"+getStaffname+"staffname:"+
                        staffName+"getstaffassign:"+getStaffassignment+"getProject:"+intent.getStringExtra("projectName")+
                "getAffli:"+getAffiliation)
                params.put("name", getStaffname)
                params.put("oldname", staffName)

                params.put("assignment", getStaffassignment)
                params.put("project",intent.getStringExtra("projectName"))

                params.put("Affiliation", getAffiliation)


                return params
            }
        }


        VolleySingleton.instance?.addToRequestQueue(stringRequest)


    }
}