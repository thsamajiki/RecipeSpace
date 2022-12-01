package com.hero.recipespace.view.main.chat

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.R
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.databinding.ItemChatListBinding
import com.hero.recipespace.view.BaseAdapter
import java.util.*

class ChatListAdapter(
    private val onClick: (ChatData) -> Unit
) : BaseAdapter<ChatListAdapter.ChatListViewHolder, ChatData>() {

    private val chatDataList = mutableListOf<ChatData>()
    private var myUserKey: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val binding = ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ChatListViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val chatData: ChatData = chatDataList[position]
        val otherUserName = getOtherUserName(chatData.userNames, myUserKey)
        val otherUserProfile = getOtherUserProfile(chatData.getUserProfiles(), myUserKey)
        holder.binding.tvUserName.text = otherUserName
        Collections.sort(chatDataList)
        if (TextUtils.isEmpty(otherUserProfile)) {
            requestManager.load(R.drawable.ic_default_user_profile).into(holder.binding.ivUserProfile)
        } else {
            requestManager.load(otherUserProfile).into(holder.binding.ivUserProfile)
        }
        holder.binding.tvChatContent.setText(chatData.getLastMessage().getMessage())
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

    fun setChatList(chatData: List<ChatData>) {
        chatDataList.clear()
        chatDataList.addAll(chatData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return chatDataList.size
    }

    class ChatListViewHolder(
        val binding: ItemChatListBinding,
        private val onClick: (ChatData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatData: ChatData) {
//            binding.ivUserProfile = itemView.findViewById(R.id.iv_user_profile)
//            binding.ivUserProfile = chatData.
//            binding.tvUserName = itemView.findViewById(R.id.tv_user_name)
//            binding.tvChatDate = itemView.findViewById(R.id.tv_chat_date)
//            binding.tvChatContent = itemView.findViewById(R.id.tv_chat)

            binding.root.setOnClickListener {
                onClick(chatData)
            }

            binding.chat = chatData
            binding.executePendingBindings()
        }

        init {

        }
    }
}