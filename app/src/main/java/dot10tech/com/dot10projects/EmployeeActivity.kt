package dot10tech.com.dot10projects

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dot10tech.com.dot10projects.FirebaseData.ClientsDetailsDataClass
import dot10tech.com.dot10projects.UI.ImageAdapter
import dot10tech.com.dot10projects.UI.IndefinitePagerIndicator

class EmployeeActivity:AppCompatActivity(){

    private lateinit var pagerAdapter: ImageAdapter
    lateinit var clientDetails:MutableList<ClientsDetailsDataClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collect()


    }

    fun collect(){

        clientDetails = mutableListOf()
        val database= FirebaseDatabase.getInstance().getReference()
        database.child("clientDetailsData").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children){
                    Log.d("test", "Value is: ${user.value}")
                    val clientdetail=dataSnapshot.child(0.toString()).getValue(ClientsDetailsDataClass::class.java)
                    clientDetails.add(clientdetail!!)
                }
                initialiseWidgets()

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("test", "Failed to read value.", error.toException())
            }
        })
    }

    fun initialiseWidgets(){

        setContentView(R.layout.activity_employee)
        val viewPagr: ViewPager =findViewById(R.id.inside_pager)
        val indicator: IndefinitePagerIndicator =findViewById(R.id.pager_indicator)
        title=""

        val size=clientDetails.size
        var i=0
        while(i<size){
            Log.d("show","indice "+i+" :"+clientDetails[i])
            i++
        }

        val clientNames=clientDetails[0].clientName
        val clientImageUrls=clientDetails[0].clientImageUrl
        val username=intent.getStringExtra("username")
        val category=intent.getStringExtra("category")
        pagerAdapter= ImageAdapter(this,clientImageUrls,clientNames,"workon",username,category)
        viewPagr.adapter = pagerAdapter
        indicator.attachToViewPager(viewPagr)

    }
}