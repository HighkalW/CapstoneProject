package com.example.capstoneproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.models.Chat
import com.example.capstoneproject.util.UserUtil
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatAdapter(options: FirestoreRecyclerOptions<Chat>) :
    FirestoreRecyclerAdapter<Chat, ChatAdapter.ChatViewHolder>(options) {

    companion object {
        const val MSG_BY_SELF = 0
        const val MSG_BY_OTHER = 1
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatText: TextView = itemView.findViewById(R.id.chat_text)
        val chatAuthor: TextView = itemView.findViewById(R.id.chat_author)
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position).author.id == UserUtil.user?.id) {
            return MSG_BY_SELF
        } else {
            return MSG_BY_OTHER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        var view: View? = null

        view = if (viewType == MSG_BY_SELF) {
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_self, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_other, parent, false)
        }
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, model: Chat) {
        holder.chatText.text = model.text
        holder.chatAuthor.text = model.author.name
    }


}