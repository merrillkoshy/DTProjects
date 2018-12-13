package dot10tech.com.dot10projects.Chats

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.R

class ChatAdapter(ctx: Context, private val imageModelArrayList: ArrayList<Chatdata>) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater


    init {

        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = inflater.inflate(R.layout.chatbox_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Picasso.get().load(imageModelArrayList[position].
            get_affiliation_icon()).
            placeholder(R.drawable.progress_animation).
            into(holder.iv)

        holder.postername.setText(imageModelArrayList[position].getNames())
        holder.message.setText(imageModelArrayList[position].getComments())
        holder.time.setText(imageModelArrayList[position].getTs())
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var time: TextView
        var message: TextView
        var postername: TextView
        var iv: ImageView

        init {

            message = itemView.findViewById(R.id.messagecomment) as TextView
            postername = itemView.findViewById(R.id.postername) as TextView
            time = itemView.findViewById(R.id.timestamp) as TextView
            iv = itemView.findViewById(R.id.iv) as ImageView
        }

    }
}