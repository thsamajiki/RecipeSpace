package com.hero.recipespace.view.main.chat

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.hero.recipespace.R
import com.hero.recipespace.data.ChatData
import com.hero.recipespace.data.MessageData
import com.hero.recipespace.databinding.ItemChatLeftBinding
import com.hero.recipespace.databinding.ItemChatRightBinding
import com.hero.recipespace.view.BaseAdapter
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(
) : BaseAdapter<RecyclerView.ViewHolder, MessageData>() {

    private var context: Context? = null
    private var messageDataArrayList: ArrayList<MessageData>? = null
    private var inflater: LayoutInflater? = null
    private var requestManager: RequestManager? = null
    private val LEFT_TYPE = 0
    private val RIGHT_TYPE = 1
    private var myUserKey: String? = null
    private var chatData: ChatData? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == RIGHT_TYPE) {
            view = inflater.inflate(R.layout.item_chat_right, parent, false)
            RightChatViewHolder(view)
        } else {
            view = inflater.inflate(R.layout.item_chat_left, parent, false)
            LeftChatViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messageData: MessageData = messageDataArrayList!![position]
        if (holder is LeftChatViewHolder) {
            val otherUserProfile = getOtherUserProfile(chatData.getUserProfiles(), myUserKey)
            val otherUserNickname = getOtherUserNickname(chatData.getUserNicknames(), myUserKey)
            if (TextUtils.isEmpty(otherUserProfile)) {
                requestManager.load(R.drawable.ic_default_user_profile).into(holder.ivProfile)
            } else {
                requestManager!!.load(otherUserProfile).into(holder.ivProfile)
            }
            holder.tvChat.setText(messageData.getMessage())
            holder.tvDate.setText(TimeUtils.getInstance()
                .convertTimeFormat(messageData.getTimestamp().toDate(), "MM.dd"))
            holder.tvUserNickname.text = otherUserNickname
        } else {
            (holder as RightChatViewHolder).tvChat.setText(messageData.getMessage())
            holder.tvDate.setText(TimeUtils.getInstance()
                .convertTimeFormat(messageData.getTimestamp().toDate(), "MM.dd"))
        }
    }

    private fun getOtherUserNickname(
        userNicknames: HashMap<String, String>,
        myUserKey: String?,
    ): String? {
        for (userKey in userNicknames.keys) {
            if (myUserKey != userKey) {
                return userNicknames[userKey]
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
        return messageDataArrayList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val messageData: MessageData = messageDataArrayList!![position]
        return if (myUserKey == messageData.getUserKey()) {
            RIGHT_TYPE
        } else LEFT_TYPE
    }

    class RightChatViewHolder(
        private val binding: ItemChatRightBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setChat(chatData: ChatData) {
            binding.tvChatDate
            binding.tvChatContent
        }
    }

    class LeftChatViewHolder(
        private val binding: ItemChatLeftBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setChat(chatData: ChatData) {
            binding.ivUserProfile
            binding.tvChatDate
            binding.tvChatContent
            binding.tvUserName
        }
    }
}