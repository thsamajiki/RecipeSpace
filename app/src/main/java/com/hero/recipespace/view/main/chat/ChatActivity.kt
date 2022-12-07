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
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.databinding.ActivityChatBinding
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.listener.OnMessageListener
import com.hero.recipespace.view.main.chat.viewmodel.ChatViewModel

class ChatActivity : AppCompatActivity(), View.OnClickListener, OnMessageListener {

    private lateinit var binding: ActivityChatBinding

    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<MessageEntity>()
    private var chat: ChatEntity? = null

    private val viewModel by viewModels<ChatViewModel>()

    companion object {
        const val EXTRA_OTHER_USER_KEY = "otherUserKey"
        const val EXTRA_CHAT_DATA = "chatData"

        private const val RECIPE_CHAT_KEY = "chatKey"

        fun getIntent(context: Context, chatKey: String) =
            Intent(context, ChatActivity::class.java)
                .putExtra(RECIPE_CHAT_KEY, chatKey)
    }

    private var messageRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.viewModel = viewModel

        chat = getChatData()
        if (chat != null) {
            initRecyclerView(binding.rvChat)
            initMessageRegistration()
        } else {
            checkExistChatData()
        }

        setupViewModel()

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.mcvSend.setOnClickListener {
            if (chat == null) {
                createChatRoom()
            } else {
                sendMessage()
            }
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            messageList.observe(this@ChatActivity) { messageList ->
                chatAdapter.setMessageList(messageList)
            }
        }
    }

    private fun checkExistChatData() {
        if (TextUtils.isEmpty(getOtherUserKey())) {
            return
        }
        val firebaseData: FirebaseData = FirebaseData.getInstance()
        firebaseData.checkExistChatData(getOtherUserKey(),
            object : OnCompleteListener<ChatData?>() {
                fun onComplete(isSuccess: Boolean, response: Response<ChatData?>) {
                    if (isSuccess && response.isNotEmpty()) {
                        chat = response.getData()
                        initRecyclerView()
                        initMessageRegistration()
                    }
                }
            })
    }

    private fun initMessageRegistration() {
        messageRegistration = FirebaseData.getInstance().getMessageList(chat?.key, this)
    }


    private fun initRecyclerView(recyclerView: RecyclerView) {
        chatAdapter = ChatAdapter()

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
    }

    private fun getChatData(): ChatEntity? {
        return intent.getParcelableExtra(ChatActivity.EXTRA_CHAT_DATA)
    }

    private fun getOtherUserKey(): String? {
        return intent.getStringExtra(ChatActivity.EXTRA_OTHER_USER_KEY)
    }

    private fun sendMessage() {
        val message = binding.editMessage.text.toString()
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        binding.editMessage.setText("")
        val firebaseData: FirebaseData = FirebaseData.getInstance()
        firebaseData.sendMessage(message, chat)
    }

    private fun createChatRoom() {
        val firebaseData: FirebaseData = FirebaseData.getInstance()
        val message = binding.editMessage.text.toString()
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        binding.editMessage.setText("")
        firebaseData.createChatRoom(this,
            getOtherUserKey()!!,
            message,
            object : OnCompleteListener<ChatData?>() {
                fun onComplete(isSuccess: Boolean, response: Response<ChatData?>) {
                    if (isSuccess && response.isNotEmpty()) {
                        chat = response.getData()
                        initRecyclerView()
                        initMessageRegistration()
                    }
                }
            })
    }

    fun onMessage(isSuccess: Boolean, message: MessageEntity?) {
        if (isSuccess && message != null) {
            messageList.add(message)
            chatAdapter.notifyItemInserted(messageList.size - 1)
            binding.rvChat.smoothScrollToPosition(messageList.size - 1)
        }
    }

    override fun onClick(view: View) {
    }
}