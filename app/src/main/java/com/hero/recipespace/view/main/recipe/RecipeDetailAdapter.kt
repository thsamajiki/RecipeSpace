package com.hero.recipespace.view.main.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.databinding.ItemRecipeDetailImageListBinding
import com.hero.recipespace.ext.setImageUrl
import com.hero.recipespace.view.BaseAdapter

class RecipeDetailAdapter(
    private val onClick: (String) -> Unit
) : BaseAdapter<RecipeDetailAdapter.RecipeImageListViewHolder, String>() {

    private val recipeImageList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeImageListViewHolder {
        val binding = ItemRecipeDetailImageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecipeImageListViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: RecipeImageListViewHolder, position: Int) {
        val imageItem = recipeImageList[position]

        holder.bind(imageItem)
    }

    fun setRecipeImageList(recipeImages: List<String>) {
        recipeImageList.clear()
        recipeImageList.addAll(recipeImages)
        notifyDataSetChanged()
    }

    fun add(recipeImages: List<String>) {
        recipeImageList.addAll(recipeImages)
        notifyDataSetChanged()
    }

    fun replaceItem(recipeImage: String) {
        val index = recipeImageList.indexOf(recipeImage)
        recipeImageList[index] = recipeImage
        notifyItemChanged(index)
    }

    override fun getItemCount(): Int {
        return recipeImageList.size
    }

    class RecipeImageListViewHolder(
        val binding: ItemRecipeDetailImageListBinding,
        private val onClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: String) {
            binding.root.setOnClickListener {
                onClick(image)
            }

            binding.ivRecipeImage.setImageUrl(image)
            binding.executePendingBindings()
        }
    }
}