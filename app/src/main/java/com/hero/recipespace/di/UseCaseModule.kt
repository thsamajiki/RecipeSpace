package com.hero.recipespace.di

import com.hero.recipespace.domain.chat.repository.ChatRepository
import com.hero.recipespace.domain.chat.usecase.CreateNewChatRoomUseCase
import com.hero.recipespace.domain.chat.usecase.GetChatUseCase
import com.hero.recipespace.domain.chat.usecase.ObserveChatListUseCase
import com.hero.recipespace.domain.message.repository.MessageRepository
import com.hero.recipespace.domain.message.usecase.GetMessageUseCase
import com.hero.recipespace.domain.message.usecase.ObserveMessageListUseCase
import com.hero.recipespace.domain.message.usecase.ReadMessageUseCase
import com.hero.recipespace.domain.message.usecase.SendMessageUseCase
import com.hero.recipespace.domain.notice.repository.NoticeRepository
import com.hero.recipespace.domain.notice.usecase.GetNoticeUseCase
import com.hero.recipespace.domain.notice.usecase.ObserveNoticeListUseCase
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.domain.rate.usecase.GetRateUseCase
import com.hero.recipespace.domain.rate.usecase.UpdateRateUseCase
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import com.hero.recipespace.domain.recipe.usecase.*
import com.hero.recipespace.domain.user.repository.UserRepository
import com.hero.recipespace.domain.user.usecase.GetUserUseCase
import com.hero.recipespace.domain.user.usecase.ObserveUserListUseCase
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
    fun provideGetChatListUseCase(chatRepository: ChatRepository) = ObserveChatListUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideAddChatUseCase(chatRepository: ChatRepository) = CreateNewChatRoomUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideGetMessageUseCase(messageRepository: MessageRepository) = GetMessageUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideGetMessageListUseCase(messageRepository: MessageRepository) = ObserveMessageListUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideAddMessageUseCase(messageRepository: MessageRepository) = SendMessageUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideReadMessageUseCase(messageRepository: MessageRepository) = ReadMessageUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideGetNoticeUseCase(noticeRepository: NoticeRepository) = GetNoticeUseCase(noticeRepository)

    @Provides
    @Singleton
    fun provideGetNoticeListUseCase(noticeRepository: NoticeRepository) = ObserveNoticeListUseCase(noticeRepository)

    @Provides
    @Singleton
    fun provideGetRateUseCase(rateRepository: RateRepository) = GetRateUseCase(rateRepository)

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
    fun provideAddRecipeUseCase(recipeRepository: RecipeRepository) = PostRecipeUseCase(recipeRepository)

    @Provides
    @Singleton
    fun provideUpdateRecipeUseCase(recipeRepository: RecipeRepository) = UpdateRecipeUseCase(recipeRepository)

    @Provides
    @Singleton
    fun provideRemoveRecipeUseCase(recipeRepository: RecipeRepository) = DeleteRecipeUseCase(recipeRepository)

    @Provides
    @Singleton
    fun provideRefreshRecipeListUseCase(recipeRepository: RecipeRepository) = RefreshRecipeListUseCase(recipeRepository)

    @Provides
    @Singleton
    fun provideGetUserUseCase(userRepository: UserRepository) = GetUserUseCase(userRepository)

    @Provides
    @Singleton
    fun provideGetUserListUseCase(userRepository: UserRepository) = ObserveUserListUseCase(userRepository)
}