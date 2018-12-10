package dot10tech.com.dot10projects.Employee

class EmployeeDetails {


    var name: ArrayList<String>? = null
    var assignment: ArrayList<String>? = null
    var affiliation: ArrayList<String>? = null

    constructor(name: ArrayList<String>, assignment: ArrayList<String>, affiliation: ArrayList<String>) {

        this.name = name
        this.assignment = assignment
        this.affiliation=affiliation
    }
}