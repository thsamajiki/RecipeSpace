package com.hero.recipespace.database.notice

import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.data.notice.remote.NoticeRemoteDataSource
import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.domain.notice.mapper.toEntity
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val noticeRemoteDataSource: NoticeRemoteDataSource
) : NoticeRepository {
    override suspend fun getNotice(noticeKey: String): NoticeEntity {
        return noticeRemoteDataSource.getData(noticeKey).toEntity()
    }

    override suspend fun getNoticeList(): List<NoticeEntity> {
        return noticeRemoteDataSource.getDataList()
            .map {
                it.toEntity()
            }
    }

    private fun getEntities(data: List<NoticeData>): List<NoticeEntity> {
        val result = mutableListOf<NoticeEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}