package com.hero.recipespace.view.main.account.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.hero.recipespace.databinding.ActivitySettingBinding
import com.hero.recipespace.databinding.DialogInAppReviewBinding
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
            onNoticeLayoutClick()
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
            showInAppReviewDialog()
        }
        binding.layoutItemOpenSource.setOnClickListener {
            openOpenSourcePopUp()
        }
        binding.layoutItemDropOut.setOnClickListener {
            openDropOutPopUp()
        }
    }

    private fun onNoticeLayoutClick() {
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

    private fun showInAppReviewDialog() {
        val builder = AlertDialog.Builder(this)
        val dialog = builder.create()
        val inAppReviewBinding = DialogInAppReviewBinding.inflate(layoutInflater)
        inAppReviewBinding.btnNo.setOnClickListener { view ->
            dialog.dismiss()
        }
        inAppReviewBinding.btnLater.setOnClickListener { view -> dialog.dismiss() }
        inAppReviewBinding.btnOk.setOnClickListener { view ->
            openInAppReviewDialog()
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.setView(inAppReviewBinding.root)
        dialog.show()
    }

    private fun openInAppReviewDialog() {
        val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                manager.launchReviewFlow(this, reviewInfo)
            } else {
//                showInAppReviewDialog()

                @ReviewErrorCode
                val reviewErrorCode = task.exception.hashCode()
                Log.e("openInAppReviewDialog", "openInAppReviewDialog: $reviewErrorCode")
            }
        }
    }

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