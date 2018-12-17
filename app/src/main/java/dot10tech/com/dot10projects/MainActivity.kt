package dot10tech.com.dot10projects

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast


import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.Admin.AddNewProjectasAdmin
import dot10tech.com.dot10projects.Admin.BurgerMenu.AddorRemoveUser
import dot10tech.com.dot10projects.Admin.BurgerMenu.Profile
import dot10tech.com.dot10projects.Admin.ClientDetails.ClientDetailsData
import dot10tech.com.dot10projects.Admin.EditProjectasAdmin
import dot10tech.com.dot10projects.Admin.OngoingProjects
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private var target = String()
    val clientName = ArrayList<String>()
    val clientImageUrl = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.content_main)


        val toolbar:Toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.overflowIcon=resources.getDrawable(R.drawable.ic_burger)


        fetchJson()
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



    fun mainPageAction(){

        Picasso.get().load("https://www.dot10tech.com/mobileapp/assets/addnew.png").placeholder(R.drawable.progress_animation).into(addProject)
        addProject.setOnClickListener {
            val clientArray = target.replace("[", "").replace("]", "").replace("\"", "").split(",")

            var i = 0
            var k = 1

            Log.d("size", "" + clientArray.size)
            val size = clientArray.size
            while (i < size) {
                val un = clientArray[i]
                clientName.add(un)
                i += 2

            }
            while (k < size) {
                val pw = clientArray[k]
                clientImageUrl.add(pw)
                k += 2
            }

            val intent=Intent(this, AddNewProjectasAdmin()::class.java)

            intent.putExtra("cN", clientName)
            intent.putExtra("ciU", clientImageUrl)
            startActivity(intent)
        }

        Picasso.get().load("https://www.dot10tech.com/mobileapp/assets/editProject.png").placeholder(R.drawable.progress_animation).into(editProject)
        editProject.setOnClickListener {
            val clientArray = target.replace("[", "").replace("]", "").replace("\"", "").split(",")

            var i = 0
            var k = 1

            Log.d("size", "" + clientArray.size)
            val size = clientArray.size
            while (i < size) {
                val un = clientArray[i]
                clientName.add(un)
                i += 2

            }
            while (k < size) {
                val pw = clientArray[k]
                clientImageUrl.add(pw)
                k += 2
            }

            val intent=Intent(this, EditProjectasAdmin()::class.java)

            intent.putExtra("cN", clientName)
            intent.putExtra("ciU", clientImageUrl)
            startActivity(intent)

        }

        Picasso.get().load("https://www.dot10tech.com/mobileapp/assets/ongoing.png").placeholder(R.drawable.progress_animation).into(onGoingProjects)
        onGoingProjects.setOnClickListener {
            val clientArray = target.replace("[", "").replace("]", "").replace("\"", "").split(",")

            var i = 0
            var k = 1

            Log.d("size", "" + clientArray.size)
            val size = clientArray.size
            while (i < size) {
                val un = clientArray[i]
                clientName.add(un)
                i += 2

            }
            while (k < size) {
                val pw = clientArray[k]
                clientImageUrl.add(pw)
                k += 2
            }
            val fn=intent.getStringExtra("fn")
            val ln=intent.getStringExtra("ln")
            val intent=Intent(this, OngoingProjects()::class.java)
            intent.putExtra("fn",fn)
            intent.putExtra("ln",ln)
            intent.putExtra("cN", clientName)
            intent.putExtra("ciU", clientImageUrl)
            startActivity(intent)
        }
    }

}
