package com.hero.recipespace.domain.chat.request

data class AddChatRequest(
    val otherUserKey: String,
    val recipeKey: String,
    val message: String,
)
