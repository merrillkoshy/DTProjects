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
import dot10tech.com.dot10projects.SplashActivity
import okhttp3.*
import java.io.IOException


class ImageAdapter(
    private val mContext: Context,
    private val imageList: ArrayList<String>,
    private val clientList: ArrayList<String>,
    private val intention: String
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
    var flag=0

    override fun getCount(): Int {

        return imageList.size
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
                        i += 2

                    }
                    while (k < size) {
                        val pw = ProjectArray[k]
                        startDate.add(pw)
                        k += 2
                    }
                    while (l < size) {
                        val pw = ProjectArray[l]
                        deadline.add(pw)
                        l += 2
                    }
                    while (m < size) {
                        val pw = ProjectArray[m]
                        overallprogress.add(pw)
                        m += 2
                    }
                    while (o < size) {
                        val pw = ProjectArray[o]
                        latestactivity.add(pw)
                        o += 2
                    }
                    while (p < size) {
                        val pw = ProjectArray[p]
                        taskdeadline.add(pw)
                        p += 2
                    }
                    while (q < size) {
                        val pw = ProjectArray[q]
                        taskstatus.add(pw)
                        q += 2
                    }

                    var n=0
                    while(n<clientName.size)
                    {   if(clientName[n]==client)
                        flag=n
                        n++
                    }

                    pagerImageView.startAnimation(animateOnSelect)
                    val intent= Intent(mContext, ClientDetailActivity::class.java )
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
                        i += 2

                    }
                    while (k < size) {
                        val pw = ProjectArray[k]
                        startDate.add(pw)
                        k += 2
                    }
                    while (l < size) {
                        val pw = ProjectArray[l]
                        deadline.add(pw)
                        l += 2
                    }
                    while (m < size) {
                        val pw = ProjectArray[m]
                        overallprogress.add(pw)
                        m += 2
                    }
                    while (o < size) {
                        val pw = ProjectArray[o]
                        latestactivity.add(pw)
                        o += 2
                    }
                    while (p < size) {
                        val pw = ProjectArray[p]
                        taskdeadline.add(pw)
                        p += 2
                    }
                    while (q < size) {
                        val pw = ProjectArray[q]
                        taskstatus.add(pw)
                        q += 2
                    }

                    var n=0
                    while(n<clientName.size)
                    {   if(clientName[n]==client)
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