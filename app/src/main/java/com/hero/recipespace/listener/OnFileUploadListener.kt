package com.hero.recipespace.listener

interface OnFileUploadListener {
    fun onFileUploadComplete(isSuccess: Boolean, downloadUrl: String?)
    fun onFileUploadProgress(percent: Float)
}