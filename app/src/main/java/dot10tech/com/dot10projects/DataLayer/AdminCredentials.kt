package dot10tech.com.dot10projects.DataLayer

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import dot10tech.com.dot10projects.FirebaseData.AdminsDataClass
import dot10tech.com.dot10projects.FirebaseData.UsersDataClass
import okhttp3.*
import java.io.IOException

private var credsTarget = String()
class AdminCredentials{

    fun fetchJson_CredTable() {
        val url = "https://dot10tech.com/mobileapp/scripts/adminLoginActivity.php"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                //Slicing the response
                credsTarget = body.toString()
                slicer()

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }

    fun slicer(){
        val credsArray = credsTarget.replace("[", "").replace("]", "").replace("\"", "").split(",")
        val usernames = ArrayList<String>()
        val passwords = ArrayList<String>()
        val profilePic = ArrayList<String>()
        val firstName = ArrayList<String>()
        val lastName = ArrayList<String>()

        var i = 0
        var j = 2
        var k = 1
        var l = 3
        var m = 4

        Log.d("size", "" + credsArray.size)
        val size = credsArray.size
        while (i < size) {
            val un = credsArray[i]
            usernames.add(un)
            i += 5

        }
        while (k < size) {
            val pw = credsArray[k]
            passwords.add(pw)
            k += 5
        }
        while (j < size) {
            val pw = credsArray[j]
            profilePic.add(pw)
            j += 5
        }

        while (l < size) {
            val pw = credsArray[l]
            firstName.add(pw)
            l += 5
        }

        while (m < size) {
            val pw = credsArray[m]
            lastName.add(pw)
            m += 5
        }

        val database= FirebaseDatabase.getInstance().getReference("admincreds")
        val uid =database.push().key

        val allUsers: List<AdminsDataClass> = mutableListOf(
            AdminsDataClass(uid!!,usernames,passwords,profilePic,firstName,lastName) )

        database.setValue(allUsers)

    }
}