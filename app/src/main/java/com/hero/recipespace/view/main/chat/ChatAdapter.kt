package com.hero.recipespace.view.main.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.databinding.ItemMessageLeftBinding
import com.hero.recipespace.databinding.ItemMessageRightBinding
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.view.BaseAdapter

class ChatAdapter() : BaseAdapter<RecyclerView.ViewHolder, MessageEntity>() {

    private val messageList = mutableListOf<MessageEntity>()

    companion object {
        private const val LEFT_TYPE = 0
        private const val RIGHT_TYPE = 1
    }

    private var myUserKey: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == RIGHT_TYPE) {
            val binding = ItemMessageRightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            RightMessageViewHolder(binding)
        } else {
            val binding = ItemMessageLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LeftMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message: MessageEntity = messageList[position]
        if (holder is LeftMessageViewHolder) {
            holder.bind(message)
        } else if (holder is RightMessageViewHolder) {
            holder.bind(message)
        }
    }

    fun setMessageList(message: List<MessageEntity>) {
        messageList.clear()
        messageList.addAll(message)
        notifyDataSetChanged()
    }

    fun add(position: Int, message: MessageEntity) {
        messageList.add(position, message)
        notifyItemInserted(position)
    }

    fun replaceItem(message: MessageEntity) {
        val index = messageList.indexOf(message)
        messageList[index] = message
        notifyItemChanged(index)
    }

    private fun getOtherUserName(
        userNames: HashMap<String, String>,
        myUserKey: String?
    ): String? {
        for (userKey in userNames.keys) {
            if (myUserKey != userKey) {
                return userNames[userKey]
            }
        }
        return null
    }

    private fun getOtherUserProfile(
        userProfiles: HashMap<String, String>,
        myUserKey: String?
    ): String? {
        for (userKey in userProfiles.keys) {
            if (myUserKey != userKey) {
                return userProfiles[userKey]
            }
        }
        return null
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val message: MessageEntity = messageList[position]
        return if (myUserKey == message.userKey) {
            RIGHT_TYPE
        } else LEFT_TYPE
    }

    class RightMessageViewHolder(
        private val binding: ItemMessageRightBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: MessageEntity) {
            binding.messageRight = message
            binding.executePendingBindings()
        }
    }

    class LeftMessageViewHolder(
        private val binding: ItemMessageLeftBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: MessageEntity) {
            binding.messageLeft = message
            binding.executePendingBindings()
        }
    }
}