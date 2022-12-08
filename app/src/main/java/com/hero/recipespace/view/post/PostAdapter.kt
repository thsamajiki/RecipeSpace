package com.hero.recipespace.view.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.databinding.ItemRecipeImageListBinding
import com.hero.recipespace.view.BaseAdapter

class PostAdapter(
    private val onClick: (String) -> Unit
) : BaseAdapter<PostAdapter.PostRecipeImageViewHolder, String>() {

    private val recipeImageList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostRecipeImageViewHolder {
        val binding = ItemRecipeImageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PostRecipeImageViewHolder(binding, onClick)
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

    override fun getItemCount(): Int {
        return recipeImageList.size
    }

    class PostRecipeImageViewHolder(
        val binding: ItemRecipeImageListBinding,
        private val onClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: String) {

            // TODO: 2022-12-08 이미지 엔티티를 추가하고 클릭 리스너 추가해야 한다.
            binding.root.setOnClickListener {
                onClick(image)
            }

            binding.executePendingBindings()
        }
    }
}