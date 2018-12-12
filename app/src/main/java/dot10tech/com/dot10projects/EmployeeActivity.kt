package dot10tech.com.dot10projects

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import dot10tech.com.dot10projects.UI.ImageAdapter
import dot10tech.com.dot10projects.UI.IndefinitePagerIndicator

class EmployeeActivity:AppCompatActivity(){

    private lateinit var pagerAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseWidgets()
    }
    fun initialiseWidgets(){
        setContentView(R.layout.activity_employee)
        val viewPagr: ViewPager =findViewById(R.id.inside_pager)
        val indicator: IndefinitePagerIndicator =findViewById(R.id.pager_indicator)
        title=""
        val clientNames=intent.getStringArrayListExtra("cN")
        val clientImageUrls=intent.getStringArrayListExtra("ciU")
        val username=intent.getStringExtra("username")

        pagerAdapter= ImageAdapter(this,clientImageUrls,clientNames,"workon",username)
        viewPagr.adapter = pagerAdapter
        indicator.attachToViewPager(viewPagr)

    }
}