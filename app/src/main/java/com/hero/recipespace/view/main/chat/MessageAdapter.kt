package com.hero.recipespace.view.main.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.databinding.ItemMessageLeftBinding
import com.hero.recipespace.databinding.ItemMessageRightBinding
import com.hero.recipespace.view.BaseAdapter

class MessageAdapter : BaseAdapter<RecyclerView.ViewHolder, MessageItem>() {
    private val messageList = mutableListOf<MessageItem>()

    private var myUserKey: String? = FirebaseAuth.getInstance().uid

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return if (viewType == RIGHT_TYPE) {
            val binding =
                ItemMessageRightBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            RightMessageViewHolder(binding)
        } else {
            val binding =
                ItemMessageLeftBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            LeftMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val message: MessageItem = messageList[position]
        if (holder is LeftMessageViewHolder) {
            holder.bind(message)
        } else if (holder is RightMessageViewHolder) {
            holder.bind(message)
        }
    }

    fun setMessageList(message: List<MessageItem>) {
        messageList.clear()
        messageList.addAll(message)
        notifyDataSetChanged()
    }

    fun add(
        position: Int,
        message: MessageItem,
    ) {
        messageList.add(position, message)
        notifyItemInserted(position)
    }

    fun replaceItem(message: MessageItem) {
        val index = messageList.indexOf(message)
        messageList[index] = message
        notifyItemChanged(index)
    }

    private fun getOtherUserName(
        userNames: HashMap<String, String>,
        myUserKey: String?,
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
        myUserKey: String?,
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
        val message: MessageItem = messageList[position]
        return if (myUserKey == message.userKey) {
            RIGHT_TYPE
        } else {
            LEFT_TYPE
        }
    }

    class RightMessageViewHolder(
        private val binding: ItemMessageRightBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageItem) {
            binding.messageRight = message

            if (message.isRead == true) {
                binding.tvMessageNotReadCheck.visibility = View.GONE
            }

            binding.executePendingBindings()
        }
    }

    class LeftMessageViewHolder(
        private val binding: ItemMessageLeftBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageItem) {
            binding.messageLeft = message
            binding.executePendingBindings()
        }
    }

    companion object {
        private const val LEFT_TYPE = 0
        private const val RIGHT_TYPE = 1
    }
}
