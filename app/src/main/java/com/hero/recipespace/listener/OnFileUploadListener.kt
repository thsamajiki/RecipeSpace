package com.hero.recipespace.listener

interface OnFileUploadListener {
    suspend fun onFileUploadComplete(isSuccess: Boolean, downloadUrl: String?)
    suspend fun onFileUploadProgress(percent: Float)
}