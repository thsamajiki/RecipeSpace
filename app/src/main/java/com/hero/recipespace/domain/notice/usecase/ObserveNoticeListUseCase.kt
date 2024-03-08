package com.hero.recipespace.domain.notice.usecase

import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNoticeListUseCase
@Inject
constructor(
    private val noticeRepository: NoticeRepository,
) {
    operator fun invoke(): Flow<List<NoticeEntity>> =
        noticeRepository.getNoticeList()
}
