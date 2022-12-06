package com.hero.recipespace.domain.notice.usecase

import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import com.hero.recipespace.listener.OnCompleteListener
import javax.inject.Inject

class GetNoticeUseCase @Inject constructor(
    private val noticeRepository: NoticeRepository
) {
    suspend fun invoke(noticeKey: String, onCompleteListener: OnCompleteListener<NoticeEntity>) =
        noticeRepository.getNotice(noticeKey, onCompleteListener)
}