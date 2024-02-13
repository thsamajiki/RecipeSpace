package com.hero.recipespace.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.view.main.account.setting.notice.NoticeListAdapter
import com.hero.recipespace.view.main.chat.ChatAdapter
import com.hero.recipespace.view.main.chat.ChatItem
import com.hero.recipespace.view.main.chat.MessageAdapter
import com.hero.recipespace.view.main.chat.MessageItem
import com.hero.recipespace.view.main.recipe.EditRecipeImageAdapter
import com.hero.recipespace.view.main.recipe.RecipeAdapter
import com.hero.recipespace.view.post.PostRecipeImageAdapter

@BindingAdapter("chatItems")
fun RecyclerView.setChatListItems(items: List<ChatItem>?) {
    items ?: return

    val adapter = this.adapter as? ChatAdapter
    adapter?.setChatList(items)
}

@BindingAdapter("messageItems")
fun RecyclerView.setMessageItems(items: List<MessageItem>?) {
    items ?: return

    val adapter = this.adapter as? MessageAdapter
    adapter?.setMessageList(items)
}

@BindingAdapter("recipeItems")
fun RecyclerView.setRecipeListItems(items: List<RecipeEntity>?) {
    items ?: return

    val adapter = this.adapter as? RecipeAdapter
    adapter?.setRecipeList(items)
}

@BindingAdapter("recipeImageItems")
fun RecyclerView.setRecipeImageListItems(items: List<String>?) {
    items ?: return

    val adapter = this.adapter as? PostRecipeImageAdapter
    adapter?.setRecipeImageList(items)
}

@BindingAdapter("editRecipeImageItems")
fun RecyclerView.setEditRecipeImageListItems(items: List<String>?) {
    items ?: return

    val adapter = this.adapter as? EditRecipeImageAdapter
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