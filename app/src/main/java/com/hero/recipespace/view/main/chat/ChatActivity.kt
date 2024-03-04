package com.hero.recipespace.view.main.chat

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityChatBinding
import com.hero.recipespace.databinding.NavHeaderChatBinding
import com.hero.recipespace.ext.isScrollable
import com.hero.recipespace.ext.setStackFromEnd
import com.hero.recipespace.view.main.chat.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private var isKeyboardOpen = false // 키보드 올라왔는지 확인

    private val viewModel by viewModels<ChatViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        binding.lifecycleOwner = this
        binding.appBarChat.lifecycleOwner = this
        binding.appBarChat.layoutRootContainer.lifecycleOwner = this

        binding.viewModel = viewModel

        val headerView = binding.navViewChat.getHeaderView(0)
        val headerViewBinding = NavHeaderChatBinding.bind(headerView)
        headerViewBinding.viewModel = viewModel
        headerViewBinding.lifecycleOwner = this

        setupView()
        setupViewModel()
        setupListeners()
    }

    private fun setupView() {
        checkKeyboardOpenClose()
        setSupportActionBar(binding.appBarChat.toolBar)
        initRecyclerView(binding.appBarChat.layoutRootContainer.rvMessageList)
    }

    private fun setupViewModel() {
        var count1 = 0
        with(viewModel) {
            messageList.observe(this@ChatActivity) { messageDataList ->
                count1++
                Log.d("count1", "count1: $count1")
                Log.d("messageList1", "messageList: $messageList")
                Log.d("messageList2", "messageDataList: $messageDataList")
                messageAdapter.setMessageList(messageDataList)
//                if (messageList.isNotEmpty()) {
//                    chatAdapter.update(messageList)
//
//                }
                binding.appBarChat.layoutRootContainer.rvMessageList.scrollToPosition(messageAdapter.itemCount - 1)
            }
        }
    }

    private fun setupListeners() {
        binding.appBarChat.ivBack.setOnClickListener {
            finish()
        }

        binding.appBarChat.ivOptionMenu.setOnClickListener {
            val isDrawerOpened = binding.layoutChatMemberList.isDrawerOpen(GravityCompat.END)
            if (isDrawerOpened) {
                binding.layoutChatMemberList.closeDrawer(GravityCompat.END)
            } else {
                binding.layoutChatMemberList.openDrawer(GravityCompat.END)
            }
        }

//        binding.ivMessageContentOption.setOnClickListener {
//            val isDrawerClosed = binding.layoutContentOptionMenu.isDrawerOpen(GravityCompat.START)
//            if (isDrawerClosed) {
//                binding.layoutContentOptionMenu.closeDrawer(GravityCompat.START)
//            } else {
//                binding.layoutContentOptionMenu.openDrawer(GravityCompat.START)
//            }
//        }

        binding.appBarChat.layoutRootContainer.mcvSend.setOnClickListener {
            sendMessage()
        }

        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 500 }
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 500 }
        var isBottom = true

        binding.appBarChat.layoutRootContainer.rvMessageList.addOnScrollListener(
            object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int,
                ) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!binding.appBarChat.layoutRootContainer.rvMessageList.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE // 최하단일 경우 false 값 return
                    ) {
                        binding.appBarChat.layoutRootContainer.fabScrollBottom.startAnimation(
                            fadeOut,
                        )
                        binding.appBarChat.layoutRootContainer.fabScrollBottom.visibility =
                            View.GONE
                        isBottom = true
                    } else {
                        if (isBottom) {
                            binding.appBarChat.layoutRootContainer.fabScrollBottom.visibility =
                                View.VISIBLE
                            binding.appBarChat.layoutRootContainer.fabScrollBottom.startAnimation(
                                fadeIn,
                            )
                            isBottom = false
                        }
                    }
                }
            },
        )

        binding.appBarChat.layoutRootContainer.fabScrollBottom.setOnClickListener {
            binding.appBarChat.layoutRootContainer.rvMessageList.smoothScrollToPosition(
                viewModel.messageList.value?.size?.minus(1) ?: 0,
            )
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        messageAdapter = MessageAdapter()

        recyclerView.run {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            smoothScrollToPosition((viewModel.messageList.value?.size?.minus(1) ?: 0))

            /**
             * 1. 키보드가 올라온 경우에만 스크롤이 가능한 경우 처리
             * 키보드가 내려간 경우 스크롤이 불가능하지만 키보드가 올라오면서 스크롤이 가능한 경우
             * */
            addOnLayoutChangeListener(onLayoutChangeListener)

            /**
             * 2. 키보드가 올라온 상태에서 데이터를 추가해 키보드가 내려갔을 때에도 스크롤이 가능한 경우
             * 3. 화면 진입 시 데이터를 불러와 처음부터 스크롤이 가능한 경우
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
                binding.appBarChat.layoutRootContainer.rvMessageList.scrollBy(
                    0,
                    oldBottom - bottom,
                ) // 스크롤 유지를 위해 추가
            }
        }

    // 키보드 Open/Close 체크
    private fun checkKeyboardOpenClose() {
        binding.layoutChatMemberList.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.layoutChatMemberList.getWindowVisibleDisplayFrame(rect)

            val rootViewHeight = binding.layoutChatMemberList.rootView.height
            val heightDiff = rootViewHeight - rect.height()
            isKeyboardOpen = heightDiff > rootViewHeight * 0.25 // true == 키보드 올라감
        }
    }

    // 채팅방에 이미 메시지가 있을 때 첫 메시지를 보낼 때 사용되는 메소드
    private fun sendMessage() {
        val message = binding.appBarChat.layoutRootContainer.editMessage.text.toString()
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        binding.appBarChat.layoutRootContainer.editMessage.setText("")

        viewModel.sendMessage(message)
    }

    object Result {
        const val CHAT_KEY = "chatKey"
    }

    override fun onClick(view: View) {
    }

    companion object {
        fun getIntent(
            context: Context,
            chatKey: String = "",
        ) =
            Intent(
                context,
                ChatActivity::class.java,
            )
                .putExtra(
                    ChatViewModel.CHAT_KEY,
                    chatKey,
                )

        fun getIntent(
            context: Context,
            recipeChatInfo: RecipeChatInfo? = null,
        ) =
            Intent(
                context,
                ChatActivity::class.java,
            )
                .putExtra(
                    ChatViewModel.EXTRA_RECIPE_CHAT,
                    recipeChatInfo,
                )
    }
}
