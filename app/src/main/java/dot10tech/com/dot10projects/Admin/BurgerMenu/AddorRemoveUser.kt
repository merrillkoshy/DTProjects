package dot10tech.com.dot10projects.Admin.BurgerMenu

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.Networking.VolleySingleton
import dot10tech.com.dot10projects.R
import dot10tech.com.dot10projects.UI.EndPoints
import kotlinx.android.synthetic.main.activity_addorremoveuser.*
import org.json.JSONException
import org.json.JSONObject

class AddorRemoveUser:AppCompatActivity(),AdapterView.OnItemSelectedListener{

    var selected_cat= String()

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        selected_cat=resources.getStringArray(R.array.category)[p2]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addorremoveuser)
        title=""

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        Picasso.
            get().
            load("https:dot10tech.com/mobileApp/assets/appicon.png").
            fit().
            placeholder(R.drawable.progress_animation).
            into(mast)

        categoryspinner.onItemSelectedListener

        ArrayAdapter(this, R.layout.spinner_item, resources.getStringArray(R.array.category))

        Picasso.
            get().
            load("https:dot10tech.com/mobileApp/assets/add_user.png").
            fit().
            placeholder(R.drawable.progress_animation).
            into(adduserbtn)
    }

       private fun addActivity() {
       val getUserName = adduser?.text.toString()
       val getPassword = addpass?.text.toString()
       val getCat=selected_cat
       val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.ADD_NEW_USER_CREDS, Response.Listener<String>{
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
               params.put("username", getUserName)
               params.put("password", getPassword)
               params.put("category", getCat)

               return params
           }
       }



       VolleySingleton.instance?.addToRequestQueue(stringRequest)
   }

   override fun onSupportNavigateUp(): Boolean {
       onBackPressed()
       return true
   }




}