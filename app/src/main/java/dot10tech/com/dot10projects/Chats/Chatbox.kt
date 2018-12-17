package dot10tech.com.dot10projects.Chats

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest

import dot10tech.com.dot10projects.MainActivity
import dot10tech.com.dot10projects.Networking.VolleySingleton
import dot10tech.com.dot10projects.R
import dot10tech.com.dot10projects.UI.EndPoints
import dot10tech.com.dot10projects.UI.UploadObject
import kotlinx.android.synthetic.main.activity_chatbox.*

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class Chatbox:AppCompatActivity(){

    private val TAG = MainActivity::class.java.getSimpleName();
    private val READ_REQUEST_CODE= 300
    private val SERVER_PATH = EndPoints.UPLOAD_URL

    private var recyclerView: RecyclerView? = null
    private var imageModelArrayList: ArrayList<Chatdata>? = null
    private var updatedModelArrayList: ArrayList<Chatdata>? = null
    private var adapter: ChatAdapter? = null
    private var commentpost= String()
    private var dateandtime= String()
    private var username= String()
    private var commentedpic= String()

    private val CAMERA = 2

    private val myImageList = arrayOf("https://dot10tech.com/mobileApp/assets/appicon.png")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbox)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)



        chatcom.setOnClickListener {

            commentpost=chatcomment.text.trim().toString()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalTime.now().toString()
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
                dateandtime = current.format(formatter)
            } else {
                var date = Date();
                val formatter = SimpleDateFormat("MMM dd yyyy/HH:mma")
                dateandtime = formatter.format(date)

            }
            Log.d("comment", commentpost)
            Log.d("date and time", dateandtime)
            username=intent.getStringExtra("username")
            addMessage(1)

            }

            chatpic.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA)
            }



        recyclerView = findViewById(R.id.cbox) as RecyclerView

        imageModelArrayList = populateList()

        val appbartitle=intent.getStringExtra("clientname")
        title=appbartitle


       adapter = ChatAdapter(this, imageModelArrayList!!)
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        if(imageModelArrayList!!.size>1)
            recyclerView!!.scrollToPosition(imageModelArrayList!!.size-1)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val thumbnail = data!!.extras!!.get("data") as Bitmap
        saveImage(thumbnail)
        Toast.makeText(this@Chatbox, "Image Saved!", Toast.LENGTH_SHORT).show()

        if(commentedpic!=""){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalTime.now().toString()
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
                dateandtime = current.format(formatter)
            } else {
                var date = Date();
                val formatter = SimpleDateFormat("MMM dd yyyy/HH:mma")
                dateandtime = formatter.format(date)

            }
        }
        username=intent.getStringExtra("username")


    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())
            uploadFile(f)
            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }


        return ""
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
                    Toast.makeText(this@Chatbox, "Response " + response.raw().message(), Toast.LENGTH_LONG).show()
                    Toast.makeText(this@Chatbox, "Success " + response.body().success, Toast.LENGTH_LONG).show()

                    commentedpic=EndPoints.UPLOAD_URL+"/mobileApp/scripts/chats/chatImages/"+f.getName()
                    Log.d("commentedpic",commentedpic)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val current = LocalTime.now().toString()
                        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
                        dateandtime = current.format(formatter)
                    } else {
                        var date = Date();
                        val formatter = SimpleDateFormat("MMM dd yyyy/HH:mma")
                        dateandtime = formatter.format(date)

                    }

                    updatedModelArrayList=updateListCam()

                    adapter = ChatAdapter(this@Chatbox, updatedModelArrayList!!)
                    recyclerView!!.adapter = adapter

                    recyclerView!!.invalidate()
                    recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                    recyclerView!!.scrollToPosition(updatedModelArrayList!!.size-1)
                    addMessage(2)

                }
                override fun onFailure(call: retrofit2.Call<UploadObject>, t:Throwable ) {
                    Log.d(TAG, "Error " + t.message);
                }
            });
        }else{
            EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    fun addMessage(i: Int) {
        val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.ADDNEWCHAT, Response.Listener<String>{
                response ->
            try {
                val obj = JSONObject(response)
                Toast.makeText(applicationContext, obj.getString("output"), Toast.LENGTH_SHORT).show()
                chatcomment.text.clear()
                if(i==1) {
                    updatedModelArrayList = updateList()

                    adapter = ChatAdapter(this, updatedModelArrayList!!)
                    recyclerView!!.adapter = adapter

                    recyclerView!!.invalidate()
                    recyclerView!!.layoutManager =
                            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                    recyclerView!!.scrollToPosition(updatedModelArrayList!!.size - 1)
                }

            }catch (e: JSONException){
                e.printStackTrace()
            }

        }, object : Response.ErrorListener{
            override fun onErrorResponse(volleyError: VolleyError) {
                Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
            }
        }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params.put("message", commentpost)
                params.put("username", username)
                params.put("dateandtime", dateandtime)
                params.put("picurl", commentedpic)
                params.put("category", "Team")
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)

    }


    private fun updateList(): ArrayList<Chatdata> {

        val usernames=intent.getStringArrayListExtra("usernames")
        val commentposts=intent.getStringArrayListExtra("messages")
        val dateandtimes=intent.getStringArrayListExtra("dateandtimes")
        val categories=intent.getStringArrayListExtra("categories")

        val list = ArrayList<Chatdata>()


        var i=0


        usernames.add(username)
        commentposts.add(commentpost)
        dateandtimes.add(dateandtime.split("/","")[1])
        val size=commentposts.size

        while (i < size) {
            val imageModel = Chatdata()
            imageModel.setNames(usernames[i])
            imageModel.setTs(dateandtimes[i].split("\\/","")[1])
            imageModel.setComments(commentposts[i])
            imageModel.set_affiliation_icon(myImageList[0])
            list.add(imageModel)
            i++
        }
        return list
    }

    private fun updateListCam(): ArrayList<Chatdata> {

        val usernames=intent.getStringArrayListExtra("usernames")
        val commentposts=intent.getStringArrayListExtra("messages")
        val dateandtimes=intent.getStringArrayListExtra("dateandtimes")
        val categories=intent.getStringArrayListExtra("categories")

        val list = ArrayList<Chatdata>()


        var i=0


        usernames.add(username)
        commentposts.add(commentedpic)
        dateandtimes.add(dateandtime)
        val size=commentposts.size

        while (i < size) {
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

    companion object {
        private val IMAGE_DIRECTORY = "/DTApp"
    }
}
