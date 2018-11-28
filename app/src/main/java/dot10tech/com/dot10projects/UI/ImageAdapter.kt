package dot10tech.com.dot10projects.UI

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.R
import java.util.ArrayList


class ImageAdapter(private val mContext: Context, private val imageList: ArrayList<String>) : PagerAdapter() {

    private val TAG = this.javaClass.simpleName
    private val slideno: Int = 0

    override fun getCount(): Int {

        var count = 0

        if (imageList != null)
            count = 1

        return count
    }

    override fun instantiateItem(viewGroup: ViewGroup, position: Int): Any {

        val rootView = LayoutInflater.from(mContext).inflate(R.layout.activity_adapter, null, false)

        val imageUrl = imageList[position]

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