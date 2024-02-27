package com.hero.recipespace.di

import android.content.Context
import androidx.room.Room
import com.hero.recipespace.database.chat.dao.ChatDao
import com.hero.recipespace.database.chat.db.AppChatDatabase
import com.hero.recipespace.database.message.dao.MessageDao
import com.hero.recipespace.database.message.db.AppMessageDatabase
import com.hero.recipespace.database.notice.dao.NoticeDao
import com.hero.recipespace.database.notice.db.AppNoticeDatabase
import com.hero.recipespace.database.rate.dao.RateDao
import com.hero.recipespace.database.rate.db.AppRateDatabase
import com.hero.recipespace.database.recipe.dao.RecipeDao
import com.hero.recipespace.database.recipe.db.AppRecipeDatabase
import com.hero.recipespace.database.user.dao.UserDao
import com.hero.recipespace.database.user.db.AppUserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideChatDatabase(
        @ApplicationContext context: Context,
    ): AppChatDatabase {
        return Room.databaseBuilder(
            context,
            AppChatDatabase::class.java,
            "chat_db",
        ).build()
    }

    @Provides
    fun provideChatDao(database: AppChatDatabase): ChatDao {
        return database.chatDao()
    }

    @Singleton
    @Provides
    fun provideMessageDatabase(
        @ApplicationContext context: Context,
    ): AppMessageDatabase {
        return Room.databaseBuilder(
            context,
            AppMessageDatabase::class.java,
            "message_db",
        ).build()
    }

    @Provides
    fun provideMessageDao(database: AppMessageDatabase): MessageDao {
        return database.messageDao()
    }

    @Singleton
    @Provides
    fun provideNoticeDatabase(
        @ApplicationContext context: Context,
    ): AppNoticeDatabase {
        return Room.databaseBuilder(
            context,
            AppNoticeDatabase::class.java,
            "notice_db",
        ).build()
    }

    @Provides
    fun provideNoticeDao(database: AppNoticeDatabase): NoticeDao {
        return database.noticeDao()
    }

    @Singleton
    @Provides
    fun provideRateDatabase(
        @ApplicationContext context: Context,
    ): AppRateDatabase {
        return Room.databaseBuilder(
            context,
            AppRateDatabase::class.java,
            "rate_db",
        ).build()
    }

    @Provides
    fun provideRateDao(database: AppRateDatabase): RateDao {
        return database.rateDao()
    }

    @Singleton
    @Provides
    fun provideRecipeDatabase(
        @ApplicationContext context: Context,
    ): AppRecipeDatabase {
        return Room.databaseBuilder(
            context,
            AppRecipeDatabase::class.java,
            "recipe_db",
        ).build()
    }

    @Provides
    fun provideRecipeDao(database: AppRecipeDatabase): RecipeDao {
        return database.recipeDao()
    }

    @Singleton
    @Provides
    fun provideUserDatabase(
        @ApplicationContext context: Context,
    ): AppUserDatabase {
        return Room.databaseBuilder(
            context,
            AppUserDatabase::class.java,
            "user_db",
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppUserDatabase): UserDao {
        return database.userDao()
    }
}
