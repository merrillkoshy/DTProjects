package dot10tech.com.dot10projects.Admin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.Volley
import dot10tech.com.dot10projects.Networking.VolleySingleton
import dot10tech.com.dot10projects.R

class EditProjectasAdmin : AppCompatActivity() {

    var activity: EditText? = null
    var day: Spinner? = null
    val addUrl : String = "https://dot10tech.com/mobileApp/scripts/addActivity.php"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editasadmin)

        //finding views
        activity = findViewById(R.id.activity)
        day = findViewById(R.id.day)
        val btnAaddActivity = findViewById<Button>(R.id.addactivity)
        btnAaddActivity.setOnClickListener {
            //we send user data to database
            addActivity()
        }

    }

    private fun addActivity() {
        val getActivity = activity?.text.toString()
        val getDay = day?.selectedItem.toString()
        val stringRequest = object : StringRequest(Request.Method.POST,addUrl,Response.Listener<String>{
                response ->
            try {
                val obj = JSONObject(response)
                Log.d("finalmes",obj.getString("message"))
                Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()
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
                params.put("activity", getActivity)
                params.put("day", getDay)
                return params
            }
        }



        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}