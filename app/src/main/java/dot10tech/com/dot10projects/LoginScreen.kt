package dot10tech.com.dot10projects

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_loginscreen.*

class LoginScreen:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseWidgets()
    }
    fun initialiseWidgets(){
        setContentView(R.layout.activity_loginscreen)
        val flag=intent.getIntExtra("flag",3)

        val clientintent=Intent(this,ClientActivity::class.java)
        val employeeintent=Intent(this,EmployeeActivity::class.java)
        val adminintent=Intent(this,MainActivity::class.java)
        login.setOnClickListener {
            if (username.text.toString() == "test" && password.text.toString() == "test") {
                when (flag) {
                    0 -> startActivity(clientintent)
                    1 -> startActivity(employeeintent)
                    2 -> startActivity(adminintent)
                    else -> {
                        Log.d("error", "ParseError")
                    }
                }
            }
        }
    }
}