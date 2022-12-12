package com.hero.recipespace.view.main.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ListenerRegistration
import com.hero.recipespace.databinding.ActivityChatBinding
import com.hero.recipespace.view.main.chat.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter

    private val viewModel by viewModels<ChatViewModel>()

    companion object {
        fun getIntent(context: Context, chatKey: String, otherUserKey: String) =
            Intent(context, ChatActivity::class.java)
                .putExtra(ChatViewModel.RECIPE_CHAT_KEY, chatKey)
                .putExtra(ChatViewModel.EXTRA_OTHER_USER_KEY, otherUserKey)
    }

    private var messageRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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

            chat.observe(this@ChatActivity) {

            }
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
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

    private fun sendMessage() {
        val message = binding.editMessage.text.toString()
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        binding.editMessage.setText("")

        viewModel.sendMessage(message)

    }

    // TODO: 2022-12-12 ViewModel 과 작업하기
//    private fun createChatRoom() {
//        val firebaseData: FirebaseData = FirebaseData.getInstance()
//        val message = binding.editMessage.text.toString()
//        if (TextUtils.isEmpty(message)) {
//            Toast.makeText(this, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show()
//            return
//        }
//        binding.editMessage.setText("")
//
//
//        firebaseData.createChatRoom(this,
//            getOtherUserKey()!!,
//            message,
//            object : OnCompleteListener<ChatData?>() {
//                fun onComplete(isSuccess: Boolean, response: Response<ChatData?>) {
//                    if (isSuccess && response.isNotEmpty()) {
//                        chat = response.getData()
//                        initRecyclerView(binding.rvChat)
//                        initMessageRegistration()
//                    }
//                }
//            })
//    }

    object Result {
        const val CHAT_KEY = "chatKey"
    }

    override fun onClick(view: View) {
    }
}