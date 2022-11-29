package com.hero.recipespace.listener

import com.google.firebase.firestore.DocumentChange
import com.hero.recipespace.data.notice.NoticeData

interface OnNoticeListChangeListener {
    fun onNoticeListChange(changeType: DocumentChange.Type?, noticeData: NoticeData?)
}