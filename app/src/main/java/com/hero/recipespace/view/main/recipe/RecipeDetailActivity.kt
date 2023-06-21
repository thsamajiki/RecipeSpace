package com.hero.recipespace.view.main.recipe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityRecipeDetailBinding
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.util.TimeUtils
import com.hero.recipespace.util.WLog
import com.hero.recipespace.view.LoadingState
import com.hero.recipespace.view.main.chat.ChatActivity
import com.hero.recipespace.view.main.chat.RecipeChatInfo
import com.hero.recipespace.view.main.recipe.viewmodel.DeleteRecipeUiState
import com.hero.recipespace.view.main.recipe.viewmodel.RecipeDetailViewModel
import com.hero.recipespace.view.photoview.PhotoActivity
import com.hero.recipespace.view.post.PostRecipeActivity.Companion.EXTRA_RECIPE_ENTITY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRecipeDetailBinding
    private val viewModel by viewModels<RecipeDetailViewModel>()

    private lateinit var recipeDetailAdapter: RecipeDetailAdapter

    private val updateResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val recipe: RecipeEntity? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getParcelableExtra(EXTRA_RECIPE_ENTITY, RecipeEntity::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    it.data?.getParcelableExtra(EXTRA_RECIPE_ENTITY)
                }

                if (recipe != null) {
                    recipeDetailAdapter.setRecipeImageList(recipe.photoUrlList.orEmpty())
                    binding.rvRecipeImages.smoothScrollToPosition(0)
                    binding.tvRecipeDesc.text = recipe.desc
                }
            }
        }

    companion object {
        fun getIntent(context: Context, recipeKey: String) =
            Intent(context, RecipeDetailActivity::class.java)
                .putExtra(RecipeDetailViewModel.RECIPE_KEY, recipeKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupView()
        setupViewModel()
        setupListeners()
        setupFragmentResultListener()
    }

    private fun setupView() {
        initRecyclerView(binding.rvRecipeImages)
    }

    private fun bindRecipe(recipe: RecipeEntity) {
        val requestManager = Glide.with(this)
        if (!recipe.profileImageUrl.isNullOrEmpty()) {
            requestManager.load(recipe.profileImageUrl)
                .into(binding.ivUserProfile)
        } else {
            requestManager.load(R.drawable.ic_default_user_profile)
                .into(binding.ivUserProfile)
        }

        binding.tvUserName.text = recipe.userName
        binding.tvRecipeDesc.text = recipe.desc
        binding.tvPostDate.text =
            TimeUtils.getInstance().convertTimeFormat(recipe.postDate?.toDate(), "yy.MM.dd")
        binding.ratingBar.rating = recipe.rate ?: 0f

        val myUserKey = FirebaseAuth.getInstance().currentUser?.uid
        if (recipe.userKey == myUserKey) {
            binding.ivOptionMenu.visibility = View.VISIBLE
            binding.ivOptionMenu.isClickable = true
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recipeDetailAdapter = RecipeDetailAdapter(
            onClick = ::onRecipePhotoClick
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recipeDetailAdapter
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            recipe.observe(this@RecipeDetailActivity) { recipe ->
                recipeDetailAdapter.setRecipeImageList(recipe.photoUrlList.orEmpty())

                bindRecipe(recipe)
            }

            lifecycleScope.launch {
                loadingState.collect { state ->
                    when (state) {
                        LoadingState.Hidden -> hideLoading()
                        LoadingState.Idle -> {}
                        LoadingState.Loading -> showLoading()
                        is LoadingState.Progress -> setProgressPercent(state.value)
                    }
                }
            }

            lifecycleScope.launch {
                deleteRecipeUiState.collect { state ->
                    when (state) {
                        is DeleteRecipeUiState.Success -> {
                            Toast.makeText(
                                this@RecipeDetailActivity,
                                "레시피를 삭제했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        is DeleteRecipeUiState.Failed -> {
                            Toast.makeText(
                                this@RecipeDetailActivity,
                                "레시피 삭제에 실패했습니다. 다시 시도해주세요 ${state.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is DeleteRecipeUiState.Idle -> {
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivUserProfile.setOnClickListener {
            onRecipePhotoClick(viewModel.recipe.value?.profileImageUrl.orEmpty())
        }

        binding.btnQuestion.setOnClickListener {
            val recipe = viewModel.recipe.value ?: return@setOnClickListener
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val myUserKey: String = firebaseUser?.uid.orEmpty()

            if (viewModel.recipe.value?.userKey.equals(myUserKey)) {
                Toast.makeText(this, "나와의 대화는 불가능합니다", Toast.LENGTH_SHORT).show()
            } else {
                val intent = ChatActivity.getIntent(
                    this,
                    recipeChatInfo = RecipeChatInfo(
                        userKey = recipe.userKey,
                        userName = recipe.userName,
                        userProfileImageUrl = recipe.profileImageUrl,
                        recipeKey = recipe.key
                    )
                )
                startActivity(intent)
            }
        }

        binding.ratingContainer.setOnClickListener {
            val recipe = viewModel.recipe.value ?: return@setOnClickListener
            WLog.d("setupListeners: recipe : " + recipe.key)

            showRatingDialog(recipe)
        }

        binding.ivOptionMenu.setOnClickListener {
            showRecipeDetailOptionMenu()
        }
    }

    private fun setupFragmentResultListener() {
        // FragmentResult - 데이터를 수신하기 위한 부분
        supportFragmentManager.setFragmentResultListener(
            RatingDialogFragment.TAG,
            this
        ) { _: String, result: Bundle ->
            // 데이터를 수신
            val recipe = result.getParcelable<RecipeEntity>(RatingDialogFragment.Result.KEY_RECIPE)

            if (recipe != null) {
                val rate = recipe.rate!!
                binding.ratingBar.rating = rate
            }
        }
    }

    private fun showRecipeDetailOptionMenu() {
        val popupMenu = PopupMenu(this, binding.ivOptionMenu)
        popupMenu.menuInflater.inflate(R.menu.menu_recipe_detail_actionbar_option, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_recipe_detail_modify -> onModifyRecipeMenuClick()
                R.id.menu_recipe_detail_delete -> onDeleteRecipeMenuClick()
            }
            true
        }
        popupMenu.show()
    }

    private fun onModifyRecipeMenuClick() {
        val intent = EditRecipeActivity.getIntent(this, viewModel.recipeKey)
        updateResultLauncher.launch(intent)
    }

    private fun onDeleteRecipeMenuClick() {
        val title = "레시피 삭제"
        val message = "레시피를 삭제하시겠습니까?"
        val positiveText = "예"
        val negativeText = "아니오"

        MaterialAlertDialogBuilder(this).setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText) { _, _ -> deleteRecipeData() }
            .setNegativeButton(negativeText) { _, _ -> }
            .create()
            .show()
    }

    private fun deleteRecipeData() {
        val recipe = viewModel.recipe.value
        if (recipe != null) {
            viewModel.deleteRecipe(recipe)
        }
    }

    private fun showRatingDialog(recipe: RecipeEntity) {
        val ratingDialogFragment = RatingDialogFragment.newInstance(recipe.key)

        ratingDialogFragment.show(supportFragmentManager, RatingDialogFragment.TAG)
    }

    private fun onRecipePhotoClick(photoUrl: String?) {
        val intent = PhotoActivity.getIntent(this, photoUrl)
        startActivity(intent)
    }

    override fun onClick(view: View) {
    }
}