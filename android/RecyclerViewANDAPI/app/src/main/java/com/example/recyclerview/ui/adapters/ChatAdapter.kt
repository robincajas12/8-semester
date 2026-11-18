package com.example.recyclerview.ui.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.R
import com.example.recyclerview.databinding.ChatMessageBinding

data class ChatMessage(val id: Long, val text: String, val fromBot: Boolean)

class ChatAdapter : ListAdapter<ChatMessage, ChatAdapter.ChatVH>(ChatDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatVH {
        val binding = ChatMessageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChatVH(binding)
    }

    override fun onBindViewHolder(holder: ChatVH, position: Int) {
        holder.render(getItem(position))
    }

    class ChatVH(val binding: ChatMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun render(message: ChatMessage) {
            binding.messageText.text = message.text
            val params = binding.messageText.layoutParams as FrameLayout.LayoutParams
            params.gravity = if (message.fromBot) Gravity.START else Gravity.END
            binding.messageText.layoutParams = params
            binding.messageText.setBackgroundResource(
                if (message.fromBot) R.drawable.bubble_bot else R.drawable.bubble_user
            )
        }
    }

    class ChatDiff : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
            oldItem == newItem
    }
}
