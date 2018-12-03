package dot10tech.com.dot10projects.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.Admin.ClientDetails.ClientDetailActivity
import dot10tech.com.dot10projects.R
import kotlinx.android.synthetic.main.activity_adminoptions.*
import kotlinx.android.synthetic.main.activity_adminoptions.view.*

import java.util.ArrayList

class OptionsViewerAdapter : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private val slideno: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instantiateItem()

    }



     fun instantiateItem() {

         setContentView(R.layout.activity_adminoptions)
        val animateOnSelect= AnimationUtils.loadAnimation(this, R.anim.animate_on_select)
        val clientName=intent.getStringExtra("clientName")
        val client=clientName
        clientNameinTitle.text=client
        clientNameinTitle.paintFlags=Paint.UNDERLINE_TEXT_FLAG

    }


}