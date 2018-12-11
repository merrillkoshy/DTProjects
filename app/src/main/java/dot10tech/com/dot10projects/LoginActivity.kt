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
    private var usercredsTarget= String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchJson_UserCredTable()
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

    fun fetchJson_UserCredTable() {
        val url = "https://dot10tech.com/mobileapp/scripts/teamscripts/userLoginactivity.php"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                //Slicing the response
                usercredsTarget = body.toString()

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
            val credsArray = usercredsTarget.replace("[", "").replace("]", "").replace("\"", "").split(",")
            val usernames = ArrayList<String>()
            val passwords = ArrayList<String>()
            val category = ArrayList<String>()


            var i = 0
            var j = 2
            var k = 1


            Log.d("size", "" + credsArray.size)
            val size = credsArray.size
            while (i < size) {
                val un = credsArray[i]
                usernames.add(un)
                i += 3

            }
            while (k < size) {
                val pw = credsArray[k]
                passwords.add(pw)
                k += 3
            }
            while (j < size) {
                val pw = credsArray[j]
                category.add(pw)
                j += 3
            }


            val intent=Intent(this,LoginScreen::class.java)
            intent.putExtra("usernames", usernames)
            intent.putExtra("passwords", passwords)
            intent.putExtra("category", category)
            intent.putExtra("flag",1)
            startActivity(intent)
        }
        val adminScreen=findViewById<ImageView>(R.id.admin)
        adminScreen.setOnClickListener {
            val credsArray = credsTarget.replace("[", "").replace("]", "").replace("\"", "").split(",")
            val usernames = ArrayList<String>()
            val passwords = ArrayList<String>()
            val profilePic = ArrayList<String>()
            val firstName = ArrayList<String>()
            val lastName = ArrayList<String>()

            var i = 0
            var j = 2
            var k = 1
            var l = 3
            var m = 4

            Log.d("size", "" + credsArray.size)
            val size = credsArray.size
            while (i < size) {
                val un = credsArray[i]
                usernames.add(un)
                i += 5

            }
            while (k < size) {
                val pw = credsArray[k]
                passwords.add(pw)
                k += 5
            }
            while (j < size) {
                val pw = credsArray[j]
                profilePic.add(pw)
                j += 5
            }

            while (l < size) {
                val pw = credsArray[l]
                firstName.add(pw)
                l += 5
            }

            while (m < size) {
                val pw = credsArray[m]
                lastName.add(pw)
                m += 5
            }

            val intent=Intent(this,LoginScreen::class.java)
            intent.putExtra("usernames", usernames)
            intent.putExtra("passwords", passwords)
            intent.putExtra("profilePic", profilePic)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("flag",2)
            startActivity(intent)
        }
    }
}