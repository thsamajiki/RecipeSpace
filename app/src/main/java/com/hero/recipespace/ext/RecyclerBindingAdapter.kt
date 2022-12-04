package com.hero.recipespace.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.message.MessageData
import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.view.main.chat.ChatAdapter
import com.hero.recipespace.view.main.chat.ChatListAdapter
import com.hero.recipespace.view.main.recipe.RecipeListAdapter

@BindingAdapter("chatListItems")
fun RecyclerView.setChatListItems(items: List<ChatData>?) {
    items ?: return

    val adapter = this.adapter as? ChatListAdapter
    adapter?.setMusicList(items)
}

@BindingAdapter("chatItems")
fun RecyclerView.setMessageItems(items: List<MessageData>?) {
    items ?: return

    val adapter = this.adapter as? ChatAdapter
    adapter?.setMusicList(items)
}

@BindingAdapter("recipeItems")
fun RecyclerView.setRecipeListItems(items: List<RecipeData>?) {
    items ?: return

    val adapter = this.adapter as? RecipeListAdapter
    adapter?.setMusicList(items)
}

@BindingAdapter("noticeItems")
fun RecyclerView.setNoticeListItems(items: List<NoticeData>?) {
    items ?: return

    val adapter = this.adapter as? ChatAdapter
    adapter?.setMusicList(items)
}