package dot10tech.com.dot10projects.Chats

import android.support.v7.widget.RecyclerView
import android.util.Log
import dot10tech.com.dot10projects.R
import dot10tech.com.dot10projects.R.id.cbox
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient

import java.io.IOException

import java.util.*

var target= String()
var commentposts=ArrayList<String>()
var usernames=ArrayList<String>()
val messages = ArrayList<String>()
val dateandtimes = ArrayList<String>()
val categories = ArrayList<String>()
private val myImageList = arrayOf("https://dot10tech.com/mobileApp/assets/appicon.png")
private var imageModelArrayList: ArrayList<Chatdata>? = null

class ChatComment{

    fun fetchJson() {
        val url = "https://dot10tech.com/mobileapp/scripts/chats/viewChat.php"

        val client = OkHttpClient()
        val request = okhttp3.Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body()?.string()

                //Slicing the response
                target = body.toString()
                startslice()

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })

    }

    fun startslice(){
        val clientArray = target.replace("[", "").replace("]", "").replace("\"", "").split(",")

        Log.d("commentpostsize",""+clientArray.size)
        Log.d("target",""+target[0])
        var i = 0
        var j = 1
        var k = 2
        var l = 3




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
        commentposts=messages
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


    }


    fun recycleupdate(): ArrayList<Chatdata>? {
        fetchJson()
        imageModelArrayList = populateList()
        return imageModelArrayList
    }


    private fun populateList(): ArrayList<Chatdata> {



        val list = ArrayList<Chatdata>()


        var i=0
        while (i < commentposts.size) {
            val imageModel = Chatdata()
            imageModel.setNames(usernames[i])
            imageModel.setTs(dateandtimes[i])
            imageModel.setComments(commentposts[i])
            imageModel.set_affiliation_icon(myImageList[0])
            list.add(imageModel)
            i++
        }

        return list
    }


}