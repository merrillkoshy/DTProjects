package dot10tech.com.dot10projects.FirebaseData

data class ChatDataClass(
    var id:String,
    var username:ArrayList<String>,
    var message:ArrayList<String>,
    var dateandtime:ArrayList<String>,
    var category:ArrayList<String>
){

    constructor():this("", arrayListOf(),arrayListOf(),arrayListOf(),arrayListOf())
}