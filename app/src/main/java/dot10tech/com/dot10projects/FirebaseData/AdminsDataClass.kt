package dot10tech.com.dot10projects.FirebaseData

data class AdminsDataClass(
    var id:String,
    var username:ArrayList<String>,
    var password:ArrayList<String>,
    var profile_pic:ArrayList<String>,
    var first_name:ArrayList<String>,
    var last_name:ArrayList<String>

)
{
    constructor():this("", arrayListOf(),arrayListOf(),arrayListOf(),arrayListOf(),arrayListOf())


}