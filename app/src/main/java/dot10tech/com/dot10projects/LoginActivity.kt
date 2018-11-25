package dot10tech.com.dot10projects

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializewidgets()
    }

    fun initializewidgets(){
        setContentView(R.layout.activity_login)
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