package com.hero.recipespace.view.main.recipe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.R
import com.hero.recipespace.databinding.FragmentRecipeListBinding
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.view.main.recipe.RatingDialogFragment.Companion.TAG
import com.hero.recipespace.view.main.recipe.viewmodel.RecipeListViewModel
import com.hero.recipespace.view.post.PostActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RecipeListViewModel>()

    private lateinit var recipeListAdapter: RecipeListAdapter

    private val postResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val recipe: RecipeEntity? = it.data?.getParcelableExtra(PostActivity.EXTRA_RECIPE_ENTITY)
            if (recipe != null) {
                recipeListAdapter.add(0, recipe)

                binding.rvRecipe.smoothScrollToPosition(0)
            }
        }
    }

    companion object {
        fun newInstance() = RecipeListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupViewModel()
    }

    private fun setupView() {
        initRecyclerView(binding.rvRecipe)
        binding.btnPost.setOnClickListener {
            intentPostActivity()
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            recipeList.observe(viewLifecycleOwner) { recipeList ->
                recipeListAdapter.setRecipeList(recipeList)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recipeListAdapter = RecipeListAdapter(
            onClick = ::showRecipeDetail,
            onClickRating = ::showRatingDialog
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recipeListAdapter
        }
    }

    private fun showRecipeDetail(recipe: RecipeEntity) {
        // TODO: 2022-12-06 key를 이렇게 넣으면 RecipeDetailActivity에서 데이터를 못가져감. 변경 필요.
        val intent = RecipeDetailActivity.getIntent(requireActivity(), viewModel.recipeKey)
        startActivity(intent)
    }

    private fun showRatingDialog(recipe: RecipeEntity) {
        val ratingDialogFragment = RatingDialogFragment()
        // TODO: 2022-12-06 setOnRatingUploadListener, setRecipeData 안티패턴 변경해보기
        ratingDialogFragment.setOnRatingUploadListener(this)
        ratingDialogFragment.setRecipeData(recipe)
        ratingDialogFragment.show(childFragmentManager, TAG)
    }

    private fun intentPostActivity() {
        val intent = PostActivity.getIntent(requireActivity(), viewModel.recipeKey)
        startActivity(intent)
//        postResultLauncher.launch(intent)
    }

//    override fun onRatingUpload(recipeData: RecipeData?) {
//        val index = recipeDataList.indexOf(recipeData)
//        if (recipeData != null) {
//            recipeDataList[index] = recipeData
//        }
//        recipeListAdapter.notifyItemChanged(index)
//    }
}