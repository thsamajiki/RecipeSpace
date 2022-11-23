package com.hero.recipespace.database.notice

import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import com.hero.recipespace.listener.OnCompleteListener

class NoticeRepositoryImpl : NoticeRepository {
    override fun getNoticeList(onCompleteListener: OnCompleteListener<List<NoticeEntity>>) {
        TODO("Not yet implemented")
    }
}