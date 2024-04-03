package com.hero.recipespace.view.main.recipe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hero.recipespace.R
import com.hero.recipespace.databinding.FragmentRecipeListBinding
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.view.main.recipe.ai_chat.AiChatDialogFragment
import com.hero.recipespace.view.main.recipe.viewmodel.RecipeListViewModel
import com.hero.recipespace.view.main.recipe.viewmodel.UiState
import com.hero.recipespace.view.post.PostRecipeActivity
import com.leinardi.android.speeddial.SpeedDialActionItem
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedkeyboardobserver.TedKeyboardObserver
import kotlinx.coroutines.launch

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
        setupListeners()
        setupSearchRecipeListener()
        setupKeyboardObserver()
        setupViewModel()
        setupFragmentResultListener()
    }

    private fun setupView() {
        initRecyclerView(binding.rvRecipeList)
        setupButtonSpeedDial()
    }

    private fun setupListeners() {
        binding.layoutSwipeRefresh.setOnRefreshListener {
            viewModel.refreshRecipeList()
            binding.layoutSwipeRefresh.isRefreshing = false
            binding.editSearchRecipe.clearFocus()
            binding.editSearchRecipe.isGone = true

            binding.layoutSwipeRefresh.isVisible = true
            binding.rvRecipeList.isVisible = true
            binding.tvNoSearchedListFound.isInvisible = true
        }

        binding.btnSpeedDial.setOnActionSelectedListener { actionItem ->
            binding.speedDialOverlayRecipeList.setClickableOverlay(true)
            when (actionItem.id) {
                R.id.post_recipe -> {
                    onPostRecipeButtonClick()
                    true
                }

                R.id.search_recipe -> {
                    onSearchRecipeButtonClick()
                    true
                }

                R.id.ai_chatbot -> {
                    onAiChatbotButtonClick()
                    true
                }

                else -> {
                    false
                }
            }
        }

        binding.layoutRecipeList.setOnClickListener {
            if (binding.layoutSwipeRefresh.isGone) {
                closeKeyboard(binding.root)
            }

            if (binding.editSearchRecipe.hasFocus()) {
                binding.editSearchRecipe.clearFocus()
            }

            binding.layoutSwipeRefresh.isVisible = true
            binding.rvRecipeList.isVisible = true

            binding.btnSpeedDial.isVisible = true

            startAnimationWithHide(
                requireContext(),
                binding.layoutSearchRecipe,
                R.anim.layout_search_hide,
            )
        }

        val nav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        nav?.setOnItemReselectedListener { item ->
            if (item.itemId == R.id.menu_recipe) {
                binding.rvRecipeList.smoothScrollToPosition(0)
            }
        }
    }

    private fun setupSearchRecipeListener() {
        binding.editSearchRecipe.setOnEditorActionListener { searchTextView, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val query = searchTextView?.text.toString()

                    if (query.isEmpty()) {
                        Toast.makeText(requireContext(),
                            "검색어를 입력해주세요.",
                            Toast.LENGTH_SHORT,
                            ).show()
                    } else if (query.length < 2) {
                        Toast.makeText(requireContext(),
                            "최소 2글자를 입력해주세요.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        viewModel.searchRecipe(query)
                        closeKeyboard(binding.root)
                        binding.editSearchRecipe.clearFocus()
                        binding.btnSpeedDial.isVisible = true
                    }

                    Log.i("query", "RecipeListFragment - setupListeners: query - $query")
                }
            }
            true
        }
    }

    private fun setupKeyboardObserver() {
        TedKeyboardObserver(requireActivity())
            .listen { isShow ->
                val isItemEmpty = recipeAdapter.itemCount == 0
                binding.layoutSwipeRefresh.isGone = isShow || isItemEmpty
                binding.tvNoSearchedListFound.isVisible = !isShow && isItemEmpty
                binding.btnSpeedDial.isGone = isShow

                if (!isShow) {
                    binding.editSearchRecipe.clearFocus()
                    binding.btnSpeedDial.isGone = false
                    binding.btnSpeedDial.isVisible = true
                    startAnimationWithHide(
                        requireContext(),
                        binding.layoutSearchRecipe,
                        R.anim.layout_search_hide,
                    )
                }
            }
    }

    private fun setupButtonSpeedDial() {
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
    }

    private fun onSearchRecipeButtonClick() {
        startAnimationWithShow(requireContext(), binding.layoutSearchRecipe, R.anim.layout_search_show)
        binding.rvRecipeList.isGone = true
        binding.btnSpeedDial.close()
        binding.btnSpeedDial.isGone = true
        binding.editSearchRecipe.isVisible = true
        binding.editSearchRecipe.requestFocus()

        openKeyboard()
    }

    private fun openKeyboard() {
        val imm = getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        WindowCompat.getInsetsController(requireActivity().window, binding.editSearchRecipe)
            .show(WindowInsetsCompat.Type.ime())
    }

    private fun closeKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun onAiChatbotButtonClick() {
        val aiChatDialogFragment = AiChatDialogFragment.newInstance()
        aiChatDialogFragment.show(childFragmentManager, AiChatDialogFragment.TAG)
    }

    private fun setupViewModel() {
        with(viewModel) {
            recipeList.observe(viewLifecycleOwner) { recipeList ->
                recipeAdapter.setRecipeList(recipeList)

                Log.d("setupViewModel", "recipeList: $recipeList")

                binding.tvEmptyRecipeListFound.isVisible = recipeList.isEmpty()
                Log.d("setupViewModel", "binding.tvEmptyRecipeListFound.isVisible: ${binding.tvEmptyRecipeListFound.isVisible}")
            }

            lifecycleScope.launch {
                searchRecipeUiState.collect { state ->
                    when (state) {
                        is UiState.GetSearchedRecipeSuccess -> {
                            recipeAdapter.setRecipeList(state.recipeList)
                            if (state.recipeList.isEmpty()) {
                                binding.layoutSwipeRefresh.isGone = true
                                binding.rvRecipeList.isGone = true
                                binding.tvNoSearchedListFound.isVisible = true
                            } else {
                                binding.layoutSwipeRefresh.isVisible = true
                                binding.rvRecipeList.isVisible = true
                                binding.tvNoSearchedListFound.isInvisible = true
                            }

                            Log.i("state.recipeList", "state.recipeList: ${state.recipeList}")
                            Log.i("visibility", "binding.layoutSwipeRefresh: ${binding.layoutSwipeRefresh}")
                            Log.i("visibility", "binding.rvRecipeList: ${binding.rvRecipeList}")
                        }
                        is UiState.GetSearchedRecipeFailed -> {
                            Toast.makeText(
                                requireContext(),
                                "검색 결과를 불러오지 못했습니다.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                        is UiState.Idle -> {}
                    }
                }
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

    // 나오기
    private fun startAnimationWithShow(context: Context, view: View, id: Int) {
        view.visibility = View.VISIBLE  // 애니메이션 전에 뷰를 보이게 한다
        view.startAnimation(AnimationUtils.loadAnimation(context, id)) // 애니메이션 설정&시작
    }

    // 사라지기
    private fun startAnimationWithHide(context: Context, view: View, id: Int) {
        val exitAnim = AnimationUtils.loadAnimation(context, id)    // 애니메이션 설정
        exitAnim.setAnimationListener(HideAnimListener(view))   // 리스너를 통해 애니메이션이 끝나면 뷰를 감춘다
        view.startAnimation(exitAnim) // 애니메이션 시작
    }

    // 애니메이션 리스너
    class HideAnimListener(private val view: View): Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
        }

        // 애니메이션이 끝나면 뷰를 감춘다
        override fun onAnimationEnd(p0: Animation?) {
            if (view.isGone) {
                view.visibility = View.VISIBLE
                view.isGone = false
            } else {
                view.visibility = View.GONE
                view.isGone = true
            }
        }

        override fun onAnimationRepeat(p0: Animation?) {
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = RecipeListFragment()
    }
}
