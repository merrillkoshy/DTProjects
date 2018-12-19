package dot10tech.com.dot10projects.DataLayer

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import dot10tech.com.dot10projects.FirebaseData.UsersDataClass
import okhttp3.*
import java.io.IOException

private var usercredsTarget= String()

class UserCredentials{

    fun fetchJson_UserCredTable() {
        val url = "https://dot10tech.com/mobileapp/scripts/teamscripts/userLoginactivity.php"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                //Slicing the response
                usercredsTarget = body.toString()
                slicer()
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }

    fun slicer(){
        val credsArray = usercredsTarget.replace("[", "").replace("]", "").replace("\"", "").split(",")
        val usernames = ArrayList<String>()
        val passwords = ArrayList<String>()
        val category = ArrayList<String>()


        var i = 0
        var j = 2
        var k = 1


        Log.d("size", "" + credsArray.size)
        val size = credsArray.size
        while (i < size) {
            val un = credsArray[i]
            usernames.add(un)
            i += 3

        }
        while (k < size) {
            val pw = credsArray[k]
            passwords.add(pw)
            k += 3
        }
        while (j < size) {
            val pw = credsArray[j]
            category.add(pw)
            j += 3
        }

        val database= FirebaseDatabase.getInstance().getReference("usercreds")
        val uid =database.push().key

        val allUsers: List<UsersDataClass> = mutableListOf(
            UsersDataClass(uid!!,usernames,passwords,category) )

        database.setValue(allUsers)
    }
}