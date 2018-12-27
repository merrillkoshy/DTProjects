package dot10tech.com.dot10projects.Employee

import android.content.Intent
import android.graphics.Color
import android.net.Uri
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
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dot10tech.com.dot10projects.Chats.ChatTestFire
import dot10tech.com.dot10projects.Chats.Chatbox
import dot10tech.com.dot10projects.FirebaseData.ChatDataClass
import dot10tech.com.dot10projects.FirebaseData.TeamAssignmentDataClass
import dot10tech.com.dot10projects.MainActivity
import dot10tech.com.dot10projects.Networking.VolleySingleton
import dot10tech.com.dot10projects.UI.EndPoints
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*


class EmployeeDashboard:AppCompatActivity(), GestureDetector.OnGestureListener{

    private val TAG = MainActivity::class.java.getSimpleName();
    private var target = String()
    private var commentpost= String()

    private var username= String()
    private var theUri=Uri.EMPTY

    private val SERVER_PATH = EndPoints.UPLOAD_URL
    private var commentedpic= String()
    private val READ_REQUEST_CODE= 300
    private val pictureImagePath = ""
    lateinit var chatDetailsList:MutableList<ChatDataClass>

    var gDetector: GestureDetectorCompat? = null

    private val CAMERA = 2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        chatDetailsList= mutableListOf()
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


            val clientName=intent.getStringExtra("cN")
            val imageUrl=intent.getStringExtra("ciU").replace("\\","")
            val startchatbox=Intent(this, ChatTestFire::class.java)

            startchatbox.putExtra("affliation_icon",imageUrl)
            startchatbox.putExtra("clientname",clientName)

            val category=intent.getStringExtra("category")
            Log.d("category1",category)
            startchatbox.putExtra("category",category)
            startchatbox.putExtra("username",username)
            startchatbox.putExtra("message",commentpost)


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

    /*fun addMessage(){
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

                    params.put("picurl", commentedpic)
                    params.put("category", "Team")
                    return params
                }
            }
            VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }*/




    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    companion object {
        private val IMAGE_DIRECTORY = "/DTApp"
    }


}