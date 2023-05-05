package com.hero.recipespace.view.main.account.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.hero.recipespace.databinding.ActivitySettingBinding
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.view.LoadingState
import com.hero.recipespace.view.login.LoginActivity
import com.hero.recipespace.view.main.account.setting.notice.NoticeListActivity
import com.hero.recipespace.view.main.account.setting.viewmodel.DropOutUiState
import com.hero.recipespace.view.main.account.setting.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    private val reviewManager: ReviewManager? = null
    private val reviewInfo: ReviewInfo? = null

    private val viewModel by viewModels<SettingViewModel>()

    companion object {
        fun getIntent(context: Context) =
            Intent(context, SettingActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupViewModel()
        setupListeners()
//        readyPlayStoreReview();
    }

    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {
                dropOutUiState.collect { state ->
                    when(state) {
                        is DropOutUiState.Failed -> Toast.makeText(this@SettingActivity, state.message, Toast.LENGTH_SHORT).show()
                        is DropOutUiState.Success -> onSuccessDropOut()
                        DropOutUiState.Idle -> {}
                    }
                }
            }

            lifecycleScope.launch {
                loadingState.collect { state ->
                    when(state) {
                        is LoadingState.Hidden -> hideLoading()
                        is LoadingState.Loading -> showLoading()
                        is LoadingState.Progress -> setProgressPercent(state.value)
                        LoadingState.Idle -> {}
                    }
                }
            }
        }
    }

    private fun onSuccessDropOut() {
        val intent = LoginActivity.getIntent(this@SettingActivity)
        startActivity(intent)
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.layoutItemNotice.setOnClickListener {
            intentNoticeList()
        }
        binding.layoutItemFont.setOnClickListener {
            openFontPopUp()
        }
        binding.layoutItemDeleteCache.setOnClickListener {
            openDeleteCachePopUp()
        }
        binding.layoutItemInquiry.setOnClickListener {
            openInquiryPopUp()
        }
        binding.layoutItemReview.setOnClickListener {

        }
        binding.layoutItemOpenSource.setOnClickListener {
            openOpenSourcePopUp()
        }
        binding.layoutItemDropOut.setOnClickListener {
            openDropOutPopUp()
        }
    }

    private fun intentNoticeList() {
        val intent = NoticeListActivity.getIntent(this)
        startActivity(intent)
    }

    private fun openFontPopUp() {
        // 선택 가능한 폰트들이 나오는 창이 아래에서 위로 나옴
    }

    private fun openDeleteCachePopUp() {
        val deleteCachePopUpMessage = "캐시 데이터 삭제 완료"
        val negativeText = "닫기"
        MaterialAlertDialogBuilder(this).setMessage(deleteCachePopUpMessage)
            .setNegativeButton(negativeText
            ) { dialog, which -> }
            .create()
            .show()
    }

    private fun openInquiryPopUp() {
        val openInquiryAlertDialogBuilder = MaterialAlertDialogBuilder(this)
        val openInquiryTitle = "문의하기"
        val openInquiryMessage = "chs8275@gmail.com으로\n문의 부탁드립니다 :)"
        val positiveText = "확인"
        MaterialAlertDialogBuilder(this).setTitle(openInquiryTitle)
            .setMessage(openInquiryMessage)
            .setPositiveButton(positiveText
            ) { dialog, which -> }
            .create()
            .show()
    }

    //    private void readyPlayStoreReview() {
    //        // 플레이스토어에 있는 리뷰 작성 페이지로 바로 이동하도록 링크를 만들기
    //
    //        //reviewManager = ReviewManagerFactory.create(this);
    //        reviewManager = new FakeReviewManager(this);
    //        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
    //        request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
    //            @Override
    //            public void onComplete(boolean isSuccess, Response<ReviewInfo> response) {
    //                if (isSuccess) {
    //                    // We can get the ReviewInfo object
    //                    reviewInfo = response.getData();
    //                    reviewManager.launchReviewFlow(SettingActivity.this, reviewInfo)
    //                            .addOnSuccessListener(new OnSuccessListener<Void>() {
    //                                @Override
    //                                public void onSuccess(Void unused) {
    //
    //                                }
    //                            });
    //                }
    //    }
    //
    //    private void launchReviewDialog(ReviewManager reviewManager, ReviewInfo reviewInfo) {
    //        if (reviewInfo == null) {
    //            Intent intent = new Intent(Intent.ACTION_VIEW);
    //            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=my packagename "));
    //            startActivity(intent);
    //        }
    //        Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
    //        flow.addOnCompleteListener(new com.google.android.play.core.tasks.OnCompleteListener<Void>() {
    //            @Override
    //            public void onComplete(@NonNull Task<Void> task) {
    //
    //            }
    //        });
    //    }

    private fun openOpenSourcePopUp() {
        val openSourceLicenseDialog = OpenSourceLicenseDialog(this)
        openSourceLicenseDialog.getOpenSourceLicenseDialog()
    }

    private fun openDropOutPopUp() {
        val openDropOutTitle = "탈퇴하기"
        val openDropOutMessage = "탈퇴하시겠습니까?"
        val positiveText = "예"
        val negativeText = "아니오"
        MaterialAlertDialogBuilder(this).setTitle(openDropOutTitle)
            .setMessage(openDropOutMessage)
            .setPositiveButton(positiveText) { dialog, _ ->
                val user = viewModel.user.value
                viewModel.dropOut(user!!)
            }
            .setNegativeButton(negativeText) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}