package com.hero.recipespace.view.post

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.databinding.ActivityPostBinding
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.mapper.toEntity
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.OnFileUploadListener
import com.hero.recipespace.listener.Response
import com.hero.recipespace.storage.FirebaseStorageApi
import com.hero.recipespace.util.LoadingProgress
import com.hero.recipespace.util.RealPathUtil
import com.hero.recipespace.view.main.recipe.RecipeDetailActivity
import com.hero.recipespace.view.post.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostActivity : AppCompatActivity(),
    View.OnClickListener,
    TextWatcher,
    OnFileUploadListener {

    private lateinit var binding: ActivityPostBinding
    private var photoPath: String? = null

    private val viewModel by viewModels<PostViewModel>()

    private val photoResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                photoPath = RealPathUtil.getRealPath(this, data.data)
                Glide.with(this).load(photoPath).into(binding.ivRecipePhoto)
                binding.btnPhoto.visibility = View.GONE
                if (binding.editContent.text.toString().isNotEmpty()) {
                    binding.tvComplete.isEnabled = true
                }
            }
        }

    companion object {
        const val EXTRA_RECIPE_ENTITY = "recipe"
        const val PERMISSION_REQ_CODE = 1010
        const val PHOTO_REQ_CODE = 2020

        fun getIntent(context: Context) =
            Intent(context, PostActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupListeners()
        addTextWatcher()
    }

    private fun setupViewModel() {
        with(viewModel) {

        }
    }

    private fun setupListeners() {
       binding.ivBack.setOnClickListener {
           finish()
       }
       binding.btnPhoto.setOnClickListener {
           if (checkStoragePermission()) {
               intentGallery()
           }
       }
        binding.ivRecipePhoto.setOnClickListener {
            if (checkStoragePermission()) {
                intentGallery()
            }
        }
       binding.tvComplete.setOnClickListener {
           uploadImage()
       }
    }

    private fun addTextWatcher() {
        binding.editContent.addTextChangedListener(this)
    }

    private fun intentGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        startActivityForResult(pickIntent, PHOTO_REQ_CODE)
    }

    private fun checkStoragePermission(): Boolean {
        val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        return if (ActivityCompat.checkSelfPermission(this, readPermission) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, writePermission) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(readPermission, writePermission), PERMISSION_REQ_CODE)
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            intentGallery()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        binding.tvComplete.isEnabled = s.isNotEmpty() && !TextUtils.isEmpty(photoPath)
    }

    private fun uploadImage() {
        LoadingProgress.initProgressDialog(this)
        FirebaseStorageApi.getInstance().setOnFileUploadListener(this)
        FirebaseStorageApi.getInstance()
            .uploadImage(FirebaseStorageApi.DEFAULT_IMAGE_PATH, photoPath)
    }

    override suspend fun onFileUploadComplete(isSuccess: Boolean, downloadUrl: String?) {
        if (isSuccess) {
            Toast.makeText(this, "업로드 완료", Toast.LENGTH_SHORT).show()
            val userName: String = FirebaseAuth.getInstance().currentUser?.displayName.toString()
            val profileImageUrl: String = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
            val recipe = RecipeEntity()
            recipe.photoUrl = downloadUrl
            recipe.desc = binding.editContent.text.toString()
            recipe.postDate = Timestamp.now()
            recipe.rate = 0
            recipe.userName = userName
            recipe.profileImageUrl = profileImageUrl
            recipe.userKey = FirebaseAuth.getInstance().currentUser?.uid
            FirebaseData.getInstance().uploadRecipeData(recipe, this)
        }
    }

    override suspend fun onFileUploadProgress(percent: Float) {
        LoadingProgress.setProgress(percent.toInt())
    }

    override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
        LoadingProgress.dismissProgressDialog()
        if (isSuccess) {
            val intent = Intent()
            intent.putExtra(EXTRA_RECIPE_ENTITY, response?.getData()?.toEntity())
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, "업로드에 실패했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(view: View) {
    }
}