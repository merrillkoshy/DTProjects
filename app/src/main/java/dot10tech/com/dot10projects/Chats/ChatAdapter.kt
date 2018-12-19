package dot10tech.com.dot10projects.Chats

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.R

class ChatAdapter(
    ctx: Context,
    private val imageModelArrayList: ArrayList<Chatdata>,
    private val category: String
) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater


    init {

        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = inflater.inflate(R.layout.chatbox_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Picasso.get().load(
            imageModelArrayList[position].get_affiliation_icon()
        ).placeholder(R.drawable.progress_animation).into(holder.iv)

        if (imageModelArrayList[position].get_cat() == "Admin") {
            holder.badge.visibility = View.VISIBLE
            Picasso.get().load(R.drawable.badge_admin).into(holder.badge)
        } else if (imageModelArrayList[position].get_cat() == "Team") {
            holder.badge.visibility = View.GONE
        } else if (imageModelArrayList[position].get_cat() == "Client") {
            holder.badge.visibility = View.GONE
            Picasso.get().load(category).placeholder(R.drawable.progress_animation).into(holder.iv)
        }
        holder.postername.setText(imageModelArrayList[position].getNames())


        if (!imageModelArrayList[position].getComments().contains("https:")) {
            holder.message.visibility = View.VISIBLE
            holder.message.setText(imageModelArrayList[position].getComments())
            holder.cp.visibility = View.GONE
        }

        var ts=imageModelArrayList[position].getTs()
        Log.d("ts",ts)
        holder.time.setText(imageModelArrayList[position].getTs())


            if (imageModelArrayList[position].getComments().contains("https:")) {
                holder.message.visibility = View.GONE
                holder.cp.visibility = View.VISIBLE
                Picasso.get().load(
                    imageModelArrayList[position].getComments().replace("\\", "")
                ).placeholder(R.drawable.progress_animation).into(holder.cp)
                holder.cp.setOnClickListener {
                    holder.image_modal.visibility = View.VISIBLE
                    Picasso.get().load(imageModelArrayList[position].getComments().replace("\\", ""))
                        .placeholder(R.drawable.progress_animation).into(holder.modal_image)
                }

                holder.closeBtn.setOnClickListener {
                    holder.image_modal.visibility = View.GONE
                }
            }

    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var time: TextView
        var message: TextView
        var postername: TextView
        var iv: ImageView
        var cp:ImageView
        var badge:ImageView
        var closeBtn:Button
        var modal_image:ImageView
        var image_modal:CardView

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