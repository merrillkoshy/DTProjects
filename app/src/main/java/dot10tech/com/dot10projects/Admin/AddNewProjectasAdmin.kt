package dot10tech.com.dot10projects.Admin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import dot10tech.com.dot10projects.R
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.provider.MediaStore
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.UI.UploadImage

import kotlinx.android.synthetic.main.activity_addprojectasadmin.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class AddNewProjectasAdmin : AppCompatActivity() {

    var clientName: EditText? = null
    private val GALLERY = 1
/*    private val CAMERA = 2
    var getClientLogo=String()
    internal var service: Service? = null
    val addUrl : String = "https://dot10tech.com/mobileApp/scripts/addClient.php"*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        choosePhotoFromGallery()
/*
        //finding views
        clientName = findViewById(R.id.activity)

        uploadimage.setOnClickListener {  }

        val btnAaddActivity = findViewById<Button>(R.id.addactivity)
        btnAaddActivity.setOnClickListener {
            //we send user data to database
            *//*addActivity()*//*
        }*/

    }

/*    private fun showPictureDialog() {


        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Upload Your Photo")
        val pictureDialogItems = arrayOf("Select photo from gallery")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
            }
        }
        pictureDialog.show()
    }*/

    fun choosePhotoFromGallery() {

        val intent=Intent(this, UploadImage::class.java)
        startActivity(intent)
/*    val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    startActivityForResult(galleryIntent, GALLERY)*/
    }


    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data.data
                try
                {
                    /*val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    Log.d("ShowPath",path)*/
                    Toast.makeText(this@AddNewProjectasAdmin, "Image Saved!", Toast.LENGTH_SHORT).show()
                    Picasso.get().load(contentURI).fit().placeholder(R.drawable.progress_animation).into(iv)


                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@AddNewProjectasAdmin, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }




    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
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

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

 /*   private fun addActivity() {
        val getClientName = activity?.text.toString()
        val stringRequest = object : StringRequest(Request.Method.POST,addUrl, Response.Listener<String>{
                response ->
            try {
                val obj = JSONObject(response)
                Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()
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
                params.put("ClientName", getClientName)
                params.put("ClientLogo", getClientLogo)
                return params
            }
        }



        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }*/

    companion object {
        private val IMAGE_DIRECTORY = "/gallerytest"
    }
}