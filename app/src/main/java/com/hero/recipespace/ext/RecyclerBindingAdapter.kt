package com.hero.recipespace.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.domain.chat.entity.ChatEntity
import com.hero.recipespace.domain.message.entity.MessageEntity
import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.view.main.account.setting.notice.NoticeListAdapter
import com.hero.recipespace.view.main.chat.ChatAdapter
import com.hero.recipespace.view.main.chat.ChatListAdapter
import com.hero.recipespace.view.main.recipe.EditRecipeImageListAdapter
import com.hero.recipespace.view.main.recipe.RecipeListAdapter
import com.hero.recipespace.view.post.PostRecipeImageListAdapter

@BindingAdapter("chatListItems")
fun RecyclerView.setChatListItems(items: List<ChatEntity>?) {
    items ?: return

    val adapter = this.adapter as? ChatListAdapter
    adapter?.setChatList(items)
}

@BindingAdapter("chatItems")
fun RecyclerView.setMessageItems(items: List<MessageEntity>?) {
    items ?: return

    val adapter = this.adapter as? ChatAdapter
    adapter?.setMessageList(items)
}

@BindingAdapter("recipeItems")
fun RecyclerView.setRecipeListItems(items: List<RecipeEntity>?) {
    items ?: return

    val adapter = this.adapter as? RecipeListAdapter
    adapter?.setRecipeList(items)
}

@BindingAdapter("recipeImageItems")
fun RecyclerView.setRecipeImageListItems(items: List<String>?) {
    items ?: return

    val adapter = this.adapter as? PostRecipeImageListAdapter
    adapter?.setRecipeImageList(items)
}

@BindingAdapter("editRecipeImageItems")
fun RecyclerView.setEditRecipeImageListItems(items: List<String>?) {
    items ?: return

    val adapter = this.adapter as? EditRecipeImageListAdapter
    adapter?.setRecipeImageList(items)
}

@BindingAdapter("noticeItems")
fun RecyclerView.setNoticeListItems(items: List<NoticeEntity>?) {
    items ?: return

    val adapter = this.adapter as? NoticeListAdapter
    adapter?.setNoticeList(items)
}

@BindingAdapter("recipeImageItemCount")
fun RecyclerView.setRecipeImageItemCount(items: List<NoticeEntity>?) {
    items ?: return

    val adapter = this.adapter as? NoticeListAdapter
    adapter?.setNoticeList(items)
}