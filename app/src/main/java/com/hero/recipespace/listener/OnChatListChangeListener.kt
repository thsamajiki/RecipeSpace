package com.hero.recipespace.listener

import com.google.firebase.firestore.DocumentChange
import com.hero.recipespace.data.chat.ChatData

interface OnChatListChangeListener {
    fun onChatListChange(changeType: DocumentChange.Type?, chatData: ChatData?)
}