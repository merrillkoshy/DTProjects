package dot10tech.com.dot10projects.Admin

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.VISIBLE
import android.widget.Button;
import android.widget.Toast;
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.Admin.ClientDetails.AddNewProjectDetails
import dot10tech.com.dot10projects.MainActivity
import dot10tech.com.dot10projects.R
import dot10tech.com.dot10projects.UI.EndPoints
import dot10tech.com.dot10projects.UI.RealPathUtil
import dot10tech.com.dot10projects.UI.UploadImageInterface
import dot10tech.com.dot10projects.UI.UploadObject
import kotlinx.android.synthetic.main.activity_upload.*
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class AddNewProjectasAdmin : AppCompatActivity() , EasyPermissions.PermissionCallbacks{


    private val TAG = MainActivity::class.java.getSimpleName();
    private val REQUEST_GALLERY_CODE = 200;
    private val READ_REQUEST_CODE = 300;
    private val SERVER_PATH = EndPoints.UPLOAD_URL
    private val uri:Uri?=null

    private var clientName= String()
    private var clientLogo= String()

        override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
            title=""
        setContentView(R.layout.activity_upload)
        val selectUploadButton:Button = findViewById(R.id.select_image)

        client_name.setHintTextColor(resources.getColor(R.color.white))
            client_name.setTextColor(resources.getColor(R.color.selected_dot_color))

            Picasso.get().load("https:dot10tech.com/mobileApp/assets/appicon.png").
                fit().
                placeholder(R.drawable.progress_animation).
                into(mast)
            selectUploadButton.setOnClickListener {
                val openGalleryIntent=Intent(Intent.ACTION_PICK)
                openGalleryIntent.setType("image/*")
                startActivityForResult(openGalleryIntent,REQUEST_GALLERY_CODE)
            }
            nextpage.setOnClickListener {

                val intent=Intent(this,AddNewProjectDetails::class.java)
                intent.putExtra("clientName",clientName)
                intent.putExtra("clientLogo",clientLogo)
                startActivity(intent)

            }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK){
            val uri = data!!.getData();
            if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                val filePath = RealPathUtil.getRealPath(this,uri)
                val file =  File(filePath);
                Log.d(TAG, "Filename " + file.getName());
                //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                val mFile = RequestBody.create(MediaType.parse("image/*"), file);
                val fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                val filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                val retrofit =  Retrofit.Builder()
                    .baseUrl(SERVER_PATH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
                val uploadImage = retrofit.create(UploadImageInterface::class.java)

                val fileUpload = uploadImage.uploadFile(fileToUpload, filename)
                fileUpload.enqueue(object: Callback<UploadObject> {

                    override fun onResponse(call:Call<UploadObject>, response:Response<UploadObject> ) {
                        Toast.makeText(this@AddNewProjectasAdmin, "Response " + response.raw().message(), Toast.LENGTH_LONG).show()
                        Toast.makeText(this@AddNewProjectasAdmin, "Success " + response.body().success, Toast.LENGTH_LONG).show()
                        nextpage.visibility=VISIBLE
                        clientName=client_name.text.toString().trim()
                        clientLogo=EndPoints.UPLOAD_URL+"/mobileApp/uploadedImages/"+file.getName()
                    }
                    override fun onFailure(call:Call<UploadObject>, t:Throwable ) {
                        Log.d(TAG, "Error " + t.message);
                    }
                });
            }else{
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }





        override fun onPermissionsGranted(requestCode:Int, perms:List<String> ) {

        if(uri != null){
            val filePath = RealPathUtil.getRealPath(this,uri)
            val file =  File(filePath);
            val mFile = RequestBody.create(MediaType.parse("image/*"), file);
            val fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
            val filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            val retrofit = Retrofit.Builder()
                    .baseUrl(SERVER_PATH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            val uploadImage = retrofit.create(UploadImageInterface::class.java);
            val fileUpload = uploadImage.uploadFile(fileToUpload, filename);
            fileUpload.enqueue(object: Callback<UploadObject> {

          override fun onResponse(call:Call<UploadObject> , response:Response<UploadObject> ) {
                    Toast.makeText(this@AddNewProjectasAdmin, "Success " + response.message(), Toast.LENGTH_LONG).show();
                    Toast.makeText(this@AddNewProjectasAdmin, "Success " + response.body().toString(), Toast.LENGTH_LONG).show();

                }

        override fun onFailure(call:Call<UploadObject> , t:Throwable) {
                    Log.d(TAG, "Error " + t.message);
                }
            })
        }
    }

    override fun onPermissionsDenied(requestCode:Int , perms:List<String>) {
        Log.d(TAG, "Permission has been denied");
    }
}