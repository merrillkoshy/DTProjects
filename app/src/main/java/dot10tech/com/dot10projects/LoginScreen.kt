package dot10tech.com.dot10projects

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.Admin.LoggedInAs
import kotlinx.android.synthetic.main.activity_loginscreen.*

class LoginScreen:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseWidgets()
    }
    fun initialiseWidgets(){
        setContentView(R.layout.activity_loginscreen)
        val flag=intent.getIntExtra("flag",3)
        title="Login"
        Picasso.get().load("https://dot10tech.com/mobileApp/assets/appicon.png").placeholder(R.drawable.progress_animation).into(masthead)
        val clientintent=Intent(this,ClientActivity::class.java)
        val employeeintent=Intent(this,EmployeeActivity::class.java)
        val adminintent=Intent(this,LoggedInAs::class.java)
        Picasso.get().load("https://dot10tech.com/mobileApp/assets/loginbtn.png").placeholder(R.drawable.progress_animation).into(login)

        val adminUsernameFromDB=intent.getStringArrayListExtra("usernames")
        val adminPasswordFromDB=intent.getStringArrayListExtra("passwords")
        val profilePicFromDB=intent.getStringArrayListExtra("profilePic")

        when(flag){
            0->{login.setOnClickListener {
                if (username.text.toString() == "test" && password.text.toString() == "test") {
                    startActivity(clientintent)
                    }
                }
                }
            1->{login.setOnClickListener {
                if (username.text.toString() == "test" && password.text.toString() == "test") {
                    startActivity(employeeintent)
                }
            }

            }
            2->{login.setOnClickListener {
                if (username.text.toString() == adminUsernameFromDB[0] && password.text.toString() == adminPasswordFromDB[0]) {
                    intent.putExtra("profilePic",profilePicFromDB[0])
                    Log.d("profilePic",profilePicFromDB[0])
                    startActivity(adminintent)
                }
            }

            }
            else -> {
                Log.d("error", "ParseError")
            }
        }

    }
}