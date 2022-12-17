package com.hero.recipespace.view.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.databinding.ItemRecipeImageListBinding
import com.hero.recipespace.ext.setImageUrl
import com.hero.recipespace.view.BaseAdapter

class PostRecipeImageListAdapter(
    private val onClick: (String) -> Unit,
    private val onCancelClick: (String) -> Unit
) : BaseAdapter<PostRecipeImageListAdapter.PostRecipeImageViewHolder, String>() {

    private val recipeImageList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostRecipeImageViewHolder {
        val binding = ItemRecipeImageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PostRecipeImageViewHolder(binding, onClick, onCancelClick)
    }

    override fun onBindViewHolder(holder: PostRecipeImageViewHolder, position: Int) {
        val image = recipeImageList[position]
        holder.bind(image)
    }

    fun setRecipeImageList(images: List<String>) {
        recipeImageList.clear()
        recipeImageList.addAll(images)
        notifyDataSetChanged()
    }

    fun add(position: Int, images: List<String>) {
        recipeImageList.addAll(position, images)
        notifyItemInserted(position)
    }

    fun replaceItem(image: String) {
        val index = recipeImageList.indexOf(image)
        recipeImageList[index] = image
        notifyItemChanged(index)
    }

    fun delete(position: Int, images: List<String>) {
        recipeImageList.removeAt(position)
        recipeImageList.addAll(images)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return recipeImageList.size
    }

    class PostRecipeImageViewHolder(
        private val binding: ItemRecipeImageListBinding,
        private val onClick: (String) -> Unit,
        private val onCancelClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: String) {
            binding.root.setOnClickListener {
                onClick(image)
            }

            binding.ivRecipeImageCancel.setOnClickListener {
                onCancelClick(image)
            }

            binding.ivRecipeImage.setImageUrl(image)
            binding.executePendingBindings()
        }
    }
}