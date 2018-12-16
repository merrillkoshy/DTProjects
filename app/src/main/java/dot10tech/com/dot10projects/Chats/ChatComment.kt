package dot10tech.com.dot10projects.Chats

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import dot10tech.com.dot10projects.Networking.VolleySingleton
import dot10tech.com.dot10projects.R
import dot10tech.com.dot10projects.UI.EndPoints
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChatComment:AppCompatActivity() {

    private var commentpost= String()
    private var dateandtime= String()
    private var username= String()
    private var commentedpic= String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = AlertDialog.Builder(this@ChatComment)

        username=intent.getStringExtra("username")
        // Set the alert dialog title
        builder.setTitle("Comment")
        builder.setIcon(R.drawable.ic_menu_send)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.post_a_comment, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.etComments)
        builder.setView(dialogLayout)


        // Display a message on alert dialog
        builder.setMessage("Post a comment")

        // Set a positive button and its click listener on alert dialog

        builder.setPositiveButton("SEND"){dialog, which ->


            commentpost = editText.text.toString().trim()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalTime.now().toString()
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
                dateandtime = current.format(formatter)
            } else {
                var date = Date();
                val formatter = SimpleDateFormat("MMM dd yyyy/HH:mma")
                dateandtime = formatter.format(date)

            }
            Log.d("comment", commentpost)
            Log.d("date and time", dateandtime)
            if(commentpost!=""){addMessage()}

        }



        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel"){_,_ ->
            Toast.makeText(applicationContext,"You cancelled the dialog.", Toast.LENGTH_SHORT).show()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    fun addMessage(){
        val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.ADDNEWCHAT, Response.Listener<String>{
                response ->
            try {
                val obj = JSONObject(response)
                Toast.makeText(applicationContext, obj.getString("output"), Toast.LENGTH_SHORT).show()


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

                params.put("message", commentpost)
                params.put("username", username)
                params.put("dateandtime", dateandtime)
                params.put("picurl", commentedpic)
                params.put("category", "Team")
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
        finish()
    }



}