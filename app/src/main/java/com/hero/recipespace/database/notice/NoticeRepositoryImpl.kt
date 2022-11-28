package com.hero.recipespace.database.notice

import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.data.notice.remote.NoticeRemoteDataSource
import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class NoticeRepositoryImpl(
    private val noticeRemoteDataSource: NoticeRemoteDataSource
) : NoticeRepository {
    override suspend fun getNotice(noticeKey: String, onCompleteListener: OnCompleteListener<NoticeEntity>) {
        noticeRemoteDataSource.getData(noticeKey, object : OnCompleteListener<NoticeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<NoticeData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override fun getNoticeList(onCompleteListener: OnCompleteListener<List<NoticeEntity>>) {
        noticeRemoteDataSource.getDataList(object : OnCompleteListener<List<NoticeData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<NoticeData>>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }
}