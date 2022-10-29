package com.hero.recipespace.view.main.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.hero.recipespace.R
import com.hero.recipespace.data.ChatData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.databinding.FragmentChatListBinding
import com.hero.recipespace.listener.OnChatListChangeListener
import com.hero.recipespace.listener.OnRecyclerItemClickListener
import com.hero.recipespace.util.MyInfoUtil
import java.util.*

class ChatListFragment: Fragment(), OnChatListChangeListener,
    OnRecyclerItemClickListener<ChatData> {

    private var chatListRegistration: ListenerRegistration? = null
    private var userKey: String? = null
    private val chatDataList = listOf<ChatData>()

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatListAdapter: ChatListAdapter

    companion object {
        fun newInstance() = ChatListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userKey = MyInfoUtil.getInstance().getKey()
        chatListRegistration = FirebaseData.getInstance().getChatList(userKey, this)
        initRecyclerView(binding.rvChatList)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        chatListAdapter = ChatListAdapter()

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = chatListAdapter
        }
    }

    override fun onChatListChange(changeType: DocumentChange.Type?, chatData: ChatData) {
        when (changeType) {
            DocumentChange.Type.ADDED -> {
                chatDataList.add(0, chatData)
                Collections.sort(chatDataList)
                chatListAdapter.notifyDataSetChanged()
            }
            DocumentChange.Type.MODIFIED -> {
                chatDataList.remove(chatData)
                chatDataList.add(0, chatData)
                chatListAdapter.notifyDataSetChanged()
            }
            DocumentChange.Type.REMOVED -> {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (chatListRegistration != null) {
            chatListRegistration!!.remove()
        }
    }

    override fun onItemClick(position: Int, view: View, data: ChatData) {
        val intent = Intent(requireActivity(), ChatActivity::class.java)
        intent.putExtra(EXTRA_CHAT_DATA, data)
        startActivity(intent)
    }
}