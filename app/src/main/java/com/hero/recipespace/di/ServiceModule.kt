package com.hero.recipespace.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.hero.recipespace.data.chat.service.ChatService
import com.hero.recipespace.data.chat.service.ChatServiceImpl
import com.hero.recipespace.data.message.service.MessageService
import com.hero.recipespace.data.message.service.MessageServiceImpl
import com.hero.recipespace.data.notice.service.NoticeService
import com.hero.recipespace.data.notice.service.NoticeServiceImpl
import com.hero.recipespace.data.rate.service.RateService
import com.hero.recipespace.data.recipe.service.RecipeService
import com.hero.recipespace.data.user.service.UserService
import com.hero.recipespace.database.chat.ChatRepositoryImpl
import com.hero.recipespace.domain.chat.repository.ChatRepository
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
    fun provideFirestore() : FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideChatService(chatServiceImpl: ChatServiceImpl) = ChatService()

    @Provides
    @Singleton
    fun provideMessageService(messageServiceImpl: MessageServiceImpl) = MessageService()

    @Provides
    @Singleton
    fun provideNoticeService(noticeServiceImpl: NoticeServiceImpl) = NoticeService(chatRepository)

    @Provides
    @Singleton
    fun provideRateService(chatRepository: ChatRepository) = RateService(chatRepository)

    @Provides
    @Singleton
    fun provideRecipeService(chatRepository: ChatRepository) = RecipeService(chatRepository)

    @Provides
    @Singleton
    fun provideUserService(chatRepository: ChatRepository) = UserService(chatRepository)
}