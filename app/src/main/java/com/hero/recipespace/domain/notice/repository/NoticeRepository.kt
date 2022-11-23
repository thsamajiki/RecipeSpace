package com.hero.recipespace.domain.notice.repository

import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.listener.OnCompleteListener

interface NoticeRepository {
    fun getNoticeList(onCompleteListener: OnCompleteListener<List<NoticeEntity>>)
}