package com.hero.recipespace.view.main.chat

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.hero.recipespace.R
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.databinding.ItemMessageLeftBinding
import com.hero.recipespace.databinding.ItemMessageRightBinding
import com.hero.recipespace.view.BaseAdapter

class ChatAdapter(
) : BaseAdapter<RecyclerView.ViewHolder, MessageData>() {

    private var messageDataList: List<MessageData>? = null
    private var requestManager: RequestManager? = null
    private val LEFT_TYPE = 0
    private val RIGHT_TYPE = 1
    private var myUserKey: String? = null
    private var chatData: ChatData? = null


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
        val messageData: MessageData? = messageDataList?.get(position)
        if (holder is LeftMessageViewHolder) {
            val otherUserProfile = getOtherUserProfile(chatData.userProfiles, myUserKey)
            val otherUserName = getOtherUserName(chatData.userNames, myUserKey)
            if (TextUtils.isEmpty(otherUserProfile)) {
                requestManager?.load(R.drawable.ic_default_user_profile)?.into(holder.ivProfile)
            } else {
                requestManager!!.load(otherUserProfile).into(holder.ivProfile)
            }
            holder.tvChat.t(messageData.getMessage())
            holder.tvDate.setText(TimeUtils.getInstance()
                .convertTimeFormat(messageData.getTimestamp().toDate(), "MM.dd"))
            holder.tvUserName.text = otherUserName
        } else {
            (holder as RightMessageViewHolder).tvChat.setText(messageData.getMessage())
            holder.tvDate.setText(TimeUtils.getInstance()
                .convertTimeFormat(messageData.getTimestamp().toDate(), "MM.dd"))
        }
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
        return messageDataList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val messageData: MessageData = messageDataList!![position]
        return if (myUserKey == messageData.userKey) {
            RIGHT_TYPE
        } else LEFT_TYPE
    }

    class RightMessageViewHolder(
        private val binding: ItemMessageRightBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatData: ChatData) {
//            binding.tvChatDate
//            binding.tvChatContent

            binding.chatRight = chatData
            binding.executePendingBindings()
        }
    }

    class LeftMessageViewHolder(
        private val binding: ItemMessageLeftBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatData: ChatData) {
//            binding.ivUserProfile
//            binding.tvChatDate
//            binding.tvChatContent
//            binding.tvUserName

            binding.chatLeft = chatData
            binding.executePendingBindings()
        }
    }
}