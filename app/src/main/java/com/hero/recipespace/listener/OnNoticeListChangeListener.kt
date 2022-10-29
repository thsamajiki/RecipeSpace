package com.hero.recipespace.listener

import com.google.firebase.firestore.DocumentChange

interface OnNoticeListChangeListener {
    fun onNoticeListChange(changeType: DocumentChange.Type?, noticeData: NoticeData?)
}