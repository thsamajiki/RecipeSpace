package com.hero.recipespace.view.main.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.databinding.ItemChatListBinding
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.view.BaseAdapter

class ChatListAdapter(
    private val onClick: (ChatEntity) -> Unit
) : BaseAdapter<ChatListAdapter.ChatListViewHolder, ChatEntity>() {

    private val chatDataList = mutableListOf<ChatEntity>()
    private var myUserKey: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val binding = ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ChatListViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val chatData = chatDataList[position]
        holder.bind(chatData)
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

    fun setChatList(chat: List<ChatEntity>) {
        chatDataList.clear()
        chatDataList.addAll(chat)
        notifyDataSetChanged()
    }

    fun add(position: Int, chat: ChatEntity) {
        chatDataList.add(position, chat)
        notifyItemInserted(position)
    }

    fun replaceItem(chat: ChatEntity) {
        val index = chatDataList.indexOf(chat)
        chatDataList[index] = chat
        notifyItemChanged(index)
    }

    override fun getItemCount(): Int {
        return chatDataList.size
    }

    class ChatListViewHolder(
        val binding: ItemChatListBinding,
        private val onClick: (ChatEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: ChatEntity) {
//            binding.ivUserProfile = itemView.findViewById(R.id.iv_user_profile)
//            binding.ivUserProfile = chatData.
//            binding.tvUserName = itemView.findViewById(R.id.tv_user_name)
//            binding.tvChatDate = itemView.findViewById(R.id.tv_chat_date)
//            binding.tvChatContent = itemView.findViewById(R.id.tv_chat)

            binding.root.setOnClickListener {
                onClick(chat)
            }

            binding.chat = chat
            binding.executePendingBindings()
        }
    }
}