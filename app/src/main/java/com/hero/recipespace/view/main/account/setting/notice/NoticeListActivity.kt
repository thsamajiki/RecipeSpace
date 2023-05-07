package com.hero.recipespace.view.main.account.setting.notice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityNoticeListBinding
import com.hero.recipespace.view.main.account.setting.notice.viewmodel.NoticeListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeListActivity : AppCompatActivity(),
    View.OnClickListener {

    private lateinit var binding: ActivityNoticeListBinding

    private val viewModel by viewModels<NoticeListViewModel>()

    private lateinit var noticeListAdapter: NoticeListAdapter

    companion object {
        fun getIntent(context: Context) =
            Intent(context, NoticeListActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notice_list)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        initRecyclerView(binding.rvNoticeList)
        setupView()
        setupListeners()
    }

    private fun setupView() {
        initRecyclerView(binding.rvNoticeList)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.run {
            setHasFixedSize(true)
            adapter = noticeListAdapter
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onClick(view: View) {
    }
}