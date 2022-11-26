package com.hero.recipespace.data.notice.remote

import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.listener.OnCompleteListener

interface NoticeRemoteDataSource {
    fun getData(noticeKey: String, onCompleteListener: OnCompleteListener<NoticeData>)

    fun getDataList(onCompleteListener: OnCompleteListener<List<NoticeData>>)

    fun add(noticeData: NoticeData, onCompleteListener: OnCompleteListener<NoticeData>)

    fun update(noticeData: NoticeData, onCompleteListener: OnCompleteListener<NoticeData>)

    fun remove(noticeData: NoticeData, onCompleteListener: OnCompleteListener<NoticeData>)
}