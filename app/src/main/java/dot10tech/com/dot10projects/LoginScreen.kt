package dot10tech.com.dot10projects

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.Admin.LoggedInAs
import kotlinx.android.synthetic.main.activity_loginscreen.*
import okhttp3.*
import java.io.IOException

class LoginScreen:AppCompatActivity(){
    private var target = String()
    val clientName = ArrayList<String>()
    val clientImageUrl = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchJson()
        initialiseWidgets()
    }

    fun fetchJson() {
        val url = "https://dot10tech.com/mobileapp/scripts/clientDetailLoadApi.php"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                //Slicing the response
                target = body.toString()

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })

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

                val clientArray = target.replace("[", "").replace("]", "").replace("\"", "").split(",")

                var a = 0
                var k = 1

                Log.d("size", "" + clientArray.size)
                val sizes = clientArray.size
                while (a < sizes) {
                    val un = clientArray[a]
                    clientName.add(un)
                    a += 2

                }
                while (k < sizes) {
                    val pw = clientArray[k]
                    clientImageUrl.add(pw)
                    k += 2
                }

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


                    if(category.toString().trim()!="Client") {
                        employeeintent.putExtra("username",adminUsernameFromDB[pos])
                        employeeintent.putExtra("cN", clientName)
                        employeeintent.putExtra("ciU", clientImageUrl)
                        employeeintent.putExtra("category", category)
                        startActivity(employeeintent)
                    }
                    else{
                        val builder = AlertDialog.Builder(this@LoginScreen)

                        // Set the alert dialog title
                        builder.setTitle("Are you in the right loginscreen?")

                        // Display a message on alert dialog
                        builder.setMessage("Sorry, you are authorized")


                        // Display a neutral button on alert dialog
                        builder.setNeutralButton("Go Back"){_,_ ->

                        }

                        // Finally, make the alert dialog using builder
                        val dialog: AlertDialog = builder.create()

                        // Display the alert dialog on app interface
                        dialog.show()
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
                        val builder = AlertDialog.Builder(this@LoginScreen)

                        // Set the alert dialog title
                        builder.setTitle("Are you in the right loginscreen?")

                        // Display a message on alert dialog
                        builder.setMessage("Sorry, you are authorized")


                        // Display a neutral button on alert dialog
                        builder.setNeutralButton("Go Back"){_,_ ->

                        }

                        // Finally, make the alert dialog using builder
                        val dialog: AlertDialog = builder.create()

                        // Display the alert dialog on app interface
                        dialog.show()
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