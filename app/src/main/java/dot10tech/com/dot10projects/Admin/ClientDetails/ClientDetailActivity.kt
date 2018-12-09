package dot10tech.com.dot10projects.Admin.ClientDetails

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.R
import kotlinx.android.synthetic.main.activity_clientdetail.*
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.RelativeLayout
import dot10tech.com.dot10projects.Admin.AdminMenu
import kotlinx.android.synthetic.main.admin_menu.*
import dot10tech.com.dot10projects.MainActivity




class ClientDetailActivity:AppCompatActivity(), GestureDetector.OnGestureListener {

    var gDetector: GestureDetectorCompat? = null

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
        if (e2!!.getX() - e1!!.getX() > 50) {
            val a_intent=Intent(this,AdminMenu::class.java)
            a_intent.putExtra("cN",intent.getStringExtra("cN"))
            startActivity(a_intent)
            this.overridePendingTransition(R.anim.menu_drawer_close,R.anim.menu_drawer_open)
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