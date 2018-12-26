package dot10tech.com.dot10projects.UI

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.R
import java.util.ArrayList
import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dot10tech.com.dot10projects.Admin.ClientDetails.ClientDetailActivity
import dot10tech.com.dot10projects.Admin.ClientDetails.EditClientDetail
import dot10tech.com.dot10projects.Employee.EmployeeDashboard
import dot10tech.com.dot10projects.Employee.EmployeeDetails
import dot10tech.com.dot10projects.FirebaseData.ProjectsDataClass
import dot10tech.com.dot10projects.FirebaseData.TeamAssignmentDataClass
import okhttp3.*
import java.io.IOException


class ImageAdapter(
    private val mContext: Context,
    private val imageList: ArrayList<String>,
    private val clientList: ArrayList<String>,
    private val intention: String,
    private val username: String,
    private val category: String
) : PagerAdapter() {

    private val TAG = this.javaClass.simpleName
    private val slideno: Int = 0


    val clientName = ArrayList<String>()
    val startDate = ArrayList<String>()
    val deadline = ArrayList<String>()
    val overallprogress = ArrayList<String>()
    val latestactivity = ArrayList<String>()
    val taskdeadline = ArrayList<String>()
    val taskstatus = ArrayList<String>()
    lateinit var projectdetailsList:MutableList<ProjectsDataClass>
    lateinit var teamAssignmentsList:MutableList<TeamAssignmentDataClass>


    val staffName = ArrayList<String>()
    val staffAssignment = ArrayList<String>()
    val workingProject = ArrayList<String>()
    val affiliation = ArrayList<String>()

    var flag=0

    override fun getCount(): Int {

        return imageList.size
    }




    override fun instantiateItem(viewGroup: ViewGroup, position: Int): Any {


        val rootView = LayoutInflater.from(mContext).inflate(R.layout.activity_adapter, null, false)
        val animateOnSelect=AnimationUtils.loadAnimation(mContext,R.anim.animate_on_select)
        val imageUrl = imageList[position]
        val client=clientList[position]


        val teamDB=FirebaseDatabase.getInstance().getReference()
        teamDB.child("teamassignment").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children){

                    Log.d("team", "Value is: ${user.value}")
                    val teamAssignment=dataSnapshot.child(0.toString()).getValue(TeamAssignmentDataClass::class.java)
                    teamAssignmentsList.add(teamAssignment!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("test", "Failed to read value.", error.toException())
            }
        })


        val database= FirebaseDatabase.getInstance().getReference()
        database.child("projectdetails").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children){
                    Log.d("project", "Value is: ${user.value}")
                    val project=dataSnapshot.child(0.toString()).getValue(ProjectsDataClass::class.java)
                    projectdetailsList.add(project!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("test", "Failed to read value.", error.toException())
            }
        })


        if(imageUrl=="NULL")
        {
            val strin: String = "https://diplomatssummit.com/mobile/hp_assets/placeholder.jpg"
            val pagerImageView = rootView.findViewById<ImageView>(R.id.iv)

            Picasso.get().load(strin).centerCrop().fit().placeholder( R.drawable.progress_animation ).into(pagerImageView)
        }else {
            projectdetailsList= mutableListOf()
            teamAssignmentsList= mutableListOf()
            val pagerImageView = rootView.findViewById<ImageView>(R.id.iv)

            Picasso.get().load(imageUrl).centerCrop().fit().placeholder( R.drawable.progress_animation ).into(pagerImageView)
            if(intention=="view"){

                pagerImageView.setOnClickListener{

                    var n=0
                    while(n<projectdetailsList[0].clientName.size)
                    {
                        Log.d(clientList[n],client)
                        if(clientList[n].toString().trim()==client.toString().trim())
                        {
                            flag=n
                            break
                        }
                        n++
                    }


                   val teamAssignment=teamAssignmentsList[0]

                    var e=0
                    val indexlist=ArrayList<String>()

                    while(e<teamAssignment.workingProject.size)
                    {
                        if(teamAssignment.workingProject[e]==client)
                        {
                            indexlist.add(e.toString())
                        }
                        e++
                    }

                    var f=0

                    val staffnameforExport=ArrayList<String>()
                    val staffassignmentforExport=ArrayList<String>()
                    val staffaffiliationforExport=ArrayList<String>()

                    while (f<indexlist.size)
                    {
                        staffnameforExport.add(teamAssignment.staffName[indexlist[f].toInt()])
                        staffassignmentforExport.add(teamAssignment.staffAssignment[indexlist[f].toInt()])
                        staffaffiliationforExport.add(teamAssignment.affiliation[indexlist[f].toInt()])
                        f++
                    }



                    pagerImageView.startAnimation(animateOnSelect)
                    val intent= Intent(mContext, ClientDetailActivity::class.java )

                    EmployeeDetails(staffnameforExport,staffassignmentforExport,staffaffiliationforExport)

                    intent.putStringArrayListExtra("staffName",staffnameforExport)
                    intent.putStringArrayListExtra("staffAssignment",staffassignmentforExport)
                    intent.putStringArrayListExtra("staffAffiliation",staffaffiliationforExport)


                    intent.putExtra("category",category)
                    intent.putExtra("username",username)
                    intent.putExtra("ciU", imageUrl)
                    intent.putExtra("cN",projectdetailsList[0].clientName[flag])
                    intent.putExtra("sd",projectdetailsList[0].startDate[flag])
                    intent.putExtra("dl",projectdetailsList[0].deadLine[flag])
                    intent.putExtra("op",projectdetailsList[0].overallProgress[flag])
                    intent.putExtra("la",projectdetailsList[0].latestActivity[flag])
                    intent.putExtra("td",projectdetailsList[0].taskDeadline[flag])
                    intent.putExtra("ts",projectdetailsList[0].taskStatus[flag])
                    mContext.startActivity(intent)
                    (mContext as Activity).overridePendingTransition(R.anim.animation_leave, R.anim.animation_enter)


                }
            }
            else if(intention=="workon"){
                pagerImageView.setOnClickListener{
                    val database= FirebaseDatabase.getInstance().getReference()
                    database.child("projectdetails").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (user in dataSnapshot.children){
                                Log.d("test", "Value is: ${user.value}")
                                val project=dataSnapshot.child(0.toString()).getValue(ProjectsDataClass::class.java)
                                projectdetailsList.add(project!!)

                                var n=0
                                while(n<projectdetailsList[0].clientName.size)
                                {
                                    Log.d(clientList[n],client)
                                    if(projectdetailsList[0].clientName[n].toString().trim()==client.toString().trim())
                                    {
                                        flag=n
                                        break
                                    }
                                    n++
                                }

                                pagerImageView.startAnimation(animateOnSelect)

                                val intent= Intent(mContext, EmployeeDashboard::class.java )

                                intent.putExtra("ciU", imageUrl)
                                intent.putExtra("username",username)
                                intent.putExtra("category",category)
                                intent.putExtra("cN",projectdetailsList[0].clientName[flag])
                                intent.putExtra("sd",projectdetailsList[0].startDate[flag])
                                intent.putExtra("dl",projectdetailsList[0].deadLine[flag])
                                intent.putExtra("op",projectdetailsList[0].overallProgress[flag])
                                intent.putExtra("la",projectdetailsList[0].latestActivity[flag])
                                intent.putExtra("td",projectdetailsList[0].taskDeadline[flag])
                                intent.putExtra("ts",projectdetailsList[0].taskStatus[flag])


                                mContext.startActivity(intent)
                                (mContext as Activity).overridePendingTransition(R.anim.animation_leave, R.anim.animation_enter)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            Log.w("test", "Failed to read value.", error.toException())
                        }
                    })


                }
            }
            else if(intention=="edit"){
                pagerImageView.setOnClickListener{

                    val database= FirebaseDatabase.getInstance().getReference()
                    database.child("projectdetails").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (user in dataSnapshot.children){
                                Log.d("test", "Value is: ${user.value}")
                                val project=dataSnapshot.child(0.toString()).getValue(ProjectsDataClass::class.java)
                                projectdetailsList.add(project!!)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            Log.w("test", "Failed to read value.", error.toException())
                        }
                    })

                    val teamDB=FirebaseDatabase.getInstance().getReference()
                    teamDB.child("teamassignment").addValueEventListener(object:ValueEventListener{
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (user in dataSnapshot.children){

                                Log.d("team", "Value is: ${user.value}")
                                val teamAssignment=dataSnapshot.child(0.toString()).getValue(TeamAssignmentDataClass::class.java)
                                teamAssignmentsList.add(teamAssignment!!)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            Log.w("test", "Failed to read value.", error.toException())
                        }
                    })



                    var n=0
                    while(n<projectdetailsList[0].clientName.size)
                    {
                        Log.d(clientList[n],client)
                        if(projectdetailsList[0].clientName[n].toString().trim()==client.toString().trim())
                        {
                            flag=n
                            break
                        }
                        n++
                    }

                    val teamAssignment=teamAssignmentsList[0]

                    var e=0
                    val indexlist=ArrayList<String>()

                    while(e<teamAssignment.workingProject.size)
                    {
                        if(teamAssignment.workingProject[e]==client)
                        {
                            indexlist.add(e.toString())
                        }
                        e++
                    }

                    var f=0

                    val staffnameforExport=ArrayList<String>()
                    val staffassignmentforExport=ArrayList<String>()
                    val staffaffiliationforExport=ArrayList<String>()

                    while (f<indexlist.size)
                    {
                        staffnameforExport.add(teamAssignment.staffName[indexlist[f].toInt()])
                        staffassignmentforExport.add(teamAssignment.staffAssignment[indexlist[f].toInt()])
                        staffaffiliationforExport.add(teamAssignment.affiliation[indexlist[f].toInt()])
                        f++
                    }


                    pagerImageView.startAnimation(animateOnSelect)
                    val intent= Intent(mContext, EditClientDetail::class.java )

                    intent.putStringArrayListExtra("staffName",staffnameforExport)
                    intent.putStringArrayListExtra("staffAssignment",staffassignmentforExport)
                    intent.putStringArrayListExtra("staffAffiliation",staffaffiliationforExport)

                    intent.putExtra("cN",projectdetailsList[0].clientName[flag])
                    intent.putExtra("sd",projectdetailsList[0].startDate[flag])
                    intent.putExtra("dl",projectdetailsList[0].deadLine[flag])
                    intent.putExtra("op",projectdetailsList[0].overallProgress[flag])
                    intent.putExtra("la",projectdetailsList[0].latestActivity[flag])
                    intent.putExtra("td",projectdetailsList[0].taskDeadline[flag])
                    intent.putExtra("ts",projectdetailsList[0].taskStatus[flag])
                    intent.putExtra("flag",flag)
                    mContext.startActivity(intent)
                    (mContext as Activity).overridePendingTransition(R.anim.animation_leave, R.anim.animation_enter)
                }
            }

        }
        viewGroup.addView(rootView)


        return rootView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}