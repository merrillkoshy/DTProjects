package dot10tech.com.dot10projects.DataLayer

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import dot10tech.com.dot10projects.FirebaseData.ClientsDetailsData
import dot10tech.com.dot10projects.FirebaseData.UsersDataClass
import okhttp3.*
import java.io.IOException

private var target = String()
val clientName = ArrayList<String>()
val clientImageUrl = ArrayList<String>()

class ClientDetails{
    fun fetchJson() {
        val url = "https://dot10tech.com/mobileapp/scripts/clientDetailLoadApi.php"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
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

        val database= FirebaseDatabase.getInstance().getReference("clientDetailsData")
        val uid =database.push().key

        val allUsers: List<ClientsDetailsData> = mutableListOf(
            ClientsDetailsData(uid!!, clientName, clientImageUrl) )

        database.setValue(allUsers)


    }
}