package com.hero.recipespace.view.main.account.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.hero.recipespace.databinding.ActivitySettingBinding
import com.hero.recipespace.view.main.account.setting.notice.NoticeListActivity
import com.hero.recipespace.view.main.account.setting.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.rlItemNotice.setOnClickListener {
            intentNoticeList()
        }
        binding.rlItemFont.setOnClickListener {
            openFontPopUp()
        }
        binding.rlItemDeleteCache.setOnClickListener {
            openDeleteCachePopUp()
        }
        binding.rlItemInquiry.setOnClickListener {
            openInquiryPopUp()
        }
        binding.rlItemReview.setOnClickListener {

        }
        binding.rlItemOpenSource.setOnClickListener {
            openOpenSource()
        }
    }

    fun onClick(view: View) {
    }

    private fun intentNoticeList() {
        val intent = NoticeListActivity.getIntent(this)
        startActivity(intent)
    }

    private fun openFontPopUp() {
        // TODO: 2021-01-12 ?????? ????????? ???????????? ????????? ?????? ???????????? ?????? ????????? ??????
    }

    private fun openDeleteCachePopUp() {
        val deleteCachePopUpMessage = "?????? ????????? ?????? ??????"
        val negativeText = "??????"
        MaterialAlertDialogBuilder(this).setMessage(deleteCachePopUpMessage)
            .setNegativeButton(negativeText
            ) { dialog, which -> }
            .create()
            .show()
    }

    private fun openInquiryPopUp() {
        val openInquiryAlertDialogBuilder = MaterialAlertDialogBuilder(this)
        val openInquiryTitle = "????????????"
        val openInquiryMessage = "chs8275@gmail.com??????\n?????? ?????????????????? :)"
        val positiveText = "??????"
        MaterialAlertDialogBuilder(this).setTitle(openInquiryTitle)
            .setMessage(openInquiryMessage)
            .setPositiveButton(positiveText
            ) { dialog, which -> }
            .create()
            .show()
    }

    //    private void readyPlayStoreReview() {
    //        // TODO: 2021-01-12 ????????????????????? ?????? ?????? ?????? ???????????? ?????? ??????????????? ????????? ?????????
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

    private fun openOpenSource() {
        val openSourceLicenseDialog = OpenSourceLicenseDialog(this)
        openSourceLicenseDialog.getOpenSourceLicenseDialog()
    }
}