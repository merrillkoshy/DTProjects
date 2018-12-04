package dot10tech.com.dot10projects.Admin.ClientDetails

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import dot10tech.com.dot10projects.Networking.Service
import dot10tech.com.dot10projects.Networking.VolleySingleton
import dot10tech.com.dot10projects.R
import org.json.JSONException
import org.json.JSONObject
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_addprojectasadmin.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import java.io.File


class AddNewProjectasAdmin : AppCompatActivity() {

    var clientName: EditText? = null
    val PICK_IMAGE = 100
    var getClientLogo=String()
    internal var service: Service? = null
    val addUrl : String = "https://dot10tech.com/mobileApp/scripts/addClient.php"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addprojectasadmin)


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        service=Retrofit.Builder().baseUrl("https://dot10tech.com/mobileApp/uploadedImages/").
            client(client).build().create(Service::class.java)


        //finding views
        clientName = findViewById(R.id.activity)

        uploadimage.setOnClickListener { val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE) }

        val btnAaddActivity = findViewById<Button>(R.id.addactivity)
        btnAaddActivity.setOnClickListener {
            //we send user data to database
            addActivity()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            val selectedImage:Uri = data!!.data

            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null) ?: return;

            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            val filePath = cursor.getString(columnIndex);
            cursor.close()
            Log.d("filePath1",selectedImage.toString())


            val file = File(filePath)



            val reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            val body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
            val name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

//            Log.d("THIS", data.getData().getPath());

            getClientLogo=file.name

            val req = service?.postImage(body, name);
            req?.enqueue(object:Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    t?.printStackTrace();
                }

                override fun onResponse(call: Call<ResponseBody>?, response: retrofit2.Response<ResponseBody>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }
    }
    private fun addActivity() {
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
}