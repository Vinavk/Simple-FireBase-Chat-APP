package com.example.firebasechatapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.ChatActivity
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.databinding.ListItemBinding

class DataAdapter(
    private val list: ArrayList<User>,
    private val context: Context
) : RecyclerView.Adapter<DataAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = list[position]
        holder.bind(user)

        if (position == list.size - 1) {
            holder.itemView.visibility = View.GONE
        } else {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java).apply {
                    putExtra("name", user.name)
                    putExtra("uid", user.uid)
                }
                context.startActivity(intent)
            }
        }


    }

    class MyViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.txtItem.text = user.name
        }
    }
}
