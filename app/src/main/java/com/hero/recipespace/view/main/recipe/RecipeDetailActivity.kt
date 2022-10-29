package com.hero.recipespace.view.main.recipe

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.hero.recipespace.R
import com.hero.recipespace.data.RecipeData
import com.hero.recipespace.databinding.ActivityDetailBinding
import com.hero.recipespace.util.MyInfoUtil
import com.hero.recipespace.util.TimeUtils
import com.hero.recipespace.view.login.SignUpActivity
import com.hero.recipespace.view.main.chat.ChatActivity
import com.hero.recipespace.view.photoview.PhotoActivity
import de.hdodenhof.circleimageview.CircleImageView

class RecipeDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivUserProfile.setOnClickListener {
            intentPhoto(getRecipeData()?.profileImageUrl)
        }
        binding.btnQuestion.setOnClickListener {
            val myUserKey: String = MyInfoUtil.getInstance().getKey()
            if (getRecipeData()?.userKey.equals(myUserKey)) {
                Toast.makeText(this, "나와의 대화는 불가능합니다", Toast.LENGTH_SHORT).show()
                return
            }
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(EXTRA_OTHER_USER_KEY, getRecipeData()?.userKey)
            startActivity(intent)
        }

        binding.ivRecipe.setOnClickListener {
            intentPhoto(getRecipeData()?.photoUrl)
        }

        binding.ivOptionMenu.setOnClickListener {
            myUserKey = MyInfoUtil.getInstance().getKey()
            if (getRecipeData().getUserKey().equals(myUserKey)) {
                binding.ivOptionMenu.visibility = View.VISIBLE
                binding.ivOptionMenu.isClickable = true
                showRecipeDetailOptionMenu()
            }
        }
    }

    private fun getRecipeData(): RecipeData? {
        return intent.getParcelableExtra(EXTRA_RECIPE_DATA)
    }

    private fun setData() {
        val recipeData: RecipeData? = getRecipeData()
        val requestManager = Glide.with(this)

        if (!TextUtils.isEmpty(recipeData?.photoUrl)) {
            requestManager.load(recipeData?.photoUrl)
                .into(binding.ivRecipe)
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

    override fun onClick(v: View) {
        when (v.id) {
        }
//        finish()
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
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun deleteRecipeData() {
//        HashMap<String, Object> editData = new HashMap<>();
//        if (!TextUtils.isEmpty(newProfileUrl)) {
//            editData.put(MyInfoUtil.EXTRA_PROFILE_URL, newProfileUrl);
//        }
    }

    private fun intentPhoto(photoUrl: String?) {
        val intent = Intent(this, PhotoActivity::class.java)
        intent.putExtra(EXTRA_PHOTO_URL, photoUrl)
        startActivity(intent)
    }
}