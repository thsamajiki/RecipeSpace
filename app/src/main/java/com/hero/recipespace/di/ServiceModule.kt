package com.hero.recipespace.di

import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.domain.chat.usecase.GetChatListUseCase
import com.hero.recipespace.domain.chat.usecase.GetChatUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideChatService(chatRepository: ChatRepository) = GetChatListUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideMessageService(chatRepository: ChatRepository) = GetChatListUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideNoticeService(chatRepository: ChatRepository) = GetChatListUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideRateService(chatRepository: ChatRepository) = GetChatListUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideRecipeService(chatRepository: ChatRepository) = GetChatUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideUserService(chatRepository: ChatRepository) = GetChatListUseCase(chatRepository)
}