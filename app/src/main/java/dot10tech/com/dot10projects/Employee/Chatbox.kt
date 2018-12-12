package dot10tech.com.dot10projects.Employee

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import dot10tech.com.dot10projects.R
import kotlinx.android.synthetic.main.activity_chatbox.*

class Chatbox:AppCompatActivity(){

    private var recyclerView: RecyclerView? = null
    private var imageModelArrayList: ArrayList<Chatdata>? = null
    private var adapter: ChatAdapter? = null

    private var username=intent.getStringExtra("username")
    private var commentpost=intent.getStringExtra("message")
    private var dateandtime=intent.getStringExtra("dateandtime")

    private val myImageList = intArrayOf(R.drawable.ic_menu_send,R.drawable.ic_menu_send,R.drawable.ic_menu_send,R.drawable.ic_menu_send,R.drawable.ic_menu_send,R.drawable.ic_menu_send,R.drawable.ic_menu_send,R.drawable.ic_menu_send)
    private val myImageNameList = arrayOf("Benz", "Bike", "Car", "Carrera", "Ferrari", "Harly", "Lamborghini", "Silver")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbox)

        username_tv.text=username
        message.text=commentpost
        time.text=dateandtime

        recyclerView = findViewById(R.id.cbox) as RecyclerView

        imageModelArrayList = populateList()
        Log.d("hjhjh", imageModelArrayList!!.size.toString() + "")
        adapter = ChatAdapter(this, imageModelArrayList!!)
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        recyclerView!!.addOnItemTouchListener(RecyclerTouchListener(applicationContext, recyclerView!!, object : ClickListener {

            override fun onClick(view: View, position: Int) {
                Toast.makeText(this@Chatbox, imageModelArrayList!![position].getNames(), Toast.LENGTH_SHORT).show()
            }

            override fun onLongClick(view: View?, position: Int) {

            }
        }))

    }



    private fun populateList(): ArrayList<Chatdata> {

        val list = ArrayList<Chatdata>()

        for (i in 0..7) {
            val imageModel = Chatdata()
            imageModel.setNames(myImageNameList[i])
            imageModel.setImage_drawables(myImageList[i])
            list.add(imageModel)
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
}
