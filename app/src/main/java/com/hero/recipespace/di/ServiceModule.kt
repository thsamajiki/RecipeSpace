package com.hero.recipespace.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

//    @Provides
//    @Singleton
//    fun provideChatService(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore) = ChatService(firebaseAuth, firebaseFirestore)
//
//    @Provides
//    @Singleton
//    fun provideMessageService(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore) = MessageService(firebaseAuth, firebaseFirestore)
//
//    @Provides
//    @Singleton
//    fun provideNoticeService(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore) = NoticeService(firebaseAuth, firebaseFirestore)
//
//    @Provides
//    @Singleton
//    fun provideRateService(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore) = RateService(firebaseAuth, firebaseFirestore)
//
//    @Provides
//    @Singleton
//    fun provideRecipeService(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore) = RecipeService(firebaseAuth, firebaseFirestore)
//
//    @Provides
//    @Singleton
//    fun provideUserService(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore) = UserService(firebaseAuth, firebaseFirestore)
}