package com.hero.recipespace.util

import android.animation.ObjectAnimator
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.hero.recipespace.R

class LoadingProgress {
    private var mDialog: Dialog? = null
    private var mProgressBar: ProgressBar? = null

    fun showDialog(context: Context?, touch: Boolean?) {
        mDialog = Dialog(context!!, R.style.LoadingDialog)
        mDialog!!.addContentView(
            ProgressBar(context),
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        )
        mDialog!!.setCancelable(touch!!)
        mDialog!!.show()
    }

    fun dismissDialog() {
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog!!.dismiss()
        }
    }

    fun initProgressDialog(context: Context) {
        mProgressBar = ProgressBar(context)
        mProgressBar!!.setMessage(context.getString(R.string.loading))
        mProgressBar!!.setCancelable(false)
        mProgressBar!!.max = 100
        mProgressBar!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        mProgressBar!!.show()
    }

    fun setProgress(i: Int) {
        val animation = ObjectAnimator.ofInt(mProgressBar, "progress", i)
        animation.duration = 500 // 0.5 second
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.start()
    }

    fun dismissProgressDialog() {
        if (mProgressBar != null && mProgressBar!!.isShowing) {
            mProgressBar!!.dismiss()
        }
    }
}