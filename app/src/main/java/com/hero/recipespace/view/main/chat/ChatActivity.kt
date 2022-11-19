package com.hero.recipespace.view.main.chat

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.ListenerRegistration
import com.hero.recipespace.R
import com.hero.recipespace.data.ChatData
import com.hero.recipespace.data.MessageData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.listener.OnMessageListener
import com.hero.recipespace.listener.OnRecyclerItemClickListener

class ChatActivity : AppCompatActivity(), View.OnClickListener,
    OnRecyclerItemClickListener<MessageData>, OnMessageListener {

    private var cvSend: MaterialCardView? = null
    private var recyclerChat: RecyclerView? = null
    private var btnBack: ImageView? = null
    private var chatAdapter: ChatAdapter? = null
    private val messageDataList = List<MessageData>()
    private var chatData: ChatData? = null
    private var editMessage: EditText? = null
    val EXTRA_OTHER_USER_KEY = "otherUserKey"
    val EXTRA_CHAT_DATA = "chatData"
    private var messageRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initView()
        chatData = getChatData()
        if (chatData != null) {
            initAdapter()
            initMessageRegistration()
        } else {
            checkExistChatData()
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
                        chatData = response.getData()
                        initAdapter()
                        initMessageRegistration()
                    }
                }
            })
    }

    private fun initMessageRegistration() {
        messageRegistration = FirebaseData.getInstance().getMessageList(chatData.key, this)
    }

    private fun initView() {
        btnBack.setOnClickListener(this)
        cvSend.setOnClickListener(this)
    }

    private fun initAdapter() {
        chatAdapter = ChatAdapter(this, messageDataList, chatData)
        chatAdapter.setOnRecyclerItemClickListener(this)
        recyclerChat!!.adapter = chatAdapter
    }

    private fun getChatData(): ChatData? {
        return intent.getParcelableExtra(ChatActivity.EXTRA_CHAT_DATA)
    }

    private fun getOtherUserKey(): String? {
        return intent.getStringExtra(ChatActivity.EXTRA_OTHER_USER_KEY)
    }

    override fun onItemClick(position: Int, view: View?, data: MessageData) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_back -> finish()
            R.id.cv_send -> if (chatData == null) {
                createChatRoom()
            } else {
                sendMessage()
            }
        }
    }

    private fun sendMessage() {
        val message = editMessage!!.text.toString()
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        editMessage!!.setText("")
        val firebaseData: FirebaseData = FirebaseData.getInstance()
        firebaseData.sendMessage(message, chatData)
    }

    private fun createChatRoom() {
        val firebaseData: FirebaseData = FirebaseData.getInstance()
        val message = editMessage!!.text.toString()
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        editMessage!!.setText("")
        firebaseData.createChatRoom(this,
            getOtherUserKey(),
            message,
            object : OnCompleteListener<ChatData?>() {
                fun onComplete(isSuccess: Boolean, response: Response<ChatData?>) {
                    if (isSuccess && response.isNotEmpty()) {
                        chatData = response.getData()
                        initAdapter()
                        initMessageRegistration()
                    }
                }
            })
    }

    override fun onMessage(isSuccess: Boolean, messageData: MessageData?) {
        if (isSuccess && messageData != null) {
            messageDataList.add(messageData)
            chatAdapter.notifyItemInserted(messageDataList.size - 1)
            recyclerChat!!.smoothScrollToPosition(messageDataList.size - 1)
        }
    }
}