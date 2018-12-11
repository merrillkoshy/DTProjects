package dot10tech.com.dot10projects.Admin.ClientDetails

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.activity_addprojectasadmin.*
import org.json.JSONException
import org.json.JSONObject


class AddNewProjectDetails:AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addprojectasadmin)
        initialise()
    }

    fun initialise(){

        val clientlogourl=intent.getStringExtra("clientLogo")
        val clientName=intent.getStringExtra("clientName")

        title=""

        projecttitle.text=clientName
        projecttitle.paintFlags= Paint.UNDERLINE_TEXT_FLAG

        Picasso.get().load(clientlogourl).
            fit().
            placeholder(R.drawable.progress_animation).
            into(mast)

        startDate.setHintTextColor(resources.getColor(R.color.white))
        startDate.setTextColor(resources.getColor(R.color.selected_dot_color))


        deadline.setHintTextColor(resources.getColor(R.color.white))
        deadline.setTextColor(resources.getColor(R.color.selected_dot_color))


        firsttask.setHintTextColor(resources.getColor(R.color.white))
        firsttask.setTextColor(resources.getColor(R.color.selected_dot_color))


        firsttaskdeadline.setHintTextColor(resources.getColor(R.color.white))
        firsttaskdeadline.setTextColor(resources.getColor(R.color.selected_dot_color))

        addc.setOnClickListener {
            addLogo()
            addActivity() }


    }

    private fun addLogo() {
        val clientlogourl=intent.getStringExtra("clientLogo")
        val clientName=intent.getStringExtra("clientName")

        val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.NEW_CLIENTLOGO, Response.Listener<String>{
                response ->
            try {
                val obj = JSONObject(response)
                Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()

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
                params.put("clientname", clientName)
                params.put("clientlogourl", clientlogourl)
                return params

            }
        }



        VolleySingleton.instance?.addToRequestQueue(stringRequest)

    }

    private fun addActivity() {
        val startDate=startDate.text.toString().trim()
        val deadline=deadline.text.toString().trim()
        val firsttask=firsttask.text.toString().trim()
        val firsttaskdeadline=firsttaskdeadline.text.toString().trim()
        val clientlogourl=intent.getStringExtra("clientLogo")
        val clientName=intent.getStringExtra("clientName")

            val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.NEW_CLIENT, Response.Listener<String>{
                    response ->
                try {
                    val obj = JSONObject(response)
                    Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()

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

                    params.put("clientname", clientName)


                    params.put("startdate", startDate)


                    params.put("deadline", deadline)


                    params.put("overallprogress", "1")


                    params.put("latestactivity", firsttask)

                    params.put("taskdeadline", firsttaskdeadline)

                    params.put("taskstatus", "Pending")

                    params.put("clientlogourl", clientlogourl)

                    return params
                }
            }



            VolleySingleton.instance?.addToRequestQueue(stringRequest)

    }




}