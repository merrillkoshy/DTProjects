package dot10tech.com.dot10projects.DataLayer

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import dot10tech.com.dot10projects.FirebaseData.ProjectsDataClass
import dot10tech.com.dot10projects.FirebaseData.TeamAssignmentDataClass
import okhttp3.*
import java.io.IOException
import java.util.ArrayList

class TeamAssignmentDetails(){
    private var targetEmp = String()
    val staffName = ArrayList<String>()
    val staffAssignment = ArrayList<String>()
    val workingProject = ArrayList<String>()
    val affiliation = ArrayList<String>()

    fun fetchJsonEmp(){
        val url = "https://dot10tech.com/mobileapp/scripts/teamAssignmentView.php"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                //Slicing the response
                targetEmp = body.toString()
                slicer()

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }

    fun slicer(){
        val clientArray = targetEmp.replace("[", "").replace("]", "").replace("\"", "").split(",")

        var a = 0
        var b = 1
        var c = 2
        var d = 3


        Log.d("size", "" + clientArray.size)
        val arrsize = clientArray.size
        while (a < arrsize) {
            val un = clientArray[a]
            staffName.add(un)
            a += 4

        }
        while (b < arrsize) {
            val pw = clientArray[b]
            staffAssignment.add(pw)
            b += 4
        }
        while (c < arrsize) {
            val pw = clientArray[c]
            Log.d("workingproject",pw)
            workingProject.add(pw)
            c += 4
        }
        while (d < arrsize) {
            val pw = clientArray[d]
            affiliation.add(pw)
            d += 4
        }
        val database= FirebaseDatabase.getInstance().getReference("teamassignment")
        val uid =database.push().key!!

        val allTeamMembers: List<TeamAssignmentDataClass> = mutableListOf(
            TeamAssignmentDataClass(uid, staffName, staffAssignment, workingProject, affiliation ))

        database.setValue(allTeamMembers)
    }
}