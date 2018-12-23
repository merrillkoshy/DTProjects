package dot10tech.com.dot10projects.FirebaseData

data class TeamAssignmentDataClass(
    var id:String,
    var staffName:ArrayList<String>,
    var staffAssignment:ArrayList<String>,
    var workingProject:ArrayList<String>,
    var affiliation:ArrayList<String>

)
{
    constructor():this("", arrayListOf(),arrayListOf(),arrayListOf(), arrayListOf())


}
