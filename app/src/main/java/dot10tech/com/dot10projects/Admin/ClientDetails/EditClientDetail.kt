package dot10tech.com.dot10projects.Admin.ClientDetails

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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

class EditClientDetail:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_editclientdetail)

        val stats = arrayOf("-- Select Status --", "Ongoing", "Completed")

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

        op_et.hint=overallprogress
        op_et.setHintTextColor(resources.getColor(R.color.unchanged))

        la_et.hint=latestactivity
        la_et.setHintTextColor(resources.getColor(R.color.unchanged))

        td_et.hint=taskdeadline.replace("\\","")
        td_et.setHintTextColor(resources.getColor(R.color.unchanged))

        ts_et.hint=taskstatus
        ts_et.setHintTextColor(resources.getColor(R.color.unchanged))



        Picasso.
            get().
            load("https:dot10tech.com/mobileApp/assets/appicon.png").
            fit().
            placeholder(R.drawable.progress_animation).
            into(mast)

        updatebtn.setOnClickListener { addActivity() }
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