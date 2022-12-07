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
        setupFragmentResultListener()
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

    private fun setupFragmentResultListener() {
        // FragmentResult - 데이터를 수신하기 위한 부분
        childFragmentManager.setFragmentResultListener(
            RatingDialogFragment.TAG,
            viewLifecycleOwner) {
            _: String, result: Bundle ->
            // 데이터를 수신하자.
            val recipe = result.getParcelable<RecipeEntity>(RatingDialogFragment.Result.KEY_RECIPE)

            if (recipe != null) {
                recipeListAdapter.replaceItem(recipe)
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
        val intent = RecipeDetailActivity.getIntent(requireActivity(), recipe.key.orEmpty())
        startActivity(intent)
    }

    private fun showRatingDialog(recipe: RecipeEntity) {
        val ratingDialogFragment = RatingDialogFragment.newInstance(recipe)

        ratingDialogFragment.show(childFragmentManager, RatingDialogFragment.TAG)
    }

    private fun intentPostActivity() {
        val intent = PostActivity.getIntent(requireActivity())
        postResultLauncher.launch(intent)
    }
}