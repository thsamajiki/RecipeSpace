package com.hero.recipespace.view.main.recipe

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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.databinding.ActivityEditRecipeBinding
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.listener.Response
import com.hero.recipespace.storage.FirebaseStorageApi
import com.hero.recipespace.util.LoadingProgress
import com.hero.recipespace.util.MyInfoUtil
import com.hero.recipespace.util.RealPathUtil
import com.hero.recipespace.view.main.recipe.viewmodel.EditRecipeViewModel
import com.hero.recipespace.view.photoview.PhotoActivity
import com.hero.recipespace.view.post.PostRecipeImageListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditRecipeActivity : AppCompatActivity(),
    View.OnClickListener,
    TextWatcher {

    private lateinit var binding: ActivityEditRecipeBinding
    private val recipePhotoPathList: MutableList<String> = mutableListOf()

    private lateinit var editRecipeImageListAdapter: EditRecipeImageListAdapter

    private val viewModel by viewModels<EditRecipeViewModel>()

    private var photoPath: String? = null

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            if (it.data!!.clipData != null) {
                val count = it.data!!.clipData!!.itemCount

                for (index in 0 until count) {
                    // 이미지 담기
                    val photoPath = it.data!!.clipData!!.getItemAt(index).toString()
                    // 이미지 추가
                    recipePhotoPathList.add(photoPath)
                }
            } else {
                val photoPath = it.data!!.data.toString()
                recipePhotoPathList.add(photoPath)
            }
//            photoPath = RealPathUtil.getRealPath(this, it.data?.data!!)
//            Glide.with(this).load(photoPath).into(binding.ivRecipePhoto)
            if (binding.editContent.text.toString().isNotEmpty()) {
                binding.tvComplete.isEnabled = true
            }
        }
    }

    companion object {
        private const val PERMISSION_REQ_CODE = 1010
        private const val RECIPE_KEY = "recipeKey"

        fun getIntent(context: Context, recipeKey: String) =
            Intent(context, EditRecipeActivity::class.java)
                .putExtra(RECIPE_KEY, recipeKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupView()
        addTextWatcher()
        setupViewModel()
        setupListeners()
    }

    private fun setupView() {
        initRecyclerView(binding.rvRecipeImages)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        editRecipeImageListAdapter = EditRecipeImageListAdapter(
            onClick = ::intentPhoto
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = editRecipeImageListAdapter
        }
    }

    private fun setupViewModel() {
        with(viewModel) {

        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvComplete.setOnClickListener {
            uploadImage()
        }
        binding.btnPhoto.setOnClickListener {
            if (checkStoragePermission()) {
                intentGallery()
            }
        }
    }

    private fun addTextWatcher() {
        binding.editContent.addTextChangedListener(this)
    }

    private fun intentGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        pickIntent.action = Intent.ACTION_GET_CONTENT
        openGalleryLauncher.launch(intent)
    }

    private fun checkStoragePermission(): Boolean {
        val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        return if (ActivityCompat.checkSelfPermission(this, readPermission)
            == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, writePermission)
            == PackageManager.PERMISSION_GRANTED
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

    override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}

    override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}

    override fun afterTextChanged(s: Editable) {
        binding.tvComplete.isEnabled = s.isNotEmpty() && !TextUtils.isEmpty(photoPath)
    }

    private fun uploadImage() {
        LoadingProgress.initProgressDialog(this)
        FirebaseStorageApi.getInstance().setOnFileUploadListener(this)
        FirebaseStorageApi.getInstance()
            .uploadImages(FirebaseStorageApi.DEFAULT_IMAGE_PATH, photoPath)
    }

    override suspend fun onFileUploadComplete(isSuccess: Boolean, downloadUrl: String?) {
        if (isSuccess) {
            Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show()
            val userName: String = FirebaseAuth.getInstance().currentUser?.displayName.toString()
            val userKey: String? = FirebaseAuth.getInstance().currentUser?.uid
            val profileImageUrl: String = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
            val recipeData = RecipeData(
                key = ,
                profileImageUrl = profileImageUrl,
                userName = userName,
                userKey = userKey,
                desc = binding.editContent.text.toString(),
                photoUrlList = ,
                postDate = Timestamp.now(),
                rate = ,
                totalRatingCount =
            )
            recipeData.photoUrl = downloadUrl
            recipeData.desc = binding.editContent.text.toString()
            recipeData.postDate = Timestamp.now()
            recipeData.rate = 0
            recipeData.userName = userName
            recipeData.profileImageUrl = profileImageUrl
            recipeData.userKey = MyInfoUtil.getInstance().getKey()
            FirebaseData.getInstance().uploadRecipeData(recipeData, this)
        }
    }

    override suspend fun onFileUploadProgress(percent: Float) {
        setProgressPercent(percent.toInt())
    }

    override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
        hideLoading()
        if (isSuccess) {
            val intent = Intent()
            intent.putExtra(EXTRA_RECIPE_DATA, response?.getData())
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, "레시피 수정에 실패했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun intentPhoto(photoUrl: String?) {
        val intent = PhotoActivity.getIntent(this, photoUrl)
        startActivity(intent)
    }

    override fun onClick(view: View) {
    }
}