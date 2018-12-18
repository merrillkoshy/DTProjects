package dot10tech.com.dot10projects

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.Admin.LoggedInAs
import dot10tech.com.dot10projects.Client.ClientActivity
import kotlinx.android.synthetic.main.activity_loginscreen.*
import okhttp3.*
import java.io.IOException

class LoginScreen:AppCompatActivity(){
    private var target = String()
    private var projecttarget= String()
    val clientName = ArrayList<String>()
    val clientImageUrl = ArrayList<String>()
    val clientNameArray = java.util.ArrayList<String>()
    val startDate = java.util.ArrayList<String>()
    val deadline = java.util.ArrayList<String>()
    val overallprogress = java.util.ArrayList<String>()
    val latestactivity = java.util.ArrayList<String>()
    val taskdeadline = java.util.ArrayList<String>()
    val taskstatus = java.util.ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchJson()
        fetchProjectDetailsasJson()
        initialiseWidgets()
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

    fun fetchProjectDetailsasJson(){
        val url = "https://dot10tech.com/mobileapp/scripts/viewProjectDetails.php"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                //Slicing the response
                projecttarget = body.toString()

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })

    }
    fun initialiseWidgets(){
        setContentView(R.layout.activity_loginscreen)
        val flag=intent.getIntExtra("flag",3)
        title="Login"
        Picasso.get().load("https://dot10tech.com/mobileApp/assets/appicon.png").placeholder(R.drawable.progress_animation).into(masthead)
        val clientintent=Intent(this, ClientActivity::class.java)
        val employeeintent=Intent(this,EmployeeActivity::class.java)
        val adminintent=Intent(this,LoggedInAs::class.java)
        Picasso.get().load("https://dot10tech.com/mobileApp/assets/loginbtn.png").placeholder(R.drawable.progress_animation).into(login)

        val adminUsernameFromDB=intent.getStringArrayListExtra("usernames")
        val usercategory=intent.getStringArrayListExtra("category")
        val adminPasswordFromDB=intent.getStringArrayListExtra("passwords")
        val profilePicFromDB=intent.getStringArrayListExtra("profilePic")
        val firstNameFromDB=intent.getStringArrayListExtra("firstName")
        val lastNameFromDB=intent.getStringArrayListExtra("lastName")

        var j=0
        while(j<adminUsernameFromDB.size)
        {
            Log.d("usernames",adminUsernameFromDB[j])
            j++
        }



        when(flag){
            0-> {
                login.setOnClickListener {

                    val ProjectArray = projecttarget.
                        replace("[", "").
                        replace("]", "").
                        replace("\"", "").split(",")

                    var a = 0
                    var b = 1
                    var l=2
                    var m=3
                    var o=4
                    var p=5
                    var q=6

                    Log.d("size", "" + ProjectArray.size)
                    val projectarraysize = ProjectArray.size
                    while (a < projectarraysize) {
                        val un = ProjectArray[a]
                        clientNameArray.add(un)
                        a += 7

                    }
                    while (b < projectarraysize) {
                        val pw = ProjectArray[b]
                        startDate.add(pw)
                        b += 7
                    }
                    while (l < projectarraysize) {
                        val pw = ProjectArray[l]
                        deadline.add(pw)
                        l += 7
                    }
                    while (m < projectarraysize) {

                        val pw = ProjectArray[m]
                        overallprogress.add(pw)
                        m += 7
                    }
                    while (o < projectarraysize) {
                        val pw = ProjectArray[o]
                        latestactivity.add(pw)
                        o += 7
                    }
                    while (p < projectarraysize) {
                        val pw = ProjectArray[p]
                        taskdeadline.add(pw)
                        p += 7
                    }
                    while (q < projectarraysize) {
                        val pw = ProjectArray[q]
                        taskstatus.add(pw)
                        q += 7
                    }


                    val clientArray = target.replace("[", "").replace("]", "").replace("\"", "").split(",")

                    var d = 0
                    var k = 1

                    Log.d("size", "" + clientArray.size)
                    val sizes = clientArray.size
                    while (d < sizes) {
                        val un = clientArray[d]
                        clientName.add(un)
                        d += 2

                    }
                    while (k < sizes) {
                        val pw = clientArray[k]
                        clientImageUrl.add(pw)
                        k += 2
                    }

                    val size = adminUsernameFromDB.size
                    var i = 0
                    var match = "fail"
                    var pos = 0
                    var getP = ""
                    var name=""

                    while (i < size) {

                        if (adminUsernameFromDB[i].trim() == username.text.trim().toString()) {
                            match = "pass"
                            pos = i
                            name=adminUsernameFromDB[i]
                            getP = adminPasswordFromDB[i]
                            break
                        }
                        i++
                    }
                    if (getP == password.text.trim().toString())
                        match += "pass"
                    Log.d("match", match)
                    if (match == "passpass") {
                        var p=0
                        val clientnameslistsize=clientName.size
                        var c=0
                        while (c<clientnameslistsize){
                            if(clientName[c].toLowerCase().contains(name)){
                                p=c
                            }
                            c++
                        }



                        if (clientName[p] != null) {
                            clientintent.putExtra("cN", clientName[p])
                            clientintent.putExtra("ciU", clientImageUrl[p])

                            clientintent.putExtra("cN",clientName[p])
                            clientintent.putExtra("sd",startDate[p])
                            clientintent.putExtra("dl",deadline[p])
                            clientintent.putExtra("op",overallprogress[p])
                            clientintent.putExtra("la",latestactivity[p])
                            clientintent.putExtra("td",taskdeadline[p])
                            clientintent.putExtra("ts",taskstatus[p])

                            startActivity(clientintent)
                        }
                    }
                }
            }
            1->{login.setOnClickListener {

                val clientArray = target.replace("[", "").replace("]", "").replace("\"", "").split(",")

                var a = 0
                var k = 1

                Log.d("size", "" + clientArray.size)
                val sizes = clientArray.size
                while (a < sizes) {
                    val un = clientArray[a]
                    clientName.add(un)
                    a += 2

                }
                while (k < sizes) {
                    val pw = clientArray[k]
                    clientImageUrl.add(pw)
                    k += 2
                }

                val size=adminUsernameFromDB.size
                var i=0
                var match="fail"
                var pos=0
                var getP=""

                while(i<size)
                {

                    if (adminUsernameFromDB[i].trim()==username.text.trim().toString())
                    {
                        match="pass"
                        pos=i
                        getP=adminPasswordFromDB[i]
                        break
                    }
                    i++
                }
                if(getP==password.text.trim().toString())
                    match+="pass"
                Log.d("match",match)
                if (match=="passpass") {
                    val category=usercategory[pos]

                    if(category.toString().trim()!="Client") {
                        employeeintent.putExtra("username",adminUsernameFromDB[pos])
                        employeeintent.putExtra("cN", clientName)
                        employeeintent.putExtra("ciU", clientImageUrl)
                        employeeintent.putExtra("category", category)
                        startActivity(employeeintent)
                    }
                    else{
                        val builder = AlertDialog.Builder(this@LoginScreen)

                        // Set the alert dialog title
                        builder.setTitle("Are you in the right loginscreen?")

                        // Display a message on alert dialog
                        builder.setMessage("Sorry, you are authorized")


                        // Display a neutral button on alert dialog
                        builder.setNeutralButton("Go Back"){_,_ ->

                        }

                        // Finally, make the alert dialog using builder
                        val dialog: AlertDialog = builder.create()

                        // Display the alert dialog on app interface
                        dialog.show()
                    }

                }
            }

            }
            2->{login.setOnClickListener {
                val size=adminUsernameFromDB.size
                var i=0
                var match="fail"
                var pos=0
                var getP=""

                while(i<size)
                {

                    if (adminUsernameFromDB[i].trim()==username.text.trim().toString())
                    {
                        match="pass"
                        pos=i
                        getP=adminPasswordFromDB[i]
                        break
                    }
                    i++
                }
                if(getP==password.text.trim().toString())
                    match+="pass"
                Log.d("match",match)
                if (match=="passpass") {
                    val profilePic=profilePicFromDB[pos]
                    val fn=firstNameFromDB[pos]
                    val ln=lastNameFromDB[pos]
                    Log.d("profilePic",profilePic)
                    if(profilePic!=null) {
                        adminintent.putExtra("profilePic", profilePic)
                        adminintent.putExtra("fN", fn)
                        adminintent.putExtra("lN", ln)
                        startActivity(adminintent)
                    }
                    else{
                        val builder = AlertDialog.Builder(this@LoginScreen)

                        // Set the alert dialog title
                        builder.setTitle("Are you in the right loginscreen?")

                        // Display a message on alert dialog
                        builder.setMessage("Sorry, you are authorized")


                        // Display a neutral button on alert dialog
                        builder.setNeutralButton("Go Back"){_,_ ->

                        }

                        // Finally, make the alert dialog using builder
                        val dialog: AlertDialog = builder.create()

                        // Display the alert dialog on app interface
                        dialog.show()
                    }
                }
            }

            }
            else -> {
                Log.d("error", "ParseError")
            }
        }

    }
}