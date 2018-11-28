package dot10tech.com.dot10projects.Admin.ClientDetails

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.R
import kotlinx.android.synthetic.main.activity_clientdetail.*

class ClientDetailActivity:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseWidgets()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun initialiseWidgets(){
        setContentView(R.layout.activity_clientdetail)
        Picasso.get().
            load(intent.getStringExtra("cL"))
            .fit().
                placeholder(R.drawable.progress_animation).
                into(clientLogo)
        clientNameTitle.text="CLIENT"
        clientName.text=intent.getStringExtra("cN")
        val startDate="1/1/18"
        val endDate="1/1/18"
        dates.text="Start Date : "+startDate+"\n\n"+"Project Deadline : "+endDate
        progressBar.setProgress(50,true)
    }

}