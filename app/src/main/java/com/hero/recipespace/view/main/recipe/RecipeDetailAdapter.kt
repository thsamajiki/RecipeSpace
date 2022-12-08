package com.hero.recipespace.view.main.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.databinding.ItemRecipeImageListBinding
import com.hero.recipespace.view.BaseAdapter

class RecipeDetailAdapter(
    private val onClick: (String) -> Unit
) : BaseAdapter<RecipeDetailAdapter.RecipeImageListViewHolder, String>() {

    private val recipeImageList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeImageListViewHolder {
        val binding = ItemRecipeImageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
        val binding: ItemRecipeImageListBinding,
        private val onClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: String) {
            // TODO: 2022-12-08 이미지에 대한 어떤 데이터를 추가하고 클릭 리스너 추가해야 한다.
            binding.root.setOnClickListener {
                onClick(image)
            }

            binding.ivRecipeImage
            binding.executePendingBindings()
        }
    }
}