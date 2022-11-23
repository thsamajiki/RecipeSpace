package com.hero.recipespace.data.notice.remote

import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.listener.OnCompleteListener

class NoticeRemoteDataSourceImpl : NoticeRemoteDataSource {
    override fun getData(noticeKey: String, onCompleteListener: OnCompleteListener<NoticeData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        noticeKey: String,
        onCompleteListener: OnCompleteListener<List<NoticeData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(noticeData: NoticeData, onCompleteListener: OnCompleteListener<NoticeData>) {
        TODO("Not yet implemented")
    }

    override fun update(
        noticeData: NoticeData,
        onCompleteListener: OnCompleteListener<NoticeData>,
    ) {
        TODO("Not yet implemented")
    }

    override fun remove(
        noticeData: NoticeData,
        onCompleteListener: OnCompleteListener<NoticeData>,
    ) {
        TODO("Not yet implemented")
    }
}