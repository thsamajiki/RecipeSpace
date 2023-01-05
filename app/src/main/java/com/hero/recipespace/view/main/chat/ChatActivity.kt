package com.hero.recipespace.view.main.chat

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
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
import com.hero.recipespace.ext.isScrollable
import com.hero.recipespace.ext.setStackFromEnd
import com.hero.recipespace.view.main.chat.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private var isKeyboardOpen = false // 키보드 올라왔는지 확인

    private val viewModel by viewModels<ChatViewModel>()

    companion object {
        fun getIntent(
            context: Context,
            chatKey: String = ""
        ) =
            Intent(context, ChatActivity::class.java)
                .putExtra(ChatViewModel.CHAT_KEY, chatKey)

        fun getIntent(
            context: Context,
            recipeChatInfo: RecipeChatInfo? = null,
        ) =
            Intent(context, ChatActivity::class.java)
                .putExtra(ChatViewModel.EXTRA_RECIPE_CHAT, recipeChatInfo)
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
        checkKeyboardOpenClose()
        initRecyclerView(binding.rvChat)
    }

    private fun setupViewModel() {
        with(viewModel) {
            messageList.observe(this@ChatActivity) { messageList ->
                chatAdapter.setMessageList(messageList)
            }
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivOptionMenu.setOnClickListener {
            Toast.makeText(this, "ivOptionMenu-ChatActivity", Toast.LENGTH_SHORT).show()
            val isDrawerOpened = binding.dlChatMemberList.isDrawerOpen(Gravity.LEFT)
            if (isDrawerOpened) {
                binding.dlChatMemberList.closeDrawer(Gravity.LEFT)
            } else {
                binding.dlChatMemberList.openDrawer(Gravity.LEFT)
            }
        }

        binding.ivMessageContentOption.setOnClickListener { view ->
            val isDrawerClosed = binding.dlChatMemberList.isDrawerOpen(Gravity.RIGHT)
            if (isDrawerClosed) {
                binding.dlChatMemberList.closeDrawer(Gravity.RIGHT)
            } else {
                binding.dlChatMemberList.openDrawer(Gravity.RIGHT)
            }
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
            smoothScrollToPosition((viewModel.messageList.value?.size?.minus(1) ?: 0))
            adapter = chatAdapter

            /**
             * 1. 키보드가 올라온 경우에만 스크롤이 가능한 경우 처리
             * 키보드가 내려간 경우 스크롤이 불가능하지만 키보드가 올라오면서 스크롤이 가능한 경우
             * */
            addOnLayoutChangeListener(onLayoutChangeListener)

            /**
             * 2. 키보드가 올라온 상태에서 데이터를 추가해 키보드가 내려갔을 때에도 스크롤이 가능한 경우
             * 3. 화면 진입 시 데이터를 불러와 청므부터 스크롤이 가능한 경우
             * 키보드가 열리지 않은 상태에서 스크롤 가능 상태이면 StackFromEnd 설정
             * 키보드가 열린 상태에서 체크하면 키보드가 사라질 때 목록이 하단에 붙을 수 있음
             * */
            viewTreeObserver.addOnScrollChangedListener {
                if (isScrollable() && !isKeyboardOpen) { // 스크롤이 가능하면서 키보드가 닫힌 상태일 떄만
                    setStackFromEnd()
                    removeOnLayoutChangeListener(onLayoutChangeListener)
                }
            }
        }
    }

    private val onLayoutChangeListener =
        View.OnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            // 키보드가 올라와 높이가 변함
            if (bottom < oldBottom) {
                binding.rvChat.scrollBy(0, oldBottom - bottom) // 스크롤 유지를 위해 추가
            }
        }

    // 키보드 Open/Close 체크
    private fun checkKeyboardOpenClose() {
        binding.clRootContainer.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.clRootContainer.getWindowVisibleDisplayFrame(rect)

            val rootViewHeight = binding.clRootContainer.rootView.height
            val heightDiff = rootViewHeight - rect.height()
            isKeyboardOpen = heightDiff > rootViewHeight * 0.25 // true == 키보드 올라감
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