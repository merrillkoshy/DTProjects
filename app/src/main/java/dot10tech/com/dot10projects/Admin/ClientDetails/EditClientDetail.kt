package dot10tech.com.dot10projects.Admin.ClientDetails

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.MainActivity
import dot10tech.com.dot10projects.Networking.VolleySingleton
import dot10tech.com.dot10projects.R
import dot10tech.com.dot10projects.UI.EndPoints
import kotlinx.android.synthetic.main.activity_editclientdetail.*
import org.json.JSONException
import org.json.JSONObject

import android.view.ViewGroup
import android.view.ViewParent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dot10tech.com.dot10projects.DataLayer.ProjectDetails
import dot10tech.com.dot10projects.DataLayer.projectOfClientName
import dot10tech.com.dot10projects.Employee.EditTeamActivity
import dot10tech.com.dot10projects.FirebaseData.ProjectsDataClass
import kotlinx.android.synthetic.main.activity_editteam.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import java.io.IOException
import java.util.ArrayList


class EditClientDetail:AppCompatActivity(){

    private var targetEmp = String()
    val staffName = ArrayList<String>()
    val staffAssignment = ArrayList<String>()
    val workingProject = ArrayList<String>()
    val affiliation = ArrayList<String>()
    lateinit var updatableChild:HashMap<String,Any>
    lateinit var projectdetailsList:MutableList<ProjectsDataClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updatableChild= hashMapOf()
        setContentView(R.layout.activity_editclientdetail)
        loadTray()

        val startDate=intent.getStringExtra("sd")
        val deadline=intent.getStringExtra("dl")
        val overallprogress=intent.getStringExtra("op")
        val latestactivity=intent.getStringExtra("la")
        val taskdeadline=intent.getStringExtra("td")
        val taskstatus=intent.getStringExtra("ts")

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)



        clientname_et.setTextColor(resources.getColor(R.color.white))
        clientname_et.hint=intent.getStringExtra("cN")
        clientname_et.setHintTextColor(resources.getColor(R.color.unchanged))

        startdate_et.hint=startDate.replace("\\","")
        startdate_et.setHintTextColor(resources.getColor(R.color.unchanged))

        deadline_et.hint=deadline.replace("\\","")
        deadline_et.setHintTextColor(resources.getColor(R.color.unchanged))

        op_et.hint=overallprogress.toString()
        op_et.setHintTextColor(resources.getColor(R.color.unchanged))

        la_et.hint=latestactivity
        la_et.setHintTextColor(resources.getColor(R.color.unchanged))

        td_et.hint=taskdeadline.replace("\\","")
        td_et.setHintTextColor(resources.getColor(R.color.unchanged))

        ts_et.setTextColor(resources.getColor(R.color.white))
        ts_et.hint=taskstatus.replace("\\t","")
        ts_et.setHintTextColor(resources.getColor(R.color.unchanged))


        fetchJsonEmp()
        Picasso.
            get().
            load("https:dot10tech.com/mobileApp/assets/appicon.png").
            fit().
            placeholder(R.drawable.progress_animation).
            into(mast)
        optiontabs()
        updatebtn.setOnClickListener { addActivity() }
    }

    fun loadTray(){
        projectdetailsList= mutableListOf()



        val database= FirebaseDatabase.getInstance().getReference()
        database.child("projectdetails"). addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children){

                    val project=dataSnapshot.child(0.toString()).getValue(ProjectsDataClass::class.java)
                    projectdetailsList.add(project!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("test", "Failed to read value.", error.toException())
            }
        })
    }

    fun optiontabs(){


        editTeamTab.setOnClickListener {
            val staffName=intent.getStringArrayListExtra("staffName")
            val staffAssignment=intent.getStringArrayListExtra("staffAssignment")
            val staffAffiliation=intent.getStringArrayListExtra("staffAffiliation")
            val projectName=intent.getStringExtra("cN")
            Log.d("projectName",projectName)

            val intent:Intent= Intent(this,EditTeamActivity::class.java)
            intent.putStringArrayListExtra("staffName",staffName)
            intent.putStringArrayListExtra("staffAssignment",staffAssignment)
            intent.putStringArrayListExtra("staffAffiliation",staffAffiliation)
            intent.putExtra("projectName",projectName)
            startActivity(intent)

        }


    }

    fun fetchJsonEmp(){
        val url = "https://dot10tech.com/mobileapp/scripts/teamAssignmentView.php"

        val client = OkHttpClient()
        val request = okhttp3.Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body()?.string()

                //Slicing the response
                targetEmp = body.toString()

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
        return true
    }

       private fun addActivity() {

           val startDate=intent.getStringExtra("sd")
           val deadline=intent.getStringExtra("dl")
           val overallprogress=intent.getStringExtra("op")
           val latestactivity=intent.getStringExtra("la")
           val taskdeadline=intent.getStringExtra("td")
           val taskstatus=intent.getStringExtra("ts")


           var getClientName = clientname_et?.text.toString()
           if (getClientName=="")
           {getClientName=intent.getStringExtra("cN")}

           var getStartDate=startdate_et.text.toString()
           if (getStartDate=="")
           {getStartDate=startDate}

           var getDeadline=deadline_et.text.toString()
           if (getDeadline=="")
           {getDeadline=deadline}

           var getoverallProgress=op_et.text.toString()
           if (getoverallProgress=="")
           {getoverallProgress=overallprogress}

           var getlatestActivity=la_et.text.toString()
           if (getlatestActivity=="")
           {getlatestActivity=latestactivity}

           var gettaskDeadline=td_et.text.toString()
           if (gettaskDeadline=="")
           {gettaskDeadline=taskdeadline}

           var gettaskStatus=ts_et.text.toString()
           if (gettaskStatus=="")
           {gettaskStatus=taskstatus}

           val selectedclientIndex=intent.getIntExtra("flag",99)

       if(getClientName==intent.getStringExtra("cN")
       &&   getStartDate==startDate
       &&   getDeadline==deadline
       &&   getoverallProgress==overallprogress
       &&   getlatestActivity==latestactivity
       &&   gettaskDeadline==taskdeadline
       &&   gettaskStatus==taskstatus)
       {
           Toast.makeText(this,"No changes",Toast.LENGTH_SHORT).show()
           finish()
       }
           else{
           val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.UPDATE_URL, Response.Listener<String>{
                   response ->
               try {
                   val obj = JSONObject(response)
                   Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()

                   val database= FirebaseDatabase.getInstance().getReference("projectdetails").child("0")

                   /*fetch all updates*/

                   projectdetailsList[0].clientName[selectedclientIndex]=getClientName
                   projectdetailsList[0].startDate[selectedclientIndex]=getStartDate
                   projectdetailsList[0].deadLine[selectedclientIndex]=getDeadline
                   projectdetailsList[0].latestActivity[selectedclientIndex]=getlatestActivity
                   projectdetailsList[0].taskDeadline[selectedclientIndex]=gettaskDeadline
                   projectdetailsList[0].taskStatus[selectedclientIndex]=gettaskStatus
                   projectdetailsList[0].overallProgress[selectedclientIndex]=getoverallProgress


                   /*package with change*/

                   val toUpdateClientname=projectdetailsList[0].clientName
                   val toUpdateStartDate=projectdetailsList[0].startDate
                   val toUpdateDeadline=projectdetailsList[0].deadLine
                   val toUpdateLatestActivity=projectdetailsList[0].latestActivity
                   val toUpdateTaskDeadline=projectdetailsList[0].taskDeadline
                   val toUpdateTaskStatus=projectdetailsList[0].taskStatus
                   val toUpdateOverallProgress=projectdetailsList[0].overallProgress


                   /*put those in table*/

                   updatableChild.put("clientName", toUpdateClientname)
                   updatableChild.put("startDate", toUpdateStartDate)
                   updatableChild.put("deadLine", toUpdateDeadline)
                   updatableChild.put("latestActivity", toUpdateLatestActivity)
                   updatableChild.put("taskDeadline", toUpdateTaskDeadline)
                   updatableChild.put("taskStatus", toUpdateTaskStatus)
                   updatableChild.put("overallProgress", toUpdateOverallProgress)


                   /*update in database*/

                   database.updateChildren(updatableChild)

                   val intent= Intent(this,MainActivity::class.java)
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

                   params.put("clientname", getClientName)
                   params.put("oldclientname", intent.getStringExtra("cN"))

                   params.put("startdate", getStartDate)
                   params.put("oldstartdate",startDate)

                   params.put("deadline", getDeadline)
                   params.put("olddeadline", deadline)

                   params.put("overallprogress", getoverallProgress)
                   params.put("oldoverallprogress", overallprogress)

                   params.put("latestactivity", getlatestActivity)
                   params.put("oldlatestactivity", latestactivity)

                   params.put("taskdeadline", gettaskDeadline)
                   params.put("oldtaskdeadline", taskdeadline)

                   params.put("taskstatus", gettaskStatus)
                   params.put("oldtaskstatus", taskstatus)

                   return params
               }
           }



           VolleySingleton.instance?.addToRequestQueue(stringRequest)
       }
   }




}