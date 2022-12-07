package com.hero.recipespace.di

import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.domain.chat.usecase.AddChatUseCase
import com.hero.recipespace.domain.chat.usecase.GetChatListUseCase
import com.hero.recipespace.domain.chat.usecase.GetChatUseCase
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.domain.message.usecase.AddMessageUseCase
import com.hero.recipespace.domain.message.usecase.GetMessageListUseCase
import com.hero.recipespace.domain.message.usecase.GetMessageUseCase
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import com.hero.recipespace.domain.notice.usecase.GetNoticeListUseCase
import com.hero.recipespace.domain.notice.usecase.GetNoticeUseCase
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.domain.rate.usecase.AddRateUseCase
import com.hero.recipespace.domain.rate.usecase.GetRateUseCase
import com.hero.recipespace.domain.rate.usecase.UpdateRateUseCase
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import com.hero.recipespace.domain.recipe.usecase.*
import com.hero.recipespace.domain.user.repository.UserRepository
import com.hero.recipespace.domain.user.usecase.GetUserListUseCase
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetChatUseCase(chatRepository: ChatRepository) = GetChatUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideGetChatListUseCase(chatRepository: ChatRepository) = GetChatListUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideAddChatUseCase(chatRepository: ChatRepository) = AddChatUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideGetMessageUseCase(messageRepository: MessageRepository) = GetMessageUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideGetMessageListUseCase(messageRepository: MessageRepository) = GetMessageListUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideAddMessageUseCase(messageRepository: MessageRepository) = AddMessageUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideGetNoticeUseCase(noticeRepository: NoticeRepository) = GetNoticeUseCase(noticeRepository)

    @Provides
    @Singleton
    fun provideGetNoticeListUseCase(noticeRepository: NoticeRepository) = GetNoticeListUseCase(noticeRepository)

    @Provides
    @Singleton
    fun provideGetRateUseCase(rateRepository: RateRepository) = GetRateUseCase(rateRepository)

    @Provides
    @Singleton
    fun provideAddRateUseCase(rateRepository: RateRepository) = AddRateUseCase(rateRepository)

    @Provides
    @Singleton
    fun provideUpdateRateUseCase(rateRepository: RateRepository) = UpdateRateUseCase(rateRepository)

    @Provides
    @Singleton
    fun provideGetRecipeUseCase(recipeRepository: RecipeRepository) = GetRecipeUseCase(recipeRepository)

    @Provides
    @Singleton
    fun provideGetRecipeListUseCase(recipeRepository: RecipeRepository) = ObserveRecipeListUseCase(recipeRepository)

    @Provides
    @Singleton
    fun provideAddRecipeUseCase(recipeRepository: RecipeRepository) = AddRecipeUseCase(recipeRepository)

    @Provides
    @Singleton
    fun provideUpdateRecipeUseCase(recipeRepository: RecipeRepository) = UpdateRecipeUseCase(recipeRepository)

    @Provides
    @Singleton
    fun provideRemoveRecipeUseCase(recipeRepository: RecipeRepository) = RemoveRecipeUseCase(recipeRepository)

    @Provides
    @Singleton
    fun provideGetUserUseCase(userRepository: UserRepository) = GetUserUseCase(userRepository)

    @Provides
    @Singleton
    fun provideGetUserListUseCase(userRepository: UserRepository) = GetUserListUseCase(userRepository)
}