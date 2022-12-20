package com.hero.recipespace.view.main.account.setting.notice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityNoticeListBinding
import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.view.main.account.setting.notice.viewmodel.NoticeListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeListActivity : AppCompatActivity(),
    View.OnClickListener {

    private lateinit var binding: ActivityNoticeListBinding
    private val noticeList = mutableListOf<NoticeEntity>()

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
        getNoticeData(viewModel.noticeItem.value!!)
        //        getNoticeListFromDatabase();
        setupView()
        setupViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        with(viewModel) {
//            noticeList.observe()
        }
    }

    private fun setupView() {
        initRecyclerView(binding.rvNoticeList)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        noticeListAdapter = NoticeListAdapter(
            onClick = ::getNoticeData
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = noticeListAdapter
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    //    private void getNoticeListFromDatabase() {
    //        NoticeRepository noticeRepository = new NoticeRepository(this);
    //        noticeRepository.getNoticeList(new OnCompleteListener<ArrayList<NoticeData>>() {
    //            @Override
    //            public void onComplete(boolean isSuccess, ArrayList<NoticeData> data) {
    //                if (isSuccess && data != null) {
    //                    noticeDataList.addAll(data);
    //                    noticeListAdapter.notifyDataSetChanged();
    //                } else {
    //                    Toast.makeText(NoticeListActivity.this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
    //                }
    //            }
    //        });
    //    }
    private fun getNoticeData(notice: NoticeEntity) {
//        FirebaseData.getInstance()
//            .getNoticeList(object : OnCompleteListener<List<NoticeData>> {
//                override fun onComplete(
//                    isSuccess: Boolean,
//                    response: Response<List<NoticeData>>?
//                ) {
//                    if (response != null) {
//                        if (isSuccess && response.isNotEmpty()) {
//                            noticeList.clear()
//                            noticeList.addAll(response.getData())
//                            noticeListAdapter.notifyDataSetChanged()
//                        }
//                    }
//                }
//            })
    }

    override fun onClick(view: View) {
    }
}