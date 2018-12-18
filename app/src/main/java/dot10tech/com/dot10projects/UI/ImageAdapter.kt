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
import dot10tech.com.dot10projects.Admin.ClientDetails.ClientDetailActivity
import dot10tech.com.dot10projects.Admin.ClientDetails.EditClientDetail
import dot10tech.com.dot10projects.Client.ClientDataClass
import dot10tech.com.dot10projects.Employee.EmployeeDashboard
import dot10tech.com.dot10projects.Employee.EmployeeDetails
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

    private var target = String()
    val clientName = ArrayList<String>()
    val startDate = ArrayList<String>()
    val deadline = ArrayList<String>()
    val overallprogress = ArrayList<String>()
    val latestactivity = ArrayList<String>()
    val taskdeadline = ArrayList<String>()
    val taskstatus = ArrayList<String>()

    private var targetEmp = String()
    val staffName = ArrayList<String>()
    val staffAssignment = ArrayList<String>()
    val workingProject = ArrayList<String>()
    val affiliation = ArrayList<String>()

    var flag=0

    override fun getCount(): Int {

        return imageList.size
    }

    fun fetchJsonEmp(){
        val url = "https://dot10tech.com/mobileapp/scripts/teamAssignmentView.php"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                //Slicing the response
                targetEmp = body.toString()

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }

    fun fetchJson(){
        val url = "https://dot10tech.com/mobileapp/scripts/viewProjectDetails.php"

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

    override fun instantiateItem(viewGroup: ViewGroup, position: Int): Any {


        val rootView = LayoutInflater.from(mContext).inflate(R.layout.activity_adapter, null, false)
        val animateOnSelect=AnimationUtils.loadAnimation(mContext,R.anim.animate_on_select)
        val imageUrl = imageList[position]
        val client=clientList[position]
        fetchJson()
        fetchJsonEmp()

        if(imageUrl=="NULL")
        {
            val strin: String = "https://diplomatssummit.com/mobile/hp_assets/placeholder.jpg"
            val pagerImageView = rootView.findViewById<ImageView>(R.id.iv)
            Log.d("gPA_test", strin)
            Picasso.get().load(strin).centerCrop().fit().placeholder( R.drawable.progress_animation ).into(pagerImageView)
        }else {
            val pagerImageView = rootView.findViewById<ImageView>(R.id.iv)
            Log.d("gPA_test", imageUrl)
            Picasso.get().load(imageUrl).centerCrop().fit().placeholder( R.drawable.progress_animation ).into(pagerImageView)
            if(intention=="view"){
                pagerImageView.setOnClickListener{
                    val ProjectArray = target.
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
                        clientName.add(un)
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

                    var n=0
                    while(n<clientName.size)
                    {
                        Log.d(clientList[n],client)
                        if(clientList[n].toString().trim()==client.toString().trim())
                        {
                            flag=n
                            break
                        }
                        n++
                    }





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

                    var e=0
                    val indexlist=ArrayList<String>()

                    while(e<workingProject.size)
                    {
                        if(workingProject[e]==client)
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
                        staffnameforExport.add(staffName[indexlist[f].toInt()])
                        staffassignmentforExport.add(staffAssignment[indexlist[f].toInt()])
                        staffaffiliationforExport.add(affiliation[indexlist[f].toInt()])
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
                    intent.putExtra("cN",clientName[flag])
                    intent.putExtra("sd",startDate[flag])
                    intent.putExtra("dl",deadline[flag])
                    intent.putExtra("op",overallprogress[flag])
                    intent.putExtra("la",latestactivity[flag])
                    intent.putExtra("td",taskdeadline[flag])
                    intent.putExtra("ts",taskstatus[flag])
                    mContext.startActivity(intent)
                    (mContext as Activity).overridePendingTransition(R.anim.animation_leave, R.anim.animation_enter)
                }
            }
            else if(intention=="workon"){
                pagerImageView.setOnClickListener{

                    val ProjectArray = target.
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
                        clientName.add(un)
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

                    var n=0
                    while(n<clientName.size)
                    {   if(clientList[n]==client)
                        flag=n
                        n++
                    }

                    pagerImageView.startAnimation(animateOnSelect)
                    val intent= Intent(mContext, EmployeeDashboard::class.java )

                    intent.putExtra("ciU", imageUrl)
                    intent.putExtra("username",username)
                    intent.putExtra("cN",clientName[flag])
                    intent.putExtra("sd",startDate[flag])
                    intent.putExtra("dl",deadline[flag])
                    intent.putExtra("op",overallprogress[flag])
                    intent.putExtra("la",latestactivity[flag])
                    intent.putExtra("td",taskdeadline[flag])
                    intent.putExtra("ts",taskstatus[flag])

                    val dataclassclient= ClientDataClass()
                    dataclassclient.setNames(clientName[flag])

                    mContext.startActivity(intent)
                    (mContext as Activity).overridePendingTransition(R.anim.animation_leave, R.anim.animation_enter)
                }
            }
            else if(intention=="edit"){
                pagerImageView.setOnClickListener{

                    val ProjectArray = target.
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
                        clientName.add(un)
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

                    var n=0
                    while(n<clientName.size)
                    {   if(clientList[n]==client)
                        flag=n
                        n++
                    }

                    pagerImageView.startAnimation(animateOnSelect)
                    val intent= Intent(mContext, EditClientDetail::class.java )
                    intent.putExtra("cN",clientName[flag])
                    intent.putExtra("sd",startDate[flag])
                    intent.putExtra("dl",deadline[flag])
                    intent.putExtra("op",overallprogress[flag])
                    intent.putExtra("la",latestactivity[flag])
                    intent.putExtra("td",taskdeadline[flag])
                    intent.putExtra("ts",taskstatus[flag])
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