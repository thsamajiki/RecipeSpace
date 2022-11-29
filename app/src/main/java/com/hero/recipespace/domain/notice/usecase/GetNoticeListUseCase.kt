package com.hero.recipespace.domain.notice.usecase

import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import com.hero.recipespace.listener.OnCompleteListener

class GetNoticeListUseCase(
    private val noticeRepository: NoticeRepository
) {
    fun invoke(onCompleteListener: OnCompleteListener<List<NoticeEntity>>) {
        noticeRepository.getNoticeList(onCompleteListener)
    }
}