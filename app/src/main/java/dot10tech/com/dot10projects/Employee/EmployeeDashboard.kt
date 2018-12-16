package dot10tech.com.dot10projects.Employee

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.R
import kotlinx.android.synthetic.main.activity_employeedashboard.*
import android.widget.EditText
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.facebook.drawee.view.SimpleDraweeView
import dot10tech.com.dot10projects.Chats.ChatImageUploadInterface
import dot10tech.com.dot10projects.Chats.Chatbox
import dot10tech.com.dot10projects.Client.ClientDataClass
import dot10tech.com.dot10projects.MainActivity
import dot10tech.com.dot10projects.Networking.VolleySingleton
import dot10tech.com.dot10projects.UI.EndPoints
import dot10tech.com.dot10projects.UI.RealPathUtil
import dot10tech.com.dot10projects.UI.UploadImageInterface
import dot10tech.com.dot10projects.UI.UploadObject
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.*
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


class EmployeeDashboard:AppCompatActivity(), GestureDetector.OnGestureListener{

    private val TAG = MainActivity::class.java.getSimpleName();
    private var target = String()
    private var commentpost= String()
    private var dateandtime= String()
    private var username= String()
    private var theUri=Uri.EMPTY

    private val SERVER_PATH = EndPoints.UPLOAD_URL
    private var commentedpic= String()
    private val READ_REQUEST_CODE= 300
    private val pictureImagePath = ""

    var gDetector: GestureDetectorCompat? = null

    private val CAMERA = 2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        fetchJson()
        initialise()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        this.gDetector?.onTouchEvent(event)
        //calling the superclass implementation
        return super.onTouchEvent(event)
    }

    fun initialise(){

        setContentView(R.layout.activity_employeedashboard)
        this.gDetector = GestureDetectorCompat(this, this)

        val startDate=intent.getStringExtra("sd")

        val taskdeadline=intent.getStringExtra("td")
        val taskstatus=intent.getStringExtra("ts")

        masthead()
        progress()
        reporting()
    }

    fun masthead(){
        val imageUrl=intent.getStringExtra("ciU").replace("\\","")
        val clientName=intent.getStringExtra("cN")
        val deadlinedate=intent.getStringExtra("dl").replace("\\","")
        val latestactivity=intent.getStringExtra("la")
        username=intent.getStringExtra("username")


        Picasso.get().
            load(imageUrl).fit().placeholder(R.drawable.progress_animation).into(clientlogo)


        clientname.text=clientName
        deadline.text=deadlinedate
        latestactivityhead.text=latestactivity
        reportasname.text=username

    }

    fun progress() {

        val progressVal=intent.getStringExtra("op").toInt()
        opPercentage.text=""+progressVal+" %"
        if(progressVal==100){
            progressBar.progress=progressVal
            val progressDrawable = progressBar.progressDrawable.mutate()
            progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
            progressBar.progressDrawable = progressDrawable
        }
        else{
            when(progressVal<100){
                progressVal<50->{progressBar.progress=progressVal
                    val progressDrawable = progressBar.progressDrawable.mutate()
                    progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
                    progressBar.progressDrawable = progressDrawable}
                progressVal==50->{progressBar.progress=progressVal
                    val progressDrawable = progressBar.progressDrawable.mutate()
                    progressDrawable.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN)
                    progressBar.progressDrawable = progressDrawable}
                progressVal>50->{progressBar.progress=progressVal
                    val progressDrawable = progressBar.progressDrawable.mutate()
                    progressDrawable.setColorFilter(getColor(R.color.orange), android.graphics.PorterDuff.Mode.SRC_IN)
                    progressBar.progressDrawable = progressDrawable}
            }
        }



    }




    fun reporting(){

        sendpic.setOnClickListener {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA)
        }

        reportcomment.setOnClickListener {
            val builder = AlertDialog.Builder(this@EmployeeDashboard)

            // Set the alert dialog title
            builder.setTitle("Comment")
            builder.setIcon(R.drawable.ic_menu_send)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.post_a_comment, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.etComments)
            builder.setView(dialogLayout)


            // Display a message on alert dialog
            builder.setMessage("Post a comment")

            // Set a positive button and its click listener on alert dialog

            builder.setPositiveButton("SEND"){dialog, which ->


                    commentpost = editText.text.toString().trim()
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
                if(commentpost!=""){addMessage()}


                /*addMessage()*/

            }



            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel"){_,_ ->
                Toast.makeText(applicationContext,"You cancelled the dialog.",Toast.LENGTH_SHORT).show()
            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
            /*val show_date=dateandtime.split("/")[0]
            val show_time=dateandtime.split("/")[1]*/
        }
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val thumbnail = data!!.extras!!.get("data") as Bitmap
        mImageView!!.setImageBitmap(thumbnail)
        saveImage(thumbnail)
        Toast.makeText(this@EmployeeDashboard, "Image Saved!", Toast.LENGTH_SHORT).show()
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
            addMessage()
        }

    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
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
                    Toast.makeText(this@EmployeeDashboard, "Response " + response.raw().message(), Toast.LENGTH_LONG).show()
                    Toast.makeText(this@EmployeeDashboard, "Success " + response.body().success, Toast.LENGTH_LONG).show()

                    commentedpic=EndPoints.UPLOAD_URL+"/mobileApp/scripts/chats/chatImages/"+f.getName()
                }
                override fun onFailure(call: retrofit2.Call<UploadObject>, t:Throwable ) {
                    Log.d(TAG, "Error " + t.message);
                }
            });
        }else{
            EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }


    override fun onShowPress(e: MotionEvent?) {
        print(e)
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        if (e1!!.getX() - e2!!.getX() > 50) {
            val clientArray = target.replace("[", "").replace("]", "").replace("\"", "").split(",")

            Log.d("commentpostsize",""+clientArray.size)
            Log.d("target",""+target[0])
            var i = 0
            var j = 1
            var k = 2
            var l = 3

            val usernames = ArrayList<String>()
            val messages = ArrayList<String>()
            val dateandtimes = ArrayList<String>()
            val categories = ArrayList<String>()

            Log.d("size", "" + clientArray.size)
            val size = clientArray.size
            while (i < size) {
                val un = clientArray[i]
                usernames.add(un)
                i += 4

            }
            while (j < size) {
                val pw = clientArray[j]
                messages.add(pw)
                j += 4
            }
            while (k < size) {
                val pw = clientArray[k]
                dateandtimes.add(pw)
                k += 4
            }
            while (l < size) {
                val pw = clientArray[l]
                categories.add(pw)
                l += 4
            }

            val clientName=intent.getStringExtra("cN")
            val startchatbox=Intent(this, Chatbox::class.java)


            startchatbox.putExtra("clientname",clientName)
            startchatbox.putExtra("usernames",usernames)
            startchatbox.putExtra("messages",messages)
            startchatbox.putExtra("dateandtimes",dateandtimes)
            startchatbox.putExtra("categories",categories)
            startchatbox.putExtra("username",username)
            startchatbox.putExtra("message",commentpost)
            startchatbox.putExtra("dateandtime",dateandtime)


            startActivity(startchatbox)
            return true;
        }
        else {
            return true;
        }
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        print(e)
    }

    fun addMessage(){
            val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.ADDNEWCHAT, Response.Listener<String>{
                    response ->
                try {
                    val obj = JSONObject(response)
                    Toast.makeText(applicationContext, obj.getString("output"), Toast.LENGTH_SHORT).show()


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


    fun fetchJson() {
        val url = "https://dot10tech.com/mobileapp/scripts/chats/viewChat.php"

        val client = OkHttpClient()
        val request = okhttp3.Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body()?.string()

                //Slicing the response
                target = body.toString()

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    companion object {
        private val IMAGE_DIRECTORY = "/DTApp"
    }


}