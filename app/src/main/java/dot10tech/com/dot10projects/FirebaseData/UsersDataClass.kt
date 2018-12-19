package dot10tech.com.dot10projects.FirebaseData

data class UsersDataClass(
    var id:String,
    var username:ArrayList<String>,
    var password:ArrayList<String>,
    var category:ArrayList<String>

)
{
    constructor():this("", arrayListOf(),arrayListOf(),arrayListOf())


}
