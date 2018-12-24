package dot10tech.com.dot10projects.DataLayer

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import dot10tech.com.dot10projects.FirebaseData.ChatDataClass
import okhttp3.*
import java.io.IOException
import java.util.ArrayList


class ChatDetails{
    private var target = String()

    fun fetchJson() {
        val url = "https://dot10tech.com/mobileapp/scripts/chats/viewChat.php"

        val client = OkHttpClient()
        val request = okhttp3.Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body()?.string()

                //Slicing the response
                target = body.toString()
                slicer()

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })

    }

    fun slicer(){
        val clientArray = target.replace("[", "").replace("]", "").replace("\"", "").split(",")

        Log.d("commentpostsize",""+clientArray.size)
        Log.d("target",""+target[0])
        var i = 0
        var j = 1
        var k = 2
        var l = 3

        val usernames = ArrayList<String>()
        val messages = ArrayList<String>()
        val dateandtimes = ArrayList<String>()
        val categories = ArrayList<String>()

        Log.d("size", "" + clientArray.size)
        val size = clientArray.size
        while (i < size) {
            val un = clientArray[i]
            usernames.add(un)
            i += 4

        }
        while (j < size) {
            val pw = clientArray[j]
            messages.add(pw)
            j += 4
        }
        while (k < size) {
            val pw = clientArray[k]
            dateandtimes.add(pw)
            k += 4
        }
        while (l < size) {
            val pw = clientArray[l]
            categories.add(pw)
            l += 4
        }

        val database= FirebaseDatabase.getInstance().getReference("chat").child("TITO")
        val uid =database.push().key

        val allUsers: List<ChatDataClass> = mutableListOf(
            ChatDataClass(uid!!,usernames,messages,dateandtimes,categories) )

        database.setValue(allUsers)

    }
}