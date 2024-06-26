package com.hero.recipespace.view.main.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.databinding.ItemRecipeImageBinding
import com.hero.recipespace.ext.setImageUrl
import com.hero.recipespace.view.BaseAdapter

class EditRecipeImageAdapter(
    private val onClick: (String) -> Unit,
    private val onCancelClick: (Int) -> Unit,
) : BaseAdapter<EditRecipeImageAdapter.EditRecipeImageViewHolder, String>() {
    private val recipeImageList = mutableListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EditRecipeImageViewHolder {
        val binding =
            ItemRecipeImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )

        return EditRecipeImageViewHolder(binding, onClick, onCancelClick)
    }

    override fun onBindViewHolder(
        holder: EditRecipeImageViewHolder,
        position: Int,
    ) {
        val image = recipeImageList[position]
        holder.bind(image)
    }

    fun getRecipeImageList(): List<String> {
        // 방어적 복사
        return recipeImageList.toList()
    }

    fun setRecipeImageList(images: List<String>) {
        recipeImageList.clear()
        recipeImageList.addAll(images)
        notifyDataSetChanged()
    }

    fun addAll(images: List<String>) {
        val oldSize = recipeImageList.size
        recipeImageList.addAll(images)
        notifyItemRangeInserted(oldSize, images.size)
    }

    fun replaceItem(image: String) {
        val index = recipeImageList.indexOf(image)
        recipeImageList[index] = image
        notifyItemChanged(index)
    }

    fun delete(position: Int) {
        recipeImageList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return recipeImageList.size
    }

    class EditRecipeImageViewHolder(
        private val binding: ItemRecipeImageBinding,
        private val onClick: (String) -> Unit,
        private val onCancelClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: String) {
            binding.root.setOnClickListener {
                onClick(image)
            }

            binding.ivRecipeImageCancel.setOnClickListener {
                onCancelClick(absoluteAdapterPosition)
            }

            binding.ivRecipeImage.setImageUrl(image)
            binding.executePendingBindings()
        }
    }
}
