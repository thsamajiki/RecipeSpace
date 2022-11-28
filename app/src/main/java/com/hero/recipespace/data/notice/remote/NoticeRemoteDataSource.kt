package com.hero.recipespace.data.notice.remote

import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.listener.OnCompleteListener

interface NoticeRemoteDataSource {
    suspend fun getData(noticeKey: String, onCompleteListener: OnCompleteListener<NoticeData>)

    fun getDataList(onCompleteListener: OnCompleteListener<List<NoticeData>>)

    suspend fun add(noticeData: NoticeData, onCompleteListener: OnCompleteListener<NoticeData>)

    suspend fun update(noticeData: NoticeData, onCompleteListener: OnCompleteListener<NoticeData>)

    suspend fun remove(noticeData: NoticeData, onCompleteListener: OnCompleteListener<NoticeData>)
}