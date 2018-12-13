package dot10tech.com.dot10projects.Client

import android.util.Log

class ClientDataClass {

    var name: String? = null

    fun setNames(name: String) {
        this.name = name

    }

    fun getNames(): String {
        return name.toString()
    }


}