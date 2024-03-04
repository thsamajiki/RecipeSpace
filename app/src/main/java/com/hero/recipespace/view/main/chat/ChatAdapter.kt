package com.hero.recipespace.view.main.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.databinding.ItemChatBinding
import com.hero.recipespace.util.WLog
import com.hero.recipespace.view.BaseAdapter

class ChatAdapter(
    private val onClick: (ChatItem) -> Unit,
) : BaseAdapter<ChatAdapter.ChatListViewHolder, ChatItem>() {
    private val chatDataList = mutableListOf<ChatItem>()
    private var myUserKey: String? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ChatListViewHolder {
        val binding =
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )

        return ChatListViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(
        holder: ChatListViewHolder,
        position: Int,
    ) {
        val chatData = chatDataList[position]
        holder.bind(chatData)
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

    fun setChatList(chat: List<ChatItem>) {
        chatDataList.clear()
        chatDataList.addAll(chat)
        notifyDataSetChanged()
    }

    fun add(
        position: Int,
        chat: ChatItem,
    ) {
        chatDataList.add(position, chat)
        notifyItemInserted(position)
    }

    fun replaceItem(chat: ChatItem) {
        val index = chatDataList.indexOf(chat)
        chatDataList[index] = chat
        notifyItemChanged(index)
    }

    override fun getItemCount(): Int {
        return chatDataList.size
    }

    class ChatListViewHolder(
        val binding: ItemChatBinding,
        private val onClick: (ChatItem) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatItem) {
//            binding.ivUserProfile = itemView.findViewById(R.id.iv_user_profile)
//            binding.ivUserProfile = chatData.
//            binding.tvUserName = itemView.findViewById(R.id.tv_user_name)
//            binding.tvChatDate = itemView.findViewById(R.id.tv_chat_date)
//            binding.tvChatContent = itemView.findViewById(R.id.tv_chat)

            binding.root.setOnClickListener {
                onClick(chat)
            }

            WLog.d("chat $chat")
            binding.chat = chat
            binding.executePendingBindings()
        }
    }
}
