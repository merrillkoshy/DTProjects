package dot10tech.com.dot10projects.FirebaseData

data class ClientsDetailsData(
    var id:String,
    var clientName:ArrayList<String>,
    var clientImageUrl:ArrayList<String>

)
{
    constructor():this("", arrayListOf(),arrayListOf())


}
