package dot10tech.com.dot10projects.Employee

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
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
import dot10tech.com.dot10projects.Chats.Chatbox
import dot10tech.com.dot10projects.Client.ClientDataClass
import dot10tech.com.dot10projects.Networking.VolleySingleton
import dot10tech.com.dot10projects.UI.EndPoints
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class EmployeeDashboard:AppCompatActivity(), GestureDetector.OnGestureListener{

    private var target = String()
    private  var commentpost= String()
    private var dateandtime= String()
    private var username= String()
    var gDetector: GestureDetectorCompat? = null

    private var REQUEST_IMAGE_CAPTURE=1

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
            val REQUEST_TAKE_PHOTO = 1

            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File

                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.android.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    }
                }
            }

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
        }
    }

    var mCurrentPhotoPath= String()

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
/*            val imageBitmap = data!!.extras.get("data") as Bitmap
            mImageView.setImageBitmap(imageBitmap)*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalTime.now().toString()
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
                dateandtime = current.format(formatter)
            } else {
                var date = Date();
                val formatter = SimpleDateFormat("MMM dd yyyy/HH:mma")
                dateandtime = formatter.format(date)

            }

            Picasso.get().load(mCurrentPhotoPath)
            val show_date=dateandtime.split("/")[0]
            val show_time=dateandtime.split("/")[1]

            timestamp.text="Image posted at :"+"\n\n"+show_date+"\n"+show_time
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


}