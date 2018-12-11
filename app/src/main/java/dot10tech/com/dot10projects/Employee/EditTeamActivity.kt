package dot10tech.com.dot10projects.Employee

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import dot10tech.com.dot10projects.R
import kotlinx.android.synthetic.main.activity_editteam.*

class EditTeamActivity:AppCompatActivity(){

    var affiliation= String()
    var assignment= String()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editteam)

        spinner()
        edittexts()
        teamupdatebtn.setOnClickListener {  }
    }

    fun spinner(){

        val staffName=intent.getStringArrayListExtra("staffName")
        val staffAssignment=intent.getStringArrayListExtra("staffAssignment")
        val staffAffiliation=intent.getStringArrayListExtra("staffAffiliation")


        Log.d("staff1",staffName[0])
        staffselectionspinner.adapter=ArrayAdapter(this,R.layout.spinner_item,staffName)

        //item selected listener for spinner
        staffselectionspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(this@EditTeamActivity, staffName[p2], LENGTH_LONG).show()
                assignment=staffAssignment[p2]
                affiliation=staffAffiliation[p2]
            }

        }

        staff_assignment.hint=assignment
        staff_affiliation.hint=affiliation



    }

    fun edittexts(){
        val staffName=staff_name.text.toString().trim()
        val staffAssignment=staff_assignment.text.toString().trim()
        val staffAffiliation=staff_affiliation.text.toString().trim()


    }
}