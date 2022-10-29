package com.hero.recipespace.listener

import com.google.firebase.firestore.DocumentChange

interface OnChatListChangeListener {
    fun onChatListChange(changeType: DocumentChange.Type?, chatData: ChatData?)
}