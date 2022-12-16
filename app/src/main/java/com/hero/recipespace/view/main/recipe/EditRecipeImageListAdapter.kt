package com.hero.recipespace.view.main.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.databinding.ItemRecipeImageListBinding
import com.hero.recipespace.ext.setImageUrl
import com.hero.recipespace.view.BaseAdapter

class EditRecipeImageListAdapter(
    private val onClick: (String) -> Unit,
    private val onCancelClick: (String) -> Unit
) : BaseAdapter<EditRecipeImageListAdapter.EditRecipeImageViewHolder, String>() {

    private val recipeImageList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditRecipeImageViewHolder {
        val binding = ItemRecipeImageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return EditRecipeImageViewHolder(binding, onClick, onCancelClick)
    }

    override fun onBindViewHolder(holder: EditRecipeImageListAdapter.EditRecipeImageViewHolder, position: Int) {
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

    class EditRecipeImageViewHolder(
        private val binding: ItemRecipeImageListBinding,
        private val onClick: (String) -> Unit,
        private val onCancelClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: String) {
            binding.root.setOnClickListener {
                onClick(image)
            }

            binding.ivRecipeImageCancel.setOnClickListener {
                // TODO: 2022-12-13 X 버튼 클릭하면 이미지가 목록에서 삭제되도록 하기
                onCancelClick(image)
            }

            binding.ivRecipeImage.setImageUrl(image)
            binding.executePendingBindings()
        }
    }
}