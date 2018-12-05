package dot10tech.com.dot10projects.Admin.ClientDetails

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import dot10tech.com.dot10projects.Admin.AddNewProjectasAdmin
import dot10tech.com.dot10projects.Admin.EditProjectasAdmin
import dot10tech.com.dot10projects.Admin.OngoingProjects
import okhttp3.*
import java.io.IOException

class ClientDetailsData():AppCompatActivity() {

    private var target = String()
    val clientName = ArrayList<String>()
    val clientImageUrl = ArrayList<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val option=intent.getIntExtra("option",0)
        fetchJson()
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

        when(option){
                1->{val intent= Intent(this, AddNewProjectasAdmin::class.java)
                        intent.putExtra("cN", clientName)
                        intent.putExtra("ciU", clientImageUrl)
                        startActivity(intent)
                    }
                2->{val intent= Intent(this, EditProjectasAdmin::class.java)
                    intent.putExtra("cN", clientName)
                    intent.putExtra("ciU", clientImageUrl)
                    startActivity(intent)
                }
                3->{val intent= Intent(this, OngoingProjects::class.java)
                    intent.putExtra("cN", clientName)
                    intent.putExtra("ciU", clientImageUrl)
                    startActivity(intent)
                }
                else->{
                    Toast.makeText(this,"Parse error",Toast.LENGTH_LONG).show()
                    finish()
            }
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



}

