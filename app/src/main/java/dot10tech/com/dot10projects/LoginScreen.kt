package dot10tech.com.dot10projects

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.Admin.LoggedInAs
import dot10tech.com.dot10projects.Client.ClientActivity
import dot10tech.com.dot10projects.DataLayer.UserCredentials
import dot10tech.com.dot10projects.FirebaseData.AdminsDataClass
import dot10tech.com.dot10projects.FirebaseData.UsersDataClass
import kotlinx.android.synthetic.main.activity_loginscreen.*
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Array

class LoginScreen:AppCompatActivity(){

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

     lateinit var usercredList:MutableList<UsersDataClass>
    lateinit var testcredlist:HashMap<String,String>
     var admincredList=ArrayList<AdminsDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchProjectDetailsasJson()
        initialiseWidgets()
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
        usercredList= mutableListOf()
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



        val database=FirebaseDatabase.getInstance().getReference()
        database.child("usercreds").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children){
                    Log.d("test", "Value is: ${user.value}")
                    val usercred=dataSnapshot.child(0.toString()).getValue(UsersDataClass::class.java)
                    usercredList.add(usercred!!)
                }
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.child(0.toString()).getValue(UsersDataClass::class.java)
                Log.d("test2", "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("test", "Failed to read value.", error.toException())
            }
        })



        var i=0
        Log.d("usercredsize",":  "+usercredList.size)

        while(i<usercredList.size){
            Log.d("usercredlist",":    "+usercredList[i])
            Log.d("usercredlistinside",""+usercredList[i].username)
            i++
        }


        when(flag){
            0-> {
                login.setOnClickListener {

                    var i = 0
                    var match = "fail"
                    var pos = 0
                    var getP = ""
                    var name=""

                    while (i < usercredList.size) {


                        if (usercredList[i].username.toString().trim() == username.text.trim().toString()) {
                            match = "pass"
                            pos = i
                            name=usercredList[i].username.toString().trim()
                            getP = usercredList[i].password.toString().trim()
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



                var i=0
                var match="fail"
                var pos=0
                var getP=""

                while (i < usercredList.size)
                {

                    if (usercredList[i].username[i]==username.text.trim().toString())
                    {
                        match="pass"
                        pos=i
                        getP=usercredList[i].password[i]
                        break
                    }
                    i++
                }
                if(getP==password.text.trim().toString())
                    match+="pass"
                Log.d("match",match)
                if (match=="passpass") {
                    val category=usercredList[pos].password[pos]

                    if(category.toString().trim()!="Client") {
                        employeeintent.putExtra("username",usercredList[pos].username[pos])
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
                val database= FirebaseDatabase.getInstance().getReference("usercreds")
                database.addValueEventListener(object :ValueEventListener{


                    override fun onDataChange(p0: DataSnapshot) {

                        if(p0!!.exists()){
                            val admincreds=p0.getValue(AdminsDataClass::class.java)
                            admincredList.add(admincreds!!)
                        }
                    }
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })

                var i=0
                var match="fail"
                var pos=0
                var getP=""

                while(i<admincredList.size)
                {

                    if (admincredList[i].username[i]==username.text.trim().toString())
                    {
                        match="pass"
                        pos=i
                        getP=admincredList[i].password[i]
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