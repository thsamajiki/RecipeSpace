package com.hero.recipespace.di

import com.hero.recipespace.database.chat.ChatRepositoryImpl
import com.hero.recipespace.database.message.MessageRepositoryImpl
import com.hero.recipespace.database.notice.NoticeRepositoryImpl
import com.hero.recipespace.database.rate.RateRepositoryImpl
import com.hero.recipespace.database.recipe.RecipeRepositoryImpl
import com.hero.recipespace.database.user.UserRepositoryImpl
import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import com.hero.recipespace.domain.user.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository

    @Singleton
    @Binds
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

    @Singleton
    @Binds
    abstract fun bindNoticeRepository(
        noticeRepositoryImpl: NoticeRepositoryImpl
    ): NoticeRepository

    @Singleton
    @Binds
    abstract fun bindRateRepository(
        rateRepositoryImpl: RateRepositoryImpl
    ): RateRepository

    @Singleton
    @Binds
    abstract fun bindRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository

    @Singleton
    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}