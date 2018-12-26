package dot10tech.com.dot10projects.Chats

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.FirebaseData.ChatDataClass
import dot10tech.com.dot10projects.MainActivity
import dot10tech.com.dot10projects.R
import dot10tech.com.dot10projects.UI.EndPoints
import dot10tech.com.dot10projects.UI.UploadObject
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_chatbox.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChatTestFire:AppCompatActivity(){

lateinit var chatDetailsList:MutableList<ChatDataClass>
lateinit var addableChild:HashMap<String,Any>

    private val TAG = MainActivity::class.java.getSimpleName()
    private val SERVER_PATH = EndPoints.UPLOAD_URL

    var commentpost=String()
    private var commentedpic= String()
    var dateandtime= String()
    var date=String()
    var time= String()
    var clientname=String()
    var category= String()
    var username= String()
    private var adapter: ChatFireAdapter? = null

    /*camera vars*/

    var mCameraFileName: String? = null
    var image: Uri? = null
    private val CAMERA = 2
    private val READ_REQUEST_CODE= 300

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

        clientname=intent.getStringExtra("clientname")
        title=clientname



        /*All inits*/

        usernames= arrayListOf()
        messages= arrayListOf()
        dateandtimes= arrayListOf()
        categories= arrayListOf()

        loadinDataClass()
        val builder =  StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        chatDetailsList= mutableListOf()
        addableChild= hashMapOf()

        category=intent.getStringExtra("category")
        username=intent.getStringExtra("username")


        activityButtons()
    }
    fun recycler(){

        val affiliation="https://dot10tech.com/mobileApp/assets/appicon.png"

        adapter= ChatFireAdapter(this,chatDetailsList,affiliation)
        cbox.adapter=adapter
        cbox.layoutManager=LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL, false)
        if(chatDetailsList[0].username.size>1)
            cbox.scrollToPosition(chatDetailsList[0].username.size-1)
    }
    fun loadinDataClass(){




        var dateforuid=String()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalTime.now().toString()
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
            dateforuid = current.format(formatter)
        } else {
            var dateF = Date();
            val formatter = SimpleDateFormat("MMM dd yyyy")
            dateforuid = formatter.format(dateF)

        }
        val database= FirebaseDatabase.getInstance().getReference("chat").child(clientname)
        val uid =database.push().key
        val allUsers: List<ChatDataClass> = mutableListOf(
            ChatDataClass(uid!!, arrayListOf("startval"),arrayListOf("startval"),arrayListOf("startval/time"),arrayListOf("startval") ))

        database.child(0.toString()).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(!p0.exists()){
                    database.setValue(allUsers)
                }
            }
        })

            loadtray()

    }
    fun loadtray(){
        val dB= FirebaseDatabase.getInstance().getReference()
        dB.child("chat").child(clientname).child(0.toString()).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children){

                    val chatdetail=dataSnapshot.getValue(ChatDataClass::class.java)
                    chatDetailsList.add(chatdetail!!)
                }
                Log.d("chatdetailslist",""+chatDetailsList)
                recycler()
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
            chatcomment.text.clear()
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

            if(chatDetailsList[0].username[0]!="startval") {
                chatDetailsList[0].username.add(username)
                chatDetailsList[0].message.add(commentpost)
                chatDetailsList[0].dateandtime.add(dateandtime)
                chatDetailsList[0].category.add(category)

                addableChild.put("username", chatDetailsList[0].username)
                addableChild.put("message", chatDetailsList[0].message)
                addableChild.put("dateandtime", chatDetailsList[0].dateandtime)
                addableChild.put("category", chatDetailsList[0].category)

            }
            else if(chatDetailsList[0].username[0]=="startval"){
                chatDetailsList[0].username[0]=username
                chatDetailsList[0].message[0]=commentpost
                chatDetailsList[0].dateandtime[0]=dateandtime
                chatDetailsList[0].category[0]=category

                addableChild.put("username", chatDetailsList[0].username)
                addableChild.put("message", chatDetailsList[0].message)
                addableChild.put("dateandtime", chatDetailsList[0].dateandtime)
                addableChild.put("category", chatDetailsList[0].category)

            }
            val database= FirebaseDatabase.getInstance().getReference("chat").child(clientname).child(0.toString())

            database.updateChildren(addableChild)

            Log.d("Stat",username+" typed : "+commentpost+" on "+date+" at "+time)
            Log.d("Stat","size was : "+chatDetailsList[0].username.size)

        }


        /*camera button*/

        chatpic.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val dateCalc = Date()

            val df = SimpleDateFormat("mm-ss")

            val newPicFile = df.format(dateCalc) + ".jpg"
            val outPath = "/sdcard/"+ IMAGE_DIRECTORY +"/$newPicFile"
            val outFile = File(outPath)

            mCameraFileName = outFile.toString()
            val outuri = Uri.fromFile(outFile)

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

            intent.putExtra(MediaStore.EXTRA_OUTPUT,outuri)
            startActivityForResult(intent, CAMERA)


        }


    }
    /*camera result*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            image = data.getData()
        }
        val file = File(mCameraFileName)
        if (!file.exists()) {
            file.mkdir()}
        Picasso.get().load(file).placeholder(R.drawable.progress_animation).into(uploadtest)
        uploadtest.visibility= View.VISIBLE

        val compressedImageFile = Compressor(this).compressToFile(file);
        uploadFile(compressedImageFile)

        /*val thumbnail = data!!.extras!!.get("data") as Bitmap
        saveImage(thumbnail)*/
        Toast.makeText(this@ChatTestFire, "Image Saved!", Toast.LENGTH_SHORT).show()

        if(commentedpic!=""){
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
        }



    }
    private fun uploadFile(f: File) {
        if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            val mFile = RequestBody.create(MediaType.parse("image/*"), f);
            val fileToUpload = MultipartBody.Part.createFormData("file", f.getName(), mFile);
            val filename = RequestBody.create(MediaType.parse("text/plain"), f.getName());
            val retrofit =  Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
            val uploadImage = retrofit.create(ChatImageUploadInterface::class.java)

            val fileUpload = uploadImage.uploadFile(fileToUpload, filename)
            fileUpload.enqueue(object: retrofit2.Callback<UploadObject> {

                override fun onResponse(call: retrofit2.Call<UploadObject>, response: retrofit2.Response<UploadObject>) {
                    Toast.makeText(this@ChatTestFire, "Response " + response.raw().message(), Toast.LENGTH_LONG).show()
                    Toast.makeText(this@ChatTestFire, "Success " + response.body().success, Toast.LENGTH_LONG).show()

                    commentedpic= EndPoints.UPLOAD_URL+"/mobileApp/scripts/chats/chatImages/"+f.getName()

                    if(chatDetailsList[0].username[0]!="startval") {
                        chatDetailsList[0].username.add(username)
                        chatDetailsList[0].message.add(commentedpic)
                        chatDetailsList[0].dateandtime.add(dateandtime)
                        chatDetailsList[0].category.add(category)

                        addableChild.put("username", chatDetailsList[0].username)
                        addableChild.put("message", chatDetailsList[0].message)
                        addableChild.put("dateandtime", chatDetailsList[0].dateandtime)
                        addableChild.put("category", chatDetailsList[0].category)

                    }
                    else if(chatDetailsList[0].username[0]=="startval"){
                        chatDetailsList[0].username[0]=username
                        chatDetailsList[0].message[0]=commentedpic
                        chatDetailsList[0].dateandtime[0]=dateandtime
                        chatDetailsList[0].category[0]=category

                        addableChild.put("username", chatDetailsList[0].username)
                        addableChild.put("message", chatDetailsList[0].message)
                        addableChild.put("dateandtime", chatDetailsList[0].dateandtime)
                        addableChild.put("category", chatDetailsList[0].category)

                    }
                    val database= FirebaseDatabase.getInstance().getReference("chat").child(clientname).child(0.toString())

                    database.updateChildren(addableChild)

                }
                override fun onFailure(call: retrofit2.Call<UploadObject>, t:Throwable ) {
                    Log.d(TAG, "Error " + t.message);
                }
            });
        }else{
            EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
    companion object {
        private val IMAGE_DIRECTORY = "/DTApp"
    }

}