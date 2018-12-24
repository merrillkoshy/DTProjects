package dot10tech.com.dot10projects.Chats

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.FirebaseData.ChatDataClass
import dot10tech.com.dot10projects.R

class ChatFireAdapter(
    ctx: Context,
    private val fireBaseDataOf:MutableList<ChatDataClass>
): RecyclerView.Adapter<ChatFireAdapter.MyViewHolder>(){
    private val inflater: LayoutInflater

    init {

        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view = inflater.inflate(R.layout.chatbox_item, p0, false)

        return MyViewHolder(view)
    }



    override fun getItemCount(): Int {
        return fireBaseDataOf.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        Log.d("field1",""+fireBaseDataOf)
        p0.postername.text=fireBaseDataOf[0].username[p1]
        val message=fireBaseDataOf[0].message[p1]
        if(!message.contains("https:")) {
            p0.message.visibility=View.VISIBLE
            p0.message.text = message
        }
        val time=fireBaseDataOf[0].dateandtime[p1].split("/")[1]
        p0.time.text=time
    }







    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var time: TextView
        var message: TextView
        var postername: TextView
        var iv: ImageView
        var cp: ImageView
        var badge: ImageView
        var closeBtn: Button
        var modal_image: ImageView
        var image_modal: CardView

        init {

            message = itemView.findViewById(R.id.messagecomment) as TextView
            postername = itemView.findViewById(R.id.postername) as TextView
            time = itemView.findViewById(R.id.timestamp) as TextView
            iv = itemView.findViewById(R.id.iv) as ImageView
            cp=itemView.findViewById(R.id.commentpic) as ImageView
            badge=itemView.findViewById(R.id.badge) as ImageView
            image_modal=itemView.findViewById(R.id.image_Modal) as CardView
            modal_image=itemView.findViewById(R.id.modal_image)as ImageView
            closeBtn=itemView.findViewById(R.id.close_btn)as Button


        }

    }
}