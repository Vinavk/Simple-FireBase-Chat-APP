package com.example.firebasechatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.data.Message
import com.example.firebasechatapp.databinding.ReceiveMessageItemBinding
import com.example.firebasechatapp.databinding.SendMessageItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class ChatAdapter(var list: ArrayList<Message>, var dbref: FirebaseAuth) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var sentbinding: SendMessageItemBinding
    private lateinit var receiveitemBinding: ReceiveMessageItemBinding

    var SEND_HOLDER = 2
    var RECEIVE_hOLDER = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            receiveitemBinding =
                ReceiveMessageItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ReceiveViewHolder(receiveitemBinding.root)
        } else {
            sentbinding =
                SendMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SentViewHolder(sentbinding.root)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        var currentmessage = list[position]
        if (dbref.currentUser!!.uid.equals(currentmessage.senderId)) {
            return SEND_HOLDER
        } else {
            return RECEIVE_hOLDER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            holder as SentViewHolder
            holder.sentmessage.text = message.message
        } else {
            holder as ReceiveViewHolder
            holder.receivemessage.text = message.message
        }
    }

    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sentmessage = sentbinding.sentMessageText
    }

    inner class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var receivemessage = receiveitemBinding.receivedMessageText
    }
}