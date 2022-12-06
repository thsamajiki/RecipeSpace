package com.hero.recipespace.view.main.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.hero.recipespace.databinding.ItemRecipeListBinding
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.view.BaseAdapter

class RecipeListAdapter(
    private val onClick: (RecipeEntity) -> Unit,
    private val onClickRating: (RecipeEntity) -> Unit
) : BaseAdapter<RecipeListAdapter.RecipeListViewHolder, RecipeEntity>() {

    private val recipeList = mutableListOf<RecipeEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        val binding = ItemRecipeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecipeListViewHolder(binding, onClick, onClickRating)
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        val recipeItem = recipeList[position]
        holder.bind(recipeItem)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    fun setRecipeList(recipeData: List<RecipeEntity>) {
        recipeList.clear()
        recipeList.addAll(recipeData)
        notifyDataSetChanged()
    }

    fun add(position: Int, recipe: RecipeEntity) {
        recipeList.add(position, recipe)
        notifyItemInserted(position)
    }

    class RecipeListViewHolder(
        private val binding: ItemRecipeListBinding,
        private val onClick: (RecipeEntity) -> Unit,
        private val onClickRating: (RecipeEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeEntity) {
//            binding.tvPostDate.text = recipeData.postDate.toString()
//            binding.tvRecipeDesc.text = recipeData.desc
//            binding.tvUserName.text =  recipeData.userName
//            binding.ivRecipe.setImageURI(recipeData.photoUrl?.toUri())
//            binding.ivUserProfile.setImageURI(recipeData.profileImageUrl?.toUri())
//            binding.ratingBar.rating = recipeData.rate

            binding.mcvRatingContainer.setOnClickListener {
                onClickRating(recipe)
            }

            binding.root.setOnClickListener {
                onClick(recipe)
            }

            binding.recipe = recipe
            binding.executePendingBindings()
        }
    }
}