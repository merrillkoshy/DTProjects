package dot10tech.com.dot10projects.Client

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.Chats.Chatbox
import dot10tech.com.dot10projects.R
import kotlinx.android.synthetic.main.activity_clientdetail.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import java.io.IOException
import java.util.ArrayList

class ClientActivity:AppCompatActivity(),GestureDetector.OnGestureListener{
    var gDetector: GestureDetectorCompat? = null
    private var target = String()
    private var commentpost= String()
    private var dateandtime= String()
    private var username= String()
    private var category=String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseWidgets()
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        this.gDetector?.onTouchEvent(event)
        //calling the superclass implementation
        return super.onTouchEvent(event)
    }

    fun initialiseWidgets(){
        setContentView(R.layout.activity_clientdetail)
        fetchJson()

        Picasso.get().
            load(intent.getStringExtra("ciU"))
            .fit().
                placeholder(R.drawable.progress_animation).
                into(clientLogo)

        val startDate=intent.getStringExtra("sd").replace("\\","")
        val deadline=intent.getStringExtra("dl").replace("\\","")

        val latestactivity=intent.getStringExtra("la")
        val taskdeadline=intent.getStringExtra("td").replace("\\","")
        val taskstatus=intent.getStringExtra("ts").replace("\\t","")


        clientNameTitle.text="CLIENT"
        clientName.text=intent.getStringExtra("cN")

        dates.text="Start Date : "+startDate+"\n\n"+"Project Deadline : "+deadline
        progress()


        latestActivity.text=" "+latestactivity


        taskDeadline.text=" "+taskdeadline


        taskStatus.text=" "+taskstatus

        this.gDetector = GestureDetectorCompat(this, this)

    }
    fun fetchJson() {
        val url = "https://dot10tech.com/mobileapp/scripts/chats/viewChat.php"

        val client = OkHttpClient()
        val request = okhttp3.Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body()?.string()

                //Slicing the response
                target = body.toString()

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })

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
            val clientArray = target.replace("[", "").replace("]", "").replace("\"", "").split(",")

            Log.d("commentpostsize",""+clientArray.size)
            Log.d("target",""+target[0])
            var i = 0
            var j = 1
            var k = 2
            var l = 3

            val usernames = ArrayList<String>()
            val messages = ArrayList<String>()
            val dateandtimes = ArrayList<String>()
            val categories = ArrayList<String>()

            Log.d("size", "" + clientArray.size)
            val size = clientArray.size
            while (i < size) {
                val un = clientArray[i]
                usernames.add(un)
                i += 4

            }
            while (j < size) {
                val pw = clientArray[j]
                messages.add(pw)
                j += 4
            }
            while (k < size) {
                val pw = clientArray[k]
                dateandtimes.add(pw)
                k += 4
            }
            while (l < size) {
                val pw = clientArray[l]
                categories.add(pw)
                l += 4
            }

            val clientName=intent.getStringExtra("cN")
            username=intent.getStringExtra("cN")
            category="Admin"
            val imageUrl=intent.getStringExtra("ciU").replace("\\","")

            val startchatbox= Intent(this, Chatbox::class.java)

            startchatbox.putExtra("affliation_icon",imageUrl)
            startchatbox.putExtra("clientname",clientName)
            startchatbox.putExtra("usernames",usernames)
            startchatbox.putExtra("messages",messages)
            startchatbox.putExtra("dateandtimes",dateandtimes)
            startchatbox.putExtra("categories",categories)
            startchatbox.putExtra("category",category)
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

}