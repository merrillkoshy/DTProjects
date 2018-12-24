package dot10tech.com.dot10projects.Chats

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dot10tech.com.dot10projects.FirebaseData.ChatDataClass
import dot10tech.com.dot10projects.R
import kotlinx.android.synthetic.main.activity_chatbox.*
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChatTestFire:AppCompatActivity(){

lateinit var chatDetailsList:MutableList<ChatDataClass>
lateinit var addableChild:HashMap<String,Any>

    var commentpost=String()
    var dateandtime= String()
    var date=String()
    var time= String()
    var category= String()
    var username= String()
    private var adapter: ChatFireAdapter? = null

    /*for Fire*/

    var usernames = ArrayList<String>()
    var messages = ArrayList<String>()
    var dateandtimes = ArrayList<String>()
    var categories = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbox)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        loadtray()

        /*All inits*/

        usernames= arrayListOf()
        messages= arrayListOf()
        dateandtimes= arrayListOf()
        categories= arrayListOf()

        chatDetailsList= mutableListOf()
        addableChild= hashMapOf()

        category=intent.getStringExtra("category")
        username=intent.getStringExtra("username")

        recycler()
        activityButtons()
    }
    fun recycler(){
        adapter= ChatFireAdapter(this,chatDetailsList)
        cbox.adapter=adapter
        cbox.layoutManager=LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL, false)
        if(chatDetailsList.size>1)
            cbox.scrollToPosition(chatDetailsList.size-1)
    }

    fun loadtray(){
        val dB= FirebaseDatabase.getInstance().getReference()
        dB.child("chat").child("TITO").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children){
                    val chatdetail=dataSnapshot.getValue(ChatDataClass::class.java)
                    chatDetailsList.add(chatdetail!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("test", "Failed to read value.", error.toException())
            }
        })

    }

    fun activityButtons(){
        chatcom.setOnClickListener {

            commentpost=chatcomment.text.trim().toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalTime.now().toString()
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
                dateandtime = current.format(formatter)
            } else {
                var dateF = Date();
                val formatter = SimpleDateFormat("MMM dd yyyy/HH:mma")
                dateandtime = formatter.format(dateF)
                date=dateandtime.split("/")[0]
                time=dateandtime.split("/")[1]
            }

            chatDetailsList[0].username.add(username)
            chatDetailsList[0].message.add(commentpost)
            chatDetailsList[0].dateandtime.add(dateandtime)
            chatDetailsList[0].category.add(category)

            addableChild.put("username",chatDetailsList[0].username)
            addableChild.put("message",chatDetailsList[0].message)
            addableChild.put("dateandtime",chatDetailsList[0].dateandtime)
            addableChild.put("category",chatDetailsList[0].category)

            val database= FirebaseDatabase.getInstance().getReference("chat").child("TITO")

            database.updateChildren(addableChild)

            Log.d("Stat",username+" typed : "+commentpost+" on "+date+" at "+time)

            addMessage()

        }
    }

    fun addMessage(){
        loadtray()
        adapter= ChatFireAdapter(this,chatDetailsList)
        cbox.adapter=adapter
        cbox.layoutManager=LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL, false)
        if(chatDetailsList.size>1)
            cbox.scrollToPosition(chatDetailsList.size-1)
    }
}