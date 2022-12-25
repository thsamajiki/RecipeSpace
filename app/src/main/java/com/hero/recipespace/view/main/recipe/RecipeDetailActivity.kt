package com.hero.recipespace.view.main.recipe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityRecipeDetailBinding
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.util.TimeUtils
import com.hero.recipespace.view.main.chat.ChatActivity
import com.hero.recipespace.view.main.recipe.viewmodel.RecipeDetailViewModel
import com.hero.recipespace.view.photoview.PhotoActivity
import com.hero.recipespace.view.post.PostRecipeActivity.Companion.EXTRA_RECIPE_ENTITY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRecipeDetailBinding
    private val viewModel by viewModels<RecipeDetailViewModel>()

    private lateinit var recipeDetailAdapter: RecipeDetailAdapter

    private val updateResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val recipe: RecipeEntity? = it.data?.getParcelableExtra(EXTRA_RECIPE_ENTITY)
                if (recipe != null) {
                    recipeDetailAdapter.add(recipe.photoUrlList.orEmpty())
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
    }

    private fun setupView() {
        val recipe: RecipeEntity? = getRecipe()
        val requestManager = Glide.with(this)

        initRecyclerView(binding.rvRecipeImages)

        if (!TextUtils.isEmpty(recipe?.profileImageUrl)) {
            requestManager.load(recipe?.profileImageUrl)
                .into(binding.ivUserProfile)
        } else {
            requestManager.load(R.drawable.ic_default_user_profile)
                .into(binding.ivUserProfile)
        }

        if (recipe != null) {
            binding.tvUserName.text = recipe.userName
            binding.tvRecipeDesc.text = recipe.desc
            binding.tvPostDate.text = TimeUtils.getInstance().convertTimeFormat(recipe.postDate?.toDate(), "yy.MM.dd")
            binding.ratingBar.rating = recipe.rate ?: 0f
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recipeDetailAdapter = RecipeDetailAdapter(
            onClick = ::intentPhoto
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recipeDetailAdapter
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            recipe.observe(this@RecipeDetailActivity) {
                recipeDetailAdapter.setRecipeImageList(it.photoUrlList.orEmpty())
            }

//            lifecycleScope.launch {
//                recipeDetailUiState.observe(this@RecipeDetailActivity) { state ->
//                    when (state) {
//                        is RecipeDetailUIState.Failed -> TODO()
//                        is RecipeDetailUIState.Success -> TODO()
//                    }
//                }
//            }
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivUserProfile.setOnClickListener {
            intentPhoto(viewModel.recipe.value?.profileImageUrl.orEmpty())
        }

        binding.btnQuestion.setOnClickListener {
            val recipe = getRecipe() ?: return@setOnClickListener
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val myUserKey: String = firebaseUser?.uid.orEmpty()

            if (getRecipe()?.userKey.equals(myUserKey)) {
                Toast.makeText(this, "나와의 대화는 불가능합니다", Toast.LENGTH_SHORT).show()
            }

            val intent = ChatActivity.getIntent(this, recipe.userKey)
            startActivity(intent)
        }

        binding.ratingBar.setOnClickListener {
            val recipe: RecipeEntity? = getRecipe()
            if (recipe != null) {
                showRatingDialog(recipe)
            }
        }

        binding.ivOptionMenu.setOnClickListener {
            Toast.makeText(this, "option_menu intent", Toast.LENGTH_SHORT).show()
            val myUserKey = FirebaseAuth.getInstance().currentUser?.uid
            if (getRecipe()?.userKey.equals(myUserKey)) {
                binding.ivOptionMenu.visibility = View.VISIBLE
                binding.ivOptionMenu.isClickable = true
                showRecipeDetailOptionMenu()
            }
        }
    }

    private fun getRecipe(): RecipeEntity? {
        return intent.getParcelableExtra(EXTRA_RECIPE_ENTITY)
    }

    private fun showRecipeDetailOptionMenu() {
        val popupMenu = PopupMenu(this, binding.ivOptionMenu)
        popupMenu.menuInflater.inflate(R.menu.menu_recipe_detail_actionbar_option, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_recipe_detail_modify -> intentModifyRecipe()
                R.id.menu_recipe_detail_delete -> deleteRecipeData()
            }
            true
        }
    }

    private fun intentModifyRecipe() {
        val intent = EditRecipeActivity.getIntent(this, viewModel.recipeKey)
        updateResultLauncher.launch(intent)
    }

    private fun deleteRecipeData() {
//        HashMap<String, Object> editData = new HashMap<>();
//        if (!TextUtils.isEmpty(newProfileUrl)) {
//            editData.put(MyInfoUtil.EXTRA_PROFILE_URL, newProfileUrl);
//        }
    }

    private fun showRatingDialog(recipe: RecipeEntity) {
        val ratingDialogFragment = RatingDialogFragment.newInstance(recipe)

        ratingDialogFragment.show(supportFragmentManager, RatingDialogFragment.TAG)
    }

    private fun intentPhoto(photoUrl: String?) {
        val intent = PhotoActivity.getIntent(this, photoUrl)
        startActivity(intent)
    }

    override fun onClick(view: View) {
    }
}