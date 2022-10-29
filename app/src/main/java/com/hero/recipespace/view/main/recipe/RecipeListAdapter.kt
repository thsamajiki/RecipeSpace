package com.hero.recipespace.view.main.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.hero.recipespace.data.RecipeData
import com.hero.recipespace.databinding.ItemRecipeListBinding
import com.hero.recipespace.listener.OnRecyclerItemClickListener
import com.hero.recipespace.view.BaseAdapter

class RecipeListAdapter(
) : BaseAdapter<RecipeListAdapter.RecipeListViewHolder, RecipeData>() {

    private val recipeList = mutableListOf<RecipeData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        val binding = ItemRecipeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecipeListViewHolder(binding, getOnRecyclerItemClickListener()!!)
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        val recipeItem = recipeList[position]
        holder.setRecipe(recipeItem)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    fun setRecipeList(recipeData: List<RecipeData>) {
        recipeList.clear()
        recipeList.addAll(recipeData)
        notifyDataSetChanged()
    }

    class RecipeListViewHolder(
        private val binding: ItemRecipeListBinding,
        private val itemClickListener: OnRecyclerItemClickListener<RecipeData>
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setRecipe(recipeData: RecipeData) {
            binding.tvPostDate.text = recipeData.postDate.toString()
            binding.tvRecipeDesc.text = recipeData.desc
            binding.tvUserName.text =  recipeData.userName
            binding.ivRecipe.setImageURI(recipeData.photoUrl?.toUri())
            binding.ivUserProfile.setImageURI(recipeData.profileImageUrl?.toUri())
            binding.ratingBar.rating = recipeData.rate

            binding.mcvRatingContainer.setOnClickListener {  }

            itemView.setOnClickListener {
                itemClickListener.onItemClick(absoluteAdapterPosition, it, recipeData)
            }
        }
    }
}