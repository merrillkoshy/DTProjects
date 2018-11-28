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


class ImageAdapter(private val mContext: Context, private val imageList: ArrayList<String>,private val clientList: ArrayList<String>) : PagerAdapter() {

    private val TAG = this.javaClass.simpleName
    private val slideno: Int = 0



    override fun getCount(): Int {

        return imageList.size
    }

    override fun instantiateItem(viewGroup: ViewGroup, position: Int): Any {


        val rootView = LayoutInflater.from(mContext).inflate(R.layout.activity_adapter, null, false)
        val animateOnSelect=AnimationUtils.loadAnimation(mContext,R.anim.animate_on_select)
        val imageUrl = imageList[position]
        val client=clientList[position]

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
            pagerImageView.setOnClickListener{
                pagerImageView.startAnimation(animateOnSelect)
                val intent= Intent(mContext, ClientDetailActivity::class.java )
                intent.putExtra("cL",imageUrl)
                intent.putExtra("cN",client)
                mContext.startActivity(intent)
                (mContext as Activity).overridePendingTransition(R.anim.animation_leave, R.anim.animation_enter)
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