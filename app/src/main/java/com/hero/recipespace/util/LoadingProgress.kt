package com.hero.recipespace.util

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.hero.recipespace.R

class LoadingProgress {
//    private var dialog: Dialog? = null
//    private var progressBar: ProgressBar? = null

    companion object {
        fun initProgressDialog(context: Context) {
            val dialog = Dialog(context, R.style.LoadingDialog)
            val progressIndicator = LinearProgressIndicator(context, null, 0)
            val progressBar = ProgressBar(context)
            dialog.setContentView(progressIndicator)
            dialog.addContentView(
                LinearProgressIndicator(context),
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            )
        }

        fun showDialog(context: Context, touch: Boolean) {
            val dialog = Dialog(context, R.style.LoadingDialog)
            initProgressDialog(context)
            dialog.setCancelable(touch)
            dialog.show()
        }

        fun dismissProgressDialog() {
            val dialog = Dialog(context, R.style.LoadingDialog)
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }

        fun setProgress(i: Int) {
            val animation = ObjectAnimator.ofInt(progressBar, "progress", i)
            animation.duration = 500 // 0.5 second
            animation.interpolator = AccelerateDecelerateInterpolator()
            animation.start()
        }
    }
}