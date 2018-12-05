package dot10tech.com.dot10projects

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.Admin.ClientDetails.ClientDetailsData
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))

        setContentView(R.layout.content_main)
        val pp=intent.getStringExtra("pp")
        val fn=intent.getStringExtra("fn")
        val ln=intent.getStringExtra("ln")
        mainPageAction()
    }

    fun mainPageAction(){

        Picasso.get().load("https://www.dot10tech.com/mobileapp/assets/addnew.png").placeholder(R.drawable.progress_animation).into(addProject)
        addProject.setOnClickListener {
            val intent=Intent(this@MainActivity,ClientDetailsData()::class.java)
            intent.putExtra("option",1)
            startActivity(intent)
        }

        Picasso.get().load("https://www.dot10tech.com/mobileapp/assets/editProject.png").placeholder(R.drawable.progress_animation).into(editProject)
        editProject.setOnClickListener {
            val intent=Intent(this@MainActivity,ClientDetailsData()::class.java)
            intent.putExtra("option",2)
            startActivity(intent)

        }

        Picasso.get().load("https://www.dot10tech.com/mobileapp/assets/ongoing.png").placeholder(R.drawable.progress_animation).into(onGoingProjects)
        onGoingProjects.setOnClickListener {
            val intent=Intent(this@MainActivity,ClientDetailsData()::class.java)
            intent.putExtra("option",3)
            startActivity(intent)
        }
    }

}
