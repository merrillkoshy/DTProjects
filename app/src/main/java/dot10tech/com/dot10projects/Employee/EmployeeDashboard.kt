package dot10tech.com.dot10projects.Employee

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.R
import kotlinx.android.synthetic.main.activity_employeedashboard.*
import android.widget.LinearLayout
import dot10tech.com.dot10projects.MainActivity
import android.widget.EditText
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import dot10tech.com.dot10projects.Admin.AdminMenu
import dot10tech.com.dot10projects.Networking.VolleySingleton
import dot10tech.com.dot10projects.UI.EndPoints
import kotlinx.android.synthetic.main.activity_editclientdetail.*
import kotlinx.android.synthetic.main.chatbox_item.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class EmployeeDashboard:AppCompatActivity(), GestureDetector.OnGestureListener{

    private  var commentpost= String()
    private var dateandtime= String()
    private var username= String()
    var gDetector: GestureDetectorCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialise()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        this.gDetector?.onTouchEvent(event)
        //calling the superclass implementation
        return super.onTouchEvent(event)
    }

    fun initialise(){

        setContentView(R.layout.activity_employeedashboard)
        this.gDetector = GestureDetectorCompat(this, this)

        val startDate=intent.getStringExtra("sd")

        val taskdeadline=intent.getStringExtra("td")
        val taskstatus=intent.getStringExtra("ts")

        masthead()
        progress()
        reporting()
    }

    fun masthead(){
        val imageUrl=intent.getStringExtra("ciU").replace("\\","")
        val clientName=intent.getStringExtra("cN")
        val deadlinedate=intent.getStringExtra("dl").replace("\\","")
        val latestactivity=intent.getStringExtra("la")
        username=intent.getStringExtra("username")


        Picasso.get().
            load(imageUrl).fit().placeholder(R.drawable.progress_animation).into(clientlogo)


        clientname.text=clientName
        deadline.text=deadlinedate
        latestactivityhead.text=latestactivity
        reportasname.text=username

    }

    fun progress() {

        val progressVal=intent.getStringExtra("op").toInt()
        opPercentage.text=""+progressVal+" %"
        if(progressVal==100){
            progressBar.progress=progressVal
            val progressDrawable = progressBar.progressDrawable.mutate()
            progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
            progressBar.progressDrawable = progressDrawable
        }
        else{
            when(progressVal<100){
                progressVal<50->{progressBar.progress=progressVal
                    val progressDrawable = progressBar.progressDrawable.mutate()
                    progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
                    progressBar.progressDrawable = progressDrawable}
                progressVal==50->{progressBar.progress=progressVal
                    val progressDrawable = progressBar.progressDrawable.mutate()
                    progressDrawable.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN)
                    progressBar.progressDrawable = progressDrawable}
                progressVal>50->{progressBar.progress=progressVal
                    val progressDrawable = progressBar.progressDrawable.mutate()
                    progressDrawable.setColorFilter(getColor(R.color.orange), android.graphics.PorterDuff.Mode.SRC_IN)
                    progressBar.progressDrawable = progressDrawable}
            }
        }



    }

    fun reporting(){

        reportcomment.setOnClickListener {
            val builder = AlertDialog.Builder(this@EmployeeDashboard)

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
                commentpost=editText.text.toString().trim()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val current=LocalTime.now().toString()
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
                    dateandtime =  current.format(formatter)
                }else {
                    var date = Date();
                    val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
                    dateandtime = formatter.format(date)

                }
                Log.d("comment",commentpost)
                Log.d("date and time",dateandtime)
                /*addMessage()*/

            }



            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel"){_,_ ->
                Toast.makeText(applicationContext,"You cancelled the dialog.",Toast.LENGTH_SHORT).show()
            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        }
    }

    override fun onShowPress(e: MotionEvent?) {
        print(e)
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        if (e1!!.getX() - e2!!.getX() > 50) {

            val startchatbox=Intent(this,Chatbox::class.java)

            startchatbox.putExtra("username",username)
            startchatbox.putExtra("message",commentpost)
            startchatbox.putExtra("dateandtime",dateandtime)

            startActivity(startchatbox)
            return true;
        }
        else {
            return true;
        }
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        print(e)
    }

    fun addMessage(){
            val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.UPDATE_URL, Response.Listener<String>{
                    response ->
                try {
                    val obj = JSONObject(response)
                    Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()

                    val intent= Intent(this,EmployeeDashboard::class.java)
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

                    params.put("message", commentpost)
                    return params
                }
            }
            VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }


}