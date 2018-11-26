package dot10tech.com.dot10projects

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import java.io.IOException

class LoginActivity:AppCompatActivity(){

    private var credsTarget = String()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchJson_CredTable()
        initializewidgets()
    }

    fun fetchJson_CredTable() {
        val url = "https://dot10tech.com/mobileapp/scripts/adminLoginActivity.php"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                //Slicing the response
                credsTarget = body.toString()

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }

    fun initializewidgets(){
        setContentView(R.layout.activity_login)

        Picasso.get().load("https://dot10tech.com/mobileApp/assets/clientlogin.png").placeholder(R.drawable.progress_animation).into(client)
        Picasso.get().load("https://dot10tech.com/mobileApp/assets/stafflogin.png").placeholder(R.drawable.progress_animation).into(employee)
        Picasso.get().load("https://dot10tech.com/mobileApp/assets/adminlogin.png").placeholder(R.drawable.progress_animation).into(admin)
        client.setOnClickListener {
            val intent=Intent(this,LoginScreen::class.java)
            intent.putExtra("flag",0)
            startActivity(intent)
        }
        employee.setOnClickListener {
            val intent=Intent(this,LoginScreen::class.java)
            intent.putExtra("flag",1)
            startActivity(intent)
        }
        val adminScreen=findViewById<ImageView>(R.id.admin)
        adminScreen.setOnClickListener {
            val credsArray = credsTarget.replace("[", "").replace("]", "").replace("\"", "").split(",")
            val usernames = ArrayList<String>()
            val passwords = ArrayList<String>()
            val profilePic = ArrayList<String>()

            var i = 0
            var j = 2
            var k = 1
            Log.d("size", "" + credsArray.size)
            val size = credsArray.size
            while (i < size) {
                val un = credsArray[i]
                usernames.add(un)
                i += 2

            }
            while (k < size) {
                val pw = credsArray[k]
                passwords.add(pw)
                k += 2
            }
            while (j < size) {
                val pw = credsArray[j]
                profilePic.add(pw)
                j += 2
            }

            val intent=Intent(this,LoginScreen::class.java)
            intent.putExtra("usernames", usernames)
            intent.putExtra("passwords", passwords)
            intent.putExtra("profilePic", profilePic)
            intent.putExtra("flag",2)
            startActivity(intent)
        }
    }
}