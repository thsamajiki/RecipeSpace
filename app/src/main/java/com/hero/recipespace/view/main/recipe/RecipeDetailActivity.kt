package com.hero.recipespace.view.main.recipe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityRecipeDetailBinding
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.util.TimeUtils
import com.hero.recipespace.view.main.chat.ChatActivity
import com.hero.recipespace.view.main.chat.ChatActivity.Companion.EXTRA_OTHER_USER_KEY
import com.hero.recipespace.view.main.recipe.viewmodel.RecipeDetailViewModel
import com.hero.recipespace.view.photoview.PhotoActivity
import com.hero.recipespace.view.post.PostActivity.Companion.EXTRA_RECIPE_ENTITY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRecipeDetailBinding
    private val viewModel by viewModels<RecipeDetailViewModel>()

    companion object {
        private const val RECIPE_KEY = "recipeKey"

        fun getIntent(context: Context, recipeKey: String) =
            Intent(context, RecipeDetailActivity::class.java)
                .putExtra(RECIPE_KEY, recipeKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        val recipeKey =
        setContentView(binding.root)
        bindRecipeUI()

        setupViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        with(viewModel) {

        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivUserProfile.setOnClickListener {
            intentPhoto(viewModel.recipeKey.photoUrl)
        }
        binding.btnQuestion.setOnClickListener {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val myUserKey: String = firebaseUser?.uid.toString()
            if (getRecipe()?.userKey.equals(myUserKey)) {
                Toast.makeText(this, "나와의 대화는 불가능합니다", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(EXTRA_OTHER_USER_KEY, getRecipe()?.userKey)
            startActivity(intent)
        }

        binding.rvRecipeImages.setOnClickListener {
            intentPhoto(getRecipe()?.photoUrl)
        }

        binding.ivOptionMenu.setOnClickListener {
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

    private fun bindRecipeUI() {
        val recipe: RecipeEntity? = getRecipe()
        val requestManager = Glide.with(this)

        if (!TextUtils.isEmpty(recipe?.photoUrl)) {
            requestManager.load(recipe?.photoUrl)
                .into(binding.rvRecipeImages)
        }

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
            binding.ratingBar.rating = recipe.rate
        }
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
        val intent = EditRecipeActivity.getIntent(this, viewModel.recipeKey.key!!)
        startActivity(intent)
    }

    private fun deleteRecipeData() {
//        HashMap<String, Object> editData = new HashMap<>();
//        if (!TextUtils.isEmpty(newProfileUrl)) {
//            editData.put(MyInfoUtil.EXTRA_PROFILE_URL, newProfileUrl);
//        }
    }

    private fun intentPhoto(photoUrl: String?) {
        val intent = PhotoActivity.getIntent(this, photoUrl)
        startActivity(intent)
    }

    override fun onClick(view: View) {
    }
}