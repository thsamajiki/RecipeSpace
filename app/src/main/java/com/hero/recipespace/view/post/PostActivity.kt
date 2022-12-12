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
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.listener.OnFileUploadListener
import com.hero.recipespace.listener.Response
import com.hero.recipespace.storage.FirebaseStorageApi
import com.hero.recipespace.util.LoadingProgress
import com.hero.recipespace.util.RealPathUtil
import com.hero.recipespace.view.post.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostActivity : AppCompatActivity(),
    View.OnClickListener,
    TextWatcher,
    OnFileUploadListener {

    private lateinit var binding: ActivityPostBinding
    private var photoPath: String? = null

    private lateinit var postAdapter: PostAdapter

    private val viewModel by viewModels<PostViewModel>()

    private val openGalleryResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                photoPath = RealPathUtil.getRealPath(this, it.data?.data!!)
                Glide.with(this).load(photoPath).into(binding.ivRecipePhoto)
//                for(i : Int in 0..10) {
//                    photoPath = RealPathUtil.getRealPath(this, it.data?.data!!)
//                    Glide.with(this).load(photoPath).into(binding.ivRecipePhoto)
//                }
                binding.btnPhoto.visibility = View.GONE
                binding.ivRecipePhoto.visibility = View.GONE
                binding.rvRecipeImages.visibility = View.VISIBLE
                if (binding.editContent.text.toString().isNotEmpty()) {
                    binding.tvComplete.isEnabled = true
                }
            }
        }

    companion object {
        const val EXTRA_RECIPE_ENTITY = "recipe"
        const val PERMISSION_REQ_CODE = 1010

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
               openGallery()
           }
       }
        binding.ivRecipePhoto.setOnClickListener {
            if (checkStoragePermission()) {
                openGallery()
            }
        }
       binding.tvComplete.setOnClickListener {
           uploadImage()
       }
    }

    private fun addTextWatcher() {
        binding.editContent.addTextChangedListener(this)
    }

    private fun openGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        pickIntent.action = Intent.ACTION_GET_CONTENT
        openGalleryResultLauncher.launch(pickIntent)
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
            openGallery()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        binding.tvComplete.isEnabled = s.isNotEmpty() && !TextUtils.isEmpty(photoPath)
    }

    private fun uploadImage() {
        showLoading()
        FirebaseStorageApi.getInstance().setOnFileUploadListener(this)
        FirebaseStorageApi.getInstance()
            .uploadImage(FirebaseStorageApi.DEFAULT_IMAGE_PATH, photoPath)
    }

    override suspend fun onFileUploadComplete(isSuccess: Boolean, downloadUrl: String?) {
        if (isSuccess) {
            Toast.makeText(this, "업로드 완료", Toast.LENGTH_SHORT).show()
            val userName: String = FirebaseAuth.getInstance().currentUser?.displayName.toString()
            val profileImageUrl: String = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
            val recipe = RecipeEntity().copy()
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
        setProgressPercent(percent.toInt())
        LoadingProgress.setProgress(percent.toInt())
    }

    override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
        hideLoading()
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