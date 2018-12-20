package dot10tech.com.dot10projects.FirebaseData

class ProjectsDataClass(
    var id:String,
    var clientName:ArrayList<String>,
    var startDate:ArrayList<String>,
    var deadLine:ArrayList<String>,
    var overallProgress:ArrayList<String>,
    var latestActivity:ArrayList<String>,
    var taskDeadline:ArrayList<String>,
    var taskStatus:ArrayList<String>
){

    constructor():this("", arrayListOf(),arrayListOf(),arrayListOf(),arrayListOf(),arrayListOf(),arrayListOf(),arrayListOf())

}