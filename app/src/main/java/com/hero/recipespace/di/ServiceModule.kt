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
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Singleton
    @Binds
    abstract fun provideChatService(chatServiceImpl: ChatServiceImpl) : ChatService

    @Singleton
    @Binds
    abstract fun provideMessageService(messageServiceImpl: MessageServiceImpl) : MessageService

    @Singleton
    @Binds
    abstract fun provideNoticeService(noticeServiceImpl: NoticeServiceImpl) : NoticeService

    @Singleton
    @Binds
    abstract fun provideRateService(rateServiceImpl: RateServiceImpl) : RateService

    @Singleton
    @Binds
    abstract fun provideRecipeService(recipeServiceImpl: RecipeServiceImpl) : RecipeService

    @Singleton
    @Binds
    abstract fun provideUserService(userServiceImpl: UserServiceImpl) : UserService
}