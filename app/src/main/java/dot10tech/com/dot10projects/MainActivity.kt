package dot10tech.com.dot10projects

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.Admin.AddNewProjectasAdmin
import dot10tech.com.dot10projects.Admin.BurgerMenu.AddorRemoveUser
import dot10tech.com.dot10projects.Admin.BurgerMenu.Profile
import dot10tech.com.dot10projects.Admin.EditProjectasAdmin
import dot10tech.com.dot10projects.Admin.OngoingProjects
import dot10tech.com.dot10projects.FirebaseData.ClientsDetailsDataClass

import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.*
import java.io.IOException




class MainActivity : AppCompatActivity() {

    lateinit var clientdetailsLoad:MutableList<ClientsDetailsDataClass>
    val clientName = ArrayList<String>()
    val clientImageUrl = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.content_main)


        val toolbar:Toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.overflowIcon=resources.getDrawable(R.drawable.ic_burger)


        mainPageAction()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                val pp=intent.getStringExtra("pp")
                val fn=intent.getStringExtra("fn")
                val ln=intent.getStringExtra("ln")
                val intent=Intent(this,Profile::class.java)
                intent.putExtra("pp",pp)
                intent.putExtra("fn",fn)
                intent.putExtra("ln",ln)
                startActivity(intent)
                return true
            }
            R.id.addremoveuser -> {
                val intent=Intent(this,AddorRemoveUser::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_settings -> {
                Toast.makeText(this, "Sample_Text", Toast.LENGTH_SHORT).show()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }







    fun mainPageAction(){
        clientdetailsLoad= mutableListOf()
        val database= FirebaseDatabase.getInstance().getReference()
        database.child("clientDetailsData").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children){
                    Log.d("test", "Value is: ${user.value}")
                    val clientdeets=dataSnapshot.child(0.toString()).getValue(ClientsDetailsDataClass::class.java)
                    clientdetailsLoad.add(clientdeets!!)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("test", "Failed to read value.", error.toException())
            }
        })


        Picasso.get().load("https://www.dot10tech.com/mobileapp/assets/addnew.png").placeholder(R.drawable.progress_animation).into(addProject)
        addProject.setOnClickListener {


            val intent=Intent(this, AddNewProjectasAdmin()::class.java)

            intent.putExtra("cN", clientdetailsLoad[0].clientName)
            intent.putExtra("ciU", clientdetailsLoad[0].clientImageUrl)
            startActivity(intent)
        }

        Picasso.get().load("https://www.dot10tech.com/mobileapp/assets/editProject.png").placeholder(R.drawable.progress_animation).into(editProject)
        editProject.setOnClickListener {


            val intent=Intent(this, EditProjectasAdmin()::class.java)
            intent.putExtra("category","Admin")
            intent.putExtra("cN", clientdetailsLoad[0].clientName)
            intent.putExtra("ciU", clientdetailsLoad[0].clientImageUrl)
            startActivity(intent)

        }

        Picasso.get().load("https://www.dot10tech.com/mobileapp/assets/ongoing.png").placeholder(R.drawable.progress_animation).into(onGoingProjects)
        onGoingProjects.setOnClickListener {



            val fn=intent.getStringExtra("fn")
            val ln=intent.getStringExtra("ln")
            val intent=Intent(this, OngoingProjects()::class.java)
            intent.putExtra("category","Admin")
            intent.putExtra("fn",fn)
            intent.putExtra("ln",ln)
            intent.putExtra("cN", clientdetailsLoad[0].clientName)
            intent.putExtra("ciU", clientdetailsLoad[0].clientImageUrl)
            startActivity(intent)
        }
    }

}
