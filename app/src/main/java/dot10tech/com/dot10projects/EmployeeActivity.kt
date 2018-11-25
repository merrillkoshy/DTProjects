package dot10tech.com.dot10projects

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class EmployeeActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseWidgets()
    }
    fun initialiseWidgets(){
        setContentView(R.layout.activity_employee)
    }
}