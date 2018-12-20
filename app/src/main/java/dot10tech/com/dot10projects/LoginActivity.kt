package dot10tech.com.dot10projects

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.FirebaseData.UsersDataClass
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import java.io.IOException

class LoginActivity:AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initializewidgets()
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

            val intent=Intent(this,LoginScreen::class.java)
            intent.putExtra("flag",2)
            startActivity(intent)
        }
    }
}