package dot10tech.com.dot10projects.Admin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.LoginActivity
import dot10tech.com.dot10projects.MainActivity
import dot10tech.com.dot10projects.R
import kotlinx.android.synthetic.main.activity_loggedinas.*

class LoggedInAs : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val profilePicFromDB=intent.getStringExtra("profilePic")
            val firstName=intent.getStringExtra("fN")
            val lastName=intent.getStringExtra("lN")
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("pp",profilePicFromDB)
            intent.putExtra("fn",firstName)
            intent.putExtra("ln",lastName)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loggedinas)
        val profilePicFromDB=intent.getStringExtra("profilePic")
        val firstName=intent.getStringExtra("fN")
        val lastName=intent.getStringExtra("lN")

        val rtlAnim=AnimationUtils.loadAnimation(this,R.anim.animation_right_to_left)
        val ltrAnim=AnimationUtils.loadAnimation(this,R.anim.animation_left_to_right)
        Picasso.get().load(profilePicFromDB.replace("\\","")).placeholder(R.drawable.progress_animation).into(profilePic)
        profilePic.startAnimation(rtlAnim)

        loggedinas.text="LOGGED IN AS"
        loggedinas.startAnimation(ltrAnim)
        firstname.text=firstName
        firstname.startAnimation(ltrAnim)
        lastname.text=lastName
        lastname.startAnimation(ltrAnim)

        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

}