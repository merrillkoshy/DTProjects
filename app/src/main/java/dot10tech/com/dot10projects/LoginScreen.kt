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
        val usercategory=intent.getStringArrayListExtra("category")
        val adminPasswordFromDB=intent.getStringArrayListExtra("passwords")
        val profilePicFromDB=intent.getStringArrayListExtra("profilePic")
        val firstNameFromDB=intent.getStringArrayListExtra("firstName")
        val lastNameFromDB=intent.getStringArrayListExtra("lastName")

        var j=0
        while(j<adminUsernameFromDB.size)
        {
            Log.d("usernames",adminUsernameFromDB[j])
            j++
        }



        when(flag){
            0->{login.setOnClickListener {
                if (username.text.toString() == "test" && password.text.toString() == "test") {
                    startActivity(clientintent)
                    }
                }
                }
            1->{login.setOnClickListener {
                val size=adminUsernameFromDB.size
                var i=0
                var match="fail"
                var pos=0
                var getP=""

                while(i<size)
                {

                    if (adminUsernameFromDB[i].trim()==username.text.trim().toString())
                    {
                        match="pass"
                        pos=i
                        getP=adminPasswordFromDB[i]
                        break
                    }
                    i++
                }
                if(getP==password.text.trim().toString())
                    match+="pass"
                Log.d("match",match)
                if (match=="passpass") {
                    val category=usercategory[pos]


                    if(category!=null) {
                        employeeintent.putExtra("category", category)

                        startActivity(employeeintent)
                    }
                    else{
                        Log.d("category","stillNULL")
                    }

                }
            }

            }
            2->{login.setOnClickListener {
                val size=adminUsernameFromDB.size
                var i=0
                var match="fail"
                var pos=0
                var getP=""

                while(i<size)
                {

                    if (adminUsernameFromDB[i].trim()==username.text.trim().toString())
                    {
                        match="pass"
                        pos=i
                        getP=adminPasswordFromDB[i]
                        break
                    }
                    i++
                }
                if(getP==password.text.trim().toString())
                    match+="pass"
                Log.d("match",match)
                if (match=="passpass") {
                    val profilePic=profilePicFromDB[pos]
                    val fn=firstNameFromDB[pos]
                    val ln=lastNameFromDB[pos]
                    Log.d("profilePic",profilePic)
                    if(profilePic!=null) {
                        adminintent.putExtra("profilePic", profilePic)
                        adminintent.putExtra("fN", fn)
                        adminintent.putExtra("lN", ln)
                        startActivity(adminintent)
                    }
                    else{
                        Log.d("profilePicStatus","stillNULL")
                    }
                }
            }

            }
            else -> {
                Log.d("error", "ParseError")
            }
        }

    }
}