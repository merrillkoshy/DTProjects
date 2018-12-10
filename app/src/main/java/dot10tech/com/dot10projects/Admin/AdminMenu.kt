package dot10tech.com.dot10projects.Admin

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.Admin.ClientDetails.ClientDetailActivity
import dot10tech.com.dot10projects.R
import dot10tech.com.dot10projects.UI.OptionsViewerAdapter
import kotlinx.android.synthetic.main.admin_menu.*

class AdminMenu:AppCompatActivity(), GestureDetector.OnGestureListener{

    var gDetector: GestureDetectorCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_menu)
        initialiseWidgets()
        this.gDetector = GestureDetectorCompat(this, this)
    }
    fun initialiseWidgets(){
        Picasso.get().load("https://dot10tech.com/mobileApp/assets/team_assignment.png").fit().
            placeholder(R.drawable.progress_animation).into(teamassignmentIcon)
        teamassignmentText.text="Team"+"\n"+"Assignment"

        Picasso.get().load("https://dot10tech.com/mobileApp/assets/tasklist.png").fit().
            placeholder(R.drawable.progress_animation).into(tasklistIcon)
        tasklist.text="Tasklist"

        optionsClicks()
    }

    fun optionsClicks(){

        teamassignmentIcon.setOnClickListener {
            val ta_intent=Intent(this,OptionsViewerAdapter::class.java)


            ta_intent.putStringArrayListExtra("staffName",intent.getStringArrayListExtra("staffName"))
            ta_intent.putStringArrayListExtra("staffAssignment",intent.getStringArrayListExtra("staffAssignment"))
            ta_intent.putStringArrayListExtra("staffAffiliation",intent.getStringArrayListExtra("staffAffiliation"))


            ta_intent.putExtra("clientName",intent.getStringExtra("cN"))
            startActivity(ta_intent)
            overridePendingTransition(R.anim.animation_leave, R.anim.animation_enter)
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        this.gDetector?.onTouchEvent(event)
        //calling the superclass implementation
        return super.onTouchEvent(event)
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
            finish()
            this.overridePendingTransition(R.anim.detail_open,R.anim.drawer_finish)
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
}