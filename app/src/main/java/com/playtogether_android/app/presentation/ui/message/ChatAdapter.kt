package com.playtogether_android.app.presentation.ui.message

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.playtogether_android.app.databinding.ItemMyChatBinding
import com.playtogether_android.app.databinding.ItemOtherChatBinding
import com.playtogether_android.domain.model.message.ChatData

class ChatAdapter : RecyclerView.Adapter<ChatViewHolder<*>>() {

    val chatList = mutableListOf<ChatData>()

    class MyChatViewHolder(private val binding: ItemMyChatBinding) :
        ChatViewHolder<ChatData>(binding.root) {
        override fun bind(item: ChatData) {
            binding.data = item
        }
    }

    class OtherChatViewHolder(private val binding: ItemOtherChatBinding) :
        ChatViewHolder<ChatData>(binding.root) {
        override fun bind(item: ChatData) {
            binding.data = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingMyChat = ItemMyChatBinding.inflate(layoutInflater, parent, false)
        val bindingOtherChat = ItemOtherChatBinding.inflate(layoutInflater, parent, false)
        Log.d("checkViewType", "${viewType}")
        return when (viewType) {
            ChatData.TYPE_MY_MESSAGE -> MyChatViewHolder(bindingMyChat)
            ChatData.TYPE_FRIEND_MESSAGE -> OtherChatViewHolder(bindingOtherChat)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder<*>, position: Int) {
        val item = chatList[position]
        when (holder) {
            is MyChatViewHolder -> holder.bind(item)
            is OtherChatViewHolder -> holder.bind(item)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount() = chatList.size

    override fun getItemViewType(position: Int): Int {
        return chatList[position].getViewType()
    }

    fun addChat(chat: ChatData) {
        Log.d("asdf", "addChat 진입 ${chat.content}")
        chatList.add(chat)
        notifyItemInserted(chatList.size)
    }
}