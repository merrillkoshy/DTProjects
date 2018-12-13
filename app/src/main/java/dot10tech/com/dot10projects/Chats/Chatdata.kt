package dot10tech.com.dot10projects.Chats

class Chatdata {

    var name: String? = null
    var comment: String? = null
    var srcurl: String? = null
    var times: String? = null

    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }
    fun setComments(comment: String) {
        this.comment = comment
    }

    fun getComments():String {
        return comment.toString()
    }

    fun setTs(times: String) {
        this.times = times
    }

    fun getTs():String {
        return times.toString()
    }
    fun get_affiliation_icon(): String {
        return srcurl.toString()
    }

    fun set_affiliation_icon(srcurl: String) {
        this.srcurl = srcurl
    }

}