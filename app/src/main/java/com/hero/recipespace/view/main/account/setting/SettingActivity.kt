package com.hero.recipespace.view.main.account.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.hero.recipespace.databinding.ActivitySettingBinding
import com.hero.recipespace.view.main.account.setting.notice.NoticeListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    private val reviewManager: ReviewManager? = null
    private val reviewInfo: ReviewInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
//        readyPlayStoreReview();
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
        val intent = Intent(this, NoticeListActivity::class.java)
        startActivity(intent)
    }

    private fun openFontPopUp() {
        // TODO: 2021-01-12 선택 가능한 폰트들이 나오는 창이 아래에서 위로 나오게 하기
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
    //        // TODO: 2021-01-12 플레이스토어에 있는 리뷰 작성 페이지로 바로 이동하도록 링크를 만들기
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