package com.hero.recipespace.database.notice

import com.hero.recipespace.data.notice.remote.NoticeRemoteDataSource
import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.domain.notice.mapper.toEntity
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoticeRepositoryImpl(
    private val noticeRemoteDataSource: NoticeRemoteDataSource
) : NoticeRepository {
    override fun getNotice(noticeKey: String): Flow<NoticeEntity> {
        return noticeRemoteDataSource.getData(noticeKey)
            .map {
                it.toEntity()
            }
    }

    override fun getNoticeList(): Flow<List<NoticeEntity>> {
        return noticeRemoteDataSource.getDataList()
            .map { it ->
                it.map {
                    it.toEntity()
                }
            }
    }
}