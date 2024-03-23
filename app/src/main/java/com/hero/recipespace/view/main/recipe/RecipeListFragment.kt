package com.hero.recipespace.view.main.recipe

import android.app.Activity
import android.content.Intent
import android.os.Build
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hero.recipespace.R
import com.hero.recipespace.databinding.FragmentRecipeListBinding
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.view.main.recipe.viewmodel.RecipeListViewModel
import com.hero.recipespace.view.post.PostRecipeActivity
import com.leinardi.android.speeddial.SpeedDialActionItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment() {
    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RecipeListViewModel>()

    private lateinit var recipeAdapter: RecipeAdapter

    private val postResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val recipe: RecipeEntity? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        it.data?.getParcelableExtra(
                            PostRecipeActivity.EXTRA_RECIPE_ENTITY,
                            RecipeEntity::class.java,
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        it.data?.getParcelableExtra(PostRecipeActivity.EXTRA_RECIPE_ENTITY)
                    }

                if (recipe != null) {
                    recipeAdapter.add(0, recipe)
                    binding.rvRecipeList.smoothScrollToPosition(0)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_recipe_list,
                container,
                false,
            )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupViewModel()
        binding.layoutSwipeRefresh.setOnRefreshListener {
            viewModel.refreshRecipeList()
            binding.layoutSwipeRefresh.isRefreshing = false
        }
        setupFragmentResultListener()
    }

    private fun setupView() {
        initRecyclerView(binding.rvRecipeList)
        binding.btnSpeedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.post_recipe, R.drawable.ic_pen)
                .create()
        )
        binding.btnSpeedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.search_recipe, R.drawable.ic_search_24)
                .create()
        )
        binding.btnSpeedDial.addActionItem(
            SpeedDialActionItem.Builder(R.id.ai_chatbot, R.drawable.ic_ai_chat)
                .create()
        )
        binding.btnSpeedDial.setOnActionSelectedListener {
            binding.speedDialOverlayRecipeList.setClickableOverlay(true)
            when (it.id) {
                R.id.post_recipe -> {
                    onPostRecipeButtonClick()
                    true
                }
                R.id.search_recipe -> {
                    true
                }
                R.id.ai_chatbot -> {
                    true
                }
                else -> {
                    false
                }
            }
        }

        val nav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        nav?.setOnItemReselectedListener { item ->
            if (item.itemId == R.id.menu_recipe) {
                binding.rvRecipeList.smoothScrollToPosition(0)
            }
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            recipeList.observe(viewLifecycleOwner) { recipeList ->
                recipeAdapter.setRecipeList(recipeList)
            }
        }
    }

    private fun setupFragmentResultListener() {
        // FragmentResult - 데이터를 수신하기 위한 부분
        childFragmentManager.setFragmentResultListener(
            RatingDialogFragment.TAG,
            viewLifecycleOwner,
        ) { _: String, result: Bundle ->
            // 데이터를 수신하자.

            val recipe =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.getParcelable(
                        RatingDialogFragment.Result.KEY_RECIPE,
                        RecipeEntity::class.java,
                    )
                } else {
                    @Suppress("DEPRECATION")
                    result.getParcelable(RatingDialogFragment.Result.KEY_RECIPE)
                }

            if (recipe != null) {
                recipeAdapter.replaceItem(recipe)
            }
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recipeAdapter =
            RecipeAdapter(
                onClick = ::showRecipeDetail,
                onClickRating = ::showRatingDialog,
            )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recipeAdapter
        }
    }

    private fun showRecipeDetail(recipe: RecipeEntity) {
        val intent = RecipeDetailActivity.getIntent(requireActivity(), recipe.key)
        startActivity(intent)
    }

    private fun showRatingDialog(recipe: RecipeEntity) {
        val ratingDialogFragment = RatingDialogFragment.newInstance(recipe.key)

        ratingDialogFragment.show(childFragmentManager, RatingDialogFragment.TAG)
    }

    private fun onPostRecipeButtonClick() {
        val intent = PostRecipeActivity.getIntent(requireActivity())
        postResultLauncher.launch(intent)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = RecipeListFragment()
    }
}
