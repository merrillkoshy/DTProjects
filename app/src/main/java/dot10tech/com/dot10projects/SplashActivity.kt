package dot10tech.com.dot10projects

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.database.FirebaseDatabase
import dot10tech.com.dot10projects.Admin.ClientDetails.ClientDetailActivity
import dot10tech.com.dot10projects.DataLayer.AdminCredentials
import dot10tech.com.dot10projects.DataLayer.ClientDetails
import dot10tech.com.dot10projects.DataLayer.ProjectDetails
import dot10tech.com.dot10projects.DataLayer.UserCredentials


class SplashActivity : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Initialize the Handler
        mDelayHandler = Handler()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        UserCredentials().fetchJson_UserCredTable()
        AdminCredentials().fetchJson_CredTable()
        ClientDetails().fetchJson()
        ProjectDetails().fetchProjectDetailsasJson()

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