package dot10tech.com.dot10projects.Admin

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import dot10tech.com.dot10projects.R
import dot10tech.com.dot10projects.UI.ImageAdapter
import dot10tech.com.dot10projects.UI.IndefinitePagerIndicator

class OngoingProjects:AppCompatActivity(){
    private lateinit var pagerAdapter: ImageAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialiseWidgets()

    }


    fun initialiseWidgets(){
        title=""
        val clientNames=intent.getStringArrayListExtra("cN")
        val clientImageUrls=intent.getStringArrayListExtra("ciU")

        var i=0
        while (i<clientNames.size)
        {
            Log.d("cN",clientNames[i])
            i++
        }

        val username=intent.getStringExtra("fn")+" "+intent.getStringExtra("ln")
        setContentView(R.layout.activity_ongoingprojects)
        val viewPagr:ViewPager=findViewById(R.id.inside_pager)
        val indicator: IndefinitePagerIndicator =findViewById(R.id.pager_indicator)

        pagerAdapter= ImageAdapter(this, clientImageUrls, clientNames, "view", username, intent.getStringExtra("category"))
        viewPagr.adapter = pagerAdapter
        indicator.attachToViewPager(viewPagr)

    }
}