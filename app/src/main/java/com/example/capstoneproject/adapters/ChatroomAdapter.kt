package com.example.capstoneproject.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.ui.chat.ChatFragment
import com.example.capstoneproject.ui.main.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.models.Chatroom
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatroomAdapter(options: FirestoreRecyclerOptions<Chatroom>, val context: Context) :
    FirestoreRecyclerAdapter<Chatroom, ChatroomAdapter.ChatroomViewHolder>(options) {
    class ChatroomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatroomName: TextView = itemView.findViewById(R.id.chatroom_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chatroom, parent, false)
        return ChatroomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatroomViewHolder, position: Int, model: Chatroom) {
        holder.chatroomName.text = model.name

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("chatroomId", model.id)

            val chatFragment = ChatFragment()
            chatFragment.arguments = bundle

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, chatFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}