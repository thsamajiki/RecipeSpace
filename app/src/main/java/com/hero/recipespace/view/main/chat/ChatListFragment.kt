package com.hero.recipespace.view.main.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.R
import com.hero.recipespace.databinding.FragmentChatListBinding
import com.hero.recipespace.view.main.chat.viewmodel.ChatListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatListFragment : Fragment() {
    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private var userKey: String? = null

    private val viewModel by viewModels<ChatListViewModel>()

    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        userKey = FirebaseAuth.getInstance().currentUser?.uid

        setupView()
        setupViewModel()

        binding.layoutSwipeRefresh.setOnRefreshListener {
            viewModel.refreshChatList(userKey.orEmpty())
            binding.layoutSwipeRefresh.isRefreshing = false
        }
    }

    private fun setupView() {
        initRecyclerView(binding.rvChatList)

        val nav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        nav?.setOnItemReselectedListener { item ->
            if (item.itemId == R.id.menu_recipe) {
                binding.rvChatList.smoothScrollToPosition(0)
            }
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {
                chatListUiState.observe(viewLifecycleOwner) { _ ->
                }
            }
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        chatAdapter =
            ChatAdapter(
                onClick = {
                    showChatRoom(it.id)
                },
            )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
    }

    private fun showChatRoom(chatKey: String) {
        val intent = ChatActivity.getIntent(requireActivity(), chatKey)
        startActivity(intent)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance() = ChatListFragment()
    }
}
