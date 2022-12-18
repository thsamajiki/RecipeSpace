package com.hero.recipespace.domain.notice.usecase

import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import javax.inject.Inject

class ObserveNoticeListUseCase @Inject constructor(
    private val noticeRepository: NoticeRepository
) {
    suspend operator fun invoke() : List<NoticeEntity> =
        noticeRepository.getNoticeList()
}