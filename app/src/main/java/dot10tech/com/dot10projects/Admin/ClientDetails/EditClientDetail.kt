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
import dot10tech.com.dot10projects.Employee.EditTeamActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_editclientdetail)


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

    fun optiontabs(){
        val clientArray = targetEmp.replace("[", "").replace("]", "").replace("\"", "").split(",")

        var a = 0
        var b = 1
        var c = 2
        var d = 3

        Log.d("size", "" + clientArray.size)
        val arrsize = clientArray.size
        while (a < arrsize) {
            val un = clientArray[a]
            staffName.add(un)
            a += 4

        }
        while (b < arrsize) {
            val pw = clientArray[b]
            staffAssignment.add(pw)
            b += 4
        }
        while (c < arrsize) {
            val pw = clientArray[c]
            Log.d("workingproject",pw)
            workingProject.add(pw)
            c += 4
        }
        while (d < arrsize) {
            val pw = clientArray[d]
            affiliation.add(pw)
            d += 4
        }

        var e=0
        val indexlist= ArrayList<String>()

        while(e<workingProject.size)
        {
            if(workingProject[e]==intent.getStringExtra("cN"))
            {
                indexlist.add(e.toString())
            }
            e++
        }

        var f=0

        val staffnameforExport= ArrayList<String>()
        val staffassignmentforExport= ArrayList<String>()
        val staffaffiliationforExport= ArrayList<String>()

        while (f<indexlist.size)
        {
            staffnameforExport.add(staffName[indexlist[f].toInt()])
            staffassignmentforExport.add(staffAssignment[indexlist[f].toInt()])
            staffaffiliationforExport.add(affiliation[indexlist[f].toInt()])
            f++
        }

        editTeamTab.setOnClickListener {

            val intent:Intent= Intent(this,EditTeamActivity::class.java)
            intent.putStringArrayListExtra("staffName",staffnameforExport)
            intent.putStringArrayListExtra("staffAssignment",staffassignmentforExport)
            intent.putStringArrayListExtra("staffAffiliation",staffaffiliationforExport)
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