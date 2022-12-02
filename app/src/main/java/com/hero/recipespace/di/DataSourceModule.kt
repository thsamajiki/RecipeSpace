package com.hero.recipespace.di

import com.hero.recipespace.data.chat.local.ChatLocalDataSource
import com.hero.recipespace.data.chat.local.ChatLocalDataSourceImpl
import com.hero.recipespace.data.chat.remote.ChatRemoteDataSource
import com.hero.recipespace.data.chat.remote.ChatRemoteDataSourceImpl
import com.hero.recipespace.data.message.local.MessageLocalDataSource
import com.hero.recipespace.data.message.local.MessageLocalDataSourceImpl
import com.hero.recipespace.data.message.remote.MessageRemoteDataSource
import com.hero.recipespace.data.message.remote.MessageRemoteDataSourceImpl
import com.hero.recipespace.data.notice.remote.NoticeRemoteDataSource
import com.hero.recipespace.data.notice.remote.NoticeRemoteDataSourceImpl
import com.hero.recipespace.data.rate.local.RateLocalDataSource
import com.hero.recipespace.data.rate.local.RateLocalDataSourceImpl
import com.hero.recipespace.data.rate.remote.RateRemoteDataSource
import com.hero.recipespace.data.rate.remote.RateRemoteDataSourceImpl
import com.hero.recipespace.data.recipe.local.RecipeLocalDataSource
import com.hero.recipespace.data.recipe.local.RecipeLocalDataSourceImpl
import com.hero.recipespace.data.recipe.remote.RecipeRemoteDataSource
import com.hero.recipespace.data.recipe.remote.RecipeRemoteDataSourceImpl
import com.hero.recipespace.data.user.local.UserLocalDataSource
import com.hero.recipespace.data.user.local.UserLocalDataSourceImpl
import com.hero.recipespace.data.user.remote.UserRemoteDataSource
import com.hero.recipespace.data.user.remote.UserRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindChatRemoteDataSource(
        chatRemoteDataSourceImpl: ChatRemoteDataSourceImpl
    ) : ChatRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindChatLocalDataSource(
        chatLocalDataSourceImpl: ChatLocalDataSourceImpl
    ) : ChatLocalDataSource

    @Singleton
    @Binds
    abstract fun bindMessageRemoteDataSource(
        messageRemoteDataSourceImpl: MessageRemoteDataSourceImpl
    ) : MessageRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindMessageLocalDataSource(
        messageLocalDataSourceImpl: MessageLocalDataSourceImpl
    ) : MessageLocalDataSource

    @Singleton
    @Binds
    abstract fun bindNoticeRemoteDataSource(
        noticeRemoteDataSourceImpl: NoticeRemoteDataSourceImpl
    ) : NoticeRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindRateRemoteDataSource(
        rateRemoteDataSourceImpl: RateRemoteDataSourceImpl
    ) : RateRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindRateLocalDataSource(
        rateLocalDataSourceImpl: RateLocalDataSourceImpl
    ) : RateLocalDataSource

    @Singleton
    @Binds
    abstract fun bindRecipeRemoteDataSource(
        recipeRemoteDataSourceImpl: RecipeRemoteDataSourceImpl
    ) : RecipeRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindRecipeLocalDataSource(
        recipeLocalDataSourceImpl: RecipeLocalDataSourceImpl
    ) : RecipeLocalDataSource

    @Singleton
    @Binds
    abstract fun bindUserRemoteDataSource(
        userRemoteDataSourceImpl: UserRemoteDataSourceImpl
    ) : UserRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindUserLocalDataSource(
        userLocalDataSourceImpl: UserLocalDataSourceImpl
    ) : UserLocalDataSource
}