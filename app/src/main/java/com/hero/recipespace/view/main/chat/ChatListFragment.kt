package com.hero.recipespace.view.main.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.hero.recipespace.R
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.databinding.FragmentChatListBinding
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.view.main.chat.viewmodel.ChatListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChatListFragment: Fragment() {

    private var chatListRegistration: ListenerRegistration? = null

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private var userKey: String? = null

    private val viewModel by viewModels<ChatListViewModel>()

    private lateinit var chatListAdapter: ChatListAdapter

    companion object {
        fun newInstance() = ChatListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userKey = FirebaseAuth.getInstance().currentUser?.uid
        chatListRegistration = FirebaseData.getInstance().getChatList(userKey!!, this)

        setupView()
        setupViewModel()
        setupChatListChangeListener()
    }

    private fun setupView() {
        initRecyclerView(binding.rvChatList)
    }

    private fun setupViewModel() {
        with(viewModel) {
            chatList.observe(viewLifecycleOwner) { chatList ->
                chatListAdapter.setChatList(chatList)
            }
        }
    }

    // TODO: 2022-12-12 리스너 대체하기
    private fun setupChatListChangeListener(changeType: DocumentChange.Type?, chatData: ChatData) {
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

    private fun initRecyclerView(recyclerView: RecyclerView) {
        chatListAdapter = ChatListAdapter(
            onClick = ::showChatRoom
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = chatListAdapter
        }
    }

    // TODO: 2022-12-13 3번째 인자 넣어주기
    private fun showChatRoom(chat: ChatEntity) {
        val intent = ChatActivity.getIntent(requireActivity(), chat.key.orEmpty())
        startActivity(intent)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
        if (chatListRegistration != null) {
            chatListRegistration!!.remove()
        }
    }
}