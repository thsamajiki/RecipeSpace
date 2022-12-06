package com.hero.recipespace.view.main.recipe

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
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.databinding.ActivityRecipeDetailBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        setContentView(binding.root)
        setData()

        setupListeners()
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivUserProfile.setOnClickListener {
            intentPhoto(viewModel.recipeData.photoUrl)
        }
        binding.btnQuestion.setOnClickListener {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val myUserKey: String = firebaseUser?.uid.toString()
            if (getRecipeData()?.userKey.equals(myUserKey)) {
                Toast.makeText(this, "나와의 대화는 불가능합니다", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(EXTRA_OTHER_USER_KEY, getRecipeData()?.userKey)
            startActivity(intent)
        }

        binding.ivRecipe.setOnClickListener {
            intentPhoto(getRecipeData()?.photoUrl)
        }

        binding.ivOptionMenu.setOnClickListener {
            val myUserKey = FirebaseAuth.getInstance().currentUser?.uid
            if (getRecipeData()?.userKey.equals(myUserKey)) {
                binding.ivOptionMenu.visibility = View.VISIBLE
                binding.ivOptionMenu.isClickable = true
                showRecipeDetailOptionMenu()
            }
        }
    }

    private fun getRecipeData(): RecipeData? {
        return intent.getParcelableExtra(EXTRA_RECIPE_ENTITY)
    }

    private fun setData() {
        val recipeData: RecipeData? = getRecipeData()
        val requestManager = Glide.with(this)

        if (!TextUtils.isEmpty(recipeData?.photoUrl)) {
            requestManager.load(recipeData?.photoUrl)
                .into(binding.)
        }

        if (!TextUtils.isEmpty(recipeData?.profileImageUrl)) {
            requestManager.load(recipeData?.profileImageUrl)
                .into(binding.ivUserProfile)
        } else {
            requestManager.load(R.drawable.ic_default_user_profile)
                .into(binding.ivUserProfile)
        }

        if (recipeData != null) {
            binding.tvUserName.text = recipeData.userName
            binding.tvRecipeDesc.text = recipeData.desc
            binding.tvPostDate.text = TimeUtils.getInstance().convertTimeFormat(recipeData.postDate?.toDate(), "yy.MM.dd")
            binding.ratingBar.rating = recipeData.rate
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
        val intent = Intent(this, EditRecipeActivity::class.java)
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

    override fun onClick(v: View) {
    }
}