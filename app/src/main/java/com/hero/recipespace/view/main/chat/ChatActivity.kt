package com.hero.recipespace.view.main.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ListenerRegistration
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityChatBinding
import com.hero.recipespace.view.main.chat.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter

    private val viewModel by viewModels<ChatViewModel>()

    companion object {
        fun getIntent(
            context: Context,
            chatKey: String = "",
            otherUserInfo: OtherUserInfo? = null,
        ) =
            Intent(context, ChatActivity::class.java)
                .putExtra(ChatViewModel.CHAT_KEY, chatKey)
                .putExtra(ChatViewModel.EXTRA_OTHER_USER, otherUserInfo)
    }

    private var messageRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupView()
        setupViewModel()
        setupListeners()
    }

    private fun setupView() {
        initRecyclerView(binding.rvChat)
    }

    private fun setupViewModel() {
        with(viewModel) {
            messageList.observe(this@ChatActivity) { messageList ->
                chatAdapter.setMessageList(messageList)
            }

//            chat.observe(this@ChatActivity) {
//
//            }
//
//            lifecycleScope.launch {
//                chatUiState.observe(this@ChatActivity) { state ->
//                    when (state) {
//                        is ChatUIState.Failed -> TODO()
//                        is ChatUIState.Success -> TODO()
//                    }
//                }
//            }
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.dlContentOptionMenu.setOnClickListener { view ->
            view.visibility = View.VISIBLE
        }

        binding.mcvSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        chatAdapter = ChatAdapter()

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
    }

    // 채팅방에 이미 메시지가 있을 때 첫 메시지를 보낼 때 사용되는 메소드
    private fun sendMessage() {
        val message = binding.editMessage.text.toString()
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        binding.editMessage.setText("")

        viewModel.sendMessage(message)
    }

    object Result {
        const val CHAT_KEY = "chatKey"
    }

    override fun onClick(view: View) {
    }
}