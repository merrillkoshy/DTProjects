package dot10tech.com.dot10projects.DataLayer

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import dot10tech.com.dot10projects.FirebaseData.ProjectsDataClass
import okhttp3.*
import java.io.IOException
import java.util.ArrayList

private var projecttarget= String()

val projectOfClientName = ArrayList<String>()
val startDate = ArrayList<String>()
val deadline = ArrayList<String>()
val overallprogress = ArrayList<String>()
val latestactivity = ArrayList<String>()
val taskdeadline = ArrayList<String>()
val taskstatus = ArrayList<String>()


class ProjectDetails{

    var uid=String()

    fun fetchProjectDetailsasJson(){
        val url = "https://dot10tech.com/mobileapp/scripts/viewProjectDetails.php"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                //Slicing the response
                projecttarget = body.toString()
                slicer()
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })

    }

    fun slicer(){
        val ProjectArray = projecttarget.
            replace("[", "").
            replace("]", "").
            replace("\"", "").split(",")

        var i = 0
        var k = 1
        var l=2
        var m=3
        var o=4
        var p=5
        var q=6

        Log.d("size", "" + ProjectArray.size)
        val size = ProjectArray.size
        while (i < size) {
            val un = ProjectArray[i]
            projectOfClientName.add(un)
            i += 7

        }
        while (k < size) {
            val pw = ProjectArray[k]
            startDate.add(pw)
            k += 7
        }
        while (l < size) {
            val pw = ProjectArray[l]
            deadline.add(pw)
            l += 7
        }
        while (m < size) {

            val pw = ProjectArray[m]
            overallprogress.add(pw)
            m += 7
        }
        while (o < size) {
            val pw = ProjectArray[o]
            latestactivity.add(pw)
            o += 7
        }
        while (p < size) {
            val pw = ProjectArray[p]
            taskdeadline.add(pw)
            p += 7
        }
        while (q < size) {
            val pw = ProjectArray[q]
            taskstatus.add(pw)
            q += 7
        }

        val database= FirebaseDatabase.getInstance().getReference("projectdetails")
        uid =database.push().key!!

        val allUsers: List<ProjectsDataClass> = mutableListOf(
            ProjectsDataClass(uid, projectOfClientName, startDate, deadline, overallprogress,
                latestactivity, taskdeadline, taskstatus) )

        database.setValue(allUsers)

    }

}