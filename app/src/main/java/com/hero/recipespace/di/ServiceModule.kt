package com.hero.recipespace.di

import com.hero.recipespace.data.chat.service.ChatService
import com.hero.recipespace.data.chat.service.ChatServiceImpl
import com.hero.recipespace.data.message.service.MessageService
import com.hero.recipespace.data.message.service.MessageServiceImpl
import com.hero.recipespace.data.notice.service.NoticeService
import com.hero.recipespace.data.notice.service.NoticeServiceImpl
import com.hero.recipespace.data.rate.service.RateService
import com.hero.recipespace.data.rate.service.RateServiceImpl
import com.hero.recipespace.data.recipe.service.RecipeService
import com.hero.recipespace.data.recipe.service.RecipeServiceImpl
import com.hero.recipespace.data.user.service.UserService
import com.hero.recipespace.data.user.service.UserServiceImpl
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
    fun provideChatService(chatServiceImpl: ChatServiceImpl) : ChatService = chatServiceImpl

    @Provides
    @Singleton
    fun provideMessageService(messageServiceImpl: MessageServiceImpl) : MessageService = messageServiceImpl

    @Provides
    @Singleton
    fun provideNoticeService(noticeServiceImpl: NoticeServiceImpl) : NoticeService = noticeServiceImpl

    @Provides
    @Singleton
    fun provideRateService(rateServiceImpl: RateServiceImpl) : RateService = rateServiceImpl

    @Provides
    @Singleton
    fun provideRecipeService(recipeServiceImpl: RecipeServiceImpl) : RecipeService = recipeServiceImpl

    @Provides
    @Singleton
    fun provideUserService(userServiceImpl: UserServiceImpl) : UserService = userServiceImpl
}