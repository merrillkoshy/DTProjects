package dot10tech.com.dot10projects.Chats

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import dot10tech.com.dot10projects.Client.ClientDataClass
import dot10tech.com.dot10projects.R
import kotlinx.android.synthetic.main.activity_chatbox.*

class Chatbox:AppCompatActivity(){

    private var recyclerView: RecyclerView? = null
    private var imageModelArrayList: ArrayList<Chatdata>? = null
    private var adapter: ChatAdapter? = null



    private val myImageList = arrayOf("https://dot10tech.com/mobileApp/assets/appicon.png")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbox)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        chatbuttons()



        recyclerView = findViewById(R.id.cbox) as RecyclerView

        imageModelArrayList = populateList()


        title=intent.getStringExtra("clientname")


        Log.d("hjhjh", imageModelArrayList!!.size.toString() + "")
        adapter = ChatAdapter(this, imageModelArrayList!!)
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        recyclerView!!.addOnItemTouchListener(
            RecyclerTouchListener(
                applicationContext,
                recyclerView!!,
                object : ClickListener {

                    override fun onClick(view: View, position: Int) {
                        Toast.makeText(this@Chatbox, imageModelArrayList!![position].getNames(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )

    }

    private fun chatbuttons() {
        chatcomment.setOnClickListener {
            val chatcomment=Intent(this,ChatComment::class.java)
            chatcomment.putExtra("username",intent.getStringExtra("username"))
            chatcomment.putExtra("dateandtime",intent.getStringExtra("dateandtime"))
            startActivity(chatcomment)
        }
        chatpic.setOnClickListener {  }
    }


    private fun populateList(): ArrayList<Chatdata> {

        val usernames=intent.getStringArrayListExtra("usernames")
        val commentposts=intent.getStringArrayListExtra("messages")
        val dateandtimes=intent.getStringArrayListExtra("dateandtimes")
        val categories=intent.getStringArrayListExtra("categories")

        val list = ArrayList<Chatdata>()


        var i=0
        while (i < commentposts.size) {
            val imageModel = Chatdata()
            imageModel.setNames(usernames[i])
            imageModel.setTs(dateandtimes[i])
            imageModel.setComments(commentposts[i])
            imageModel.set_affiliation_icon(myImageList[0])
            list.add(imageModel)
            i++
        }

        return list
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }

    internal class RecyclerTouchListener(context: Context, recyclerView: RecyclerView, private val clickListener: ClickListener?) : RecyclerView.OnItemTouchListener {
        override fun onInterceptTouchEvent(p0: RecyclerView, p1: MotionEvent): Boolean {
            val child = p0.findChildViewUnder(p1.x, p1.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(p1)) {
                clickListener.onClick(child, p0.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(p0: RecyclerView, p1: MotionEvent) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                    }
                }
            })
        }



        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
