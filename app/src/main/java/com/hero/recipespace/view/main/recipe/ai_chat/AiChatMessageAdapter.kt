package com.hero.recipespace.view.main.recipe.ai_chat

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.hero.recipespace.R

class AiChatMessageAdapter(
    private val context: Context,
    private val messageList: MutableList<List<String>>
): RecyclerView.Adapter<AiChatMessageAdapter.AiChatMessageViewHolder>() {
    inner class AiChatMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message: TextView = view.findViewById(R.id.tvMessage)
        val image: ShapeableImageView = view.findViewById(R.id.ivGemini)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiChatMessageAdapter.AiChatMessageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ai_chat_message, parent, false)

        return AiChatMessageViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AiChatMessageAdapter.AiChatMessageViewHolder, position: Int) {
        val item = messageList[position]
        if (messageList[position].contains("당신")) {
//            holder.image.isGone = true
            holder.image.isVisible = false
            holder.image.isInvisible = true
        } else {
//            holder.image.isGone = false
            holder.image.isVisible = true
            holder.image.isInvisible = false
        }
        holder.message.text = "${item[0]}: ${item[1]}"
    }

    override fun getItemCount(): Int {
        return messageList.count()
    }
}
