package com.hero.recipespace.view.post

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.databinding.ActivityPostRecipeBinding
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.view.LoadingState
import com.hero.recipespace.view.photoview.PhotoActivity
import com.hero.recipespace.view.post.viewmodel.PostRecipeUiState
import com.hero.recipespace.view.post.viewmodel.PostRecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostRecipeActivity : AppCompatActivity(),
    View.OnClickListener,
    TextWatcher {

    private lateinit var binding: ActivityPostRecipeBinding

    private lateinit var postRecipeImageListAdapter: PostRecipeImageListAdapter

    private val viewModel by viewModels<PostRecipeViewModel>()

    // TODO: 2022-12-13 rv_recipe_images 리사이클러뷰에 갤러리에서 선택한 이미지들을 넣어주기
    private val openGalleryResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                val recipePhotoPathList = mutableListOf<String>()

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

                postRecipeImageListAdapter.addAll(recipePhotoPathList)

//                recipePhotoPathList = RealPathUtil.getRealPath(this, it.data?.data!!)
//                Glide.with(this).load(recipePhotoPathList).into(binding.ivRecipePhoto)
//                for(i : Int in 0..9) {
//                    recipePhotoUrlList[i] = RealPathUtil.getRealPath(this, it.data?.data!!)
//                    Glide.with(this).load(photoPath).into(binding.ivRecipePhoto)
//                }
                binding.rvRecipeImages.visibility = View.VISIBLE
                if (binding.editContent.text.toString()
                        .isNotEmpty() && recipePhotoPathList.isNotEmpty()
                ) {
                    binding.tvComplete.isEnabled = true
                }
            }
        }

    companion object {
        const val EXTRA_RECIPE_ENTITY = "recipe"
        const val PERMISSION_REQ_CODE = 1010

        fun getIntent(context: Context) =
            Intent(context, PostRecipeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupListeners()
        addTextWatcher()
    }

    private fun setupView() {
        initRecyclerView(binding.rvRecipeImages)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        postRecipeImageListAdapter = PostRecipeImageListAdapter(
            onClick = ::intentPhoto,
            onCancelClick = ::deletePhoto
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = postRecipeImageListAdapter
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
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
                postRecipeUiState.collect { state ->
                    when (state) {
                        is PostRecipeUiState.Success -> {
                            val intent = Intent()
                            intent.putExtra(EXTRA_RECIPE_ENTITY, state.recipe)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                        is PostRecipeUiState.Failed -> {
                            Toast.makeText(
                                this@PostRecipeActivity,
                                "업로드에 실패했습니다. 다시 시도해주세요 ${state.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        PostRecipeUiState.Idle -> {}
                    }
                }
            }
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

        binding.tvComplete.setOnClickListener {
            viewModel.uploadRecipe(binding.editContent.text.toString(), postRecipeImageListAdapter.getRecipeImageList())
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
        return if (ActivityCompat.checkSelfPermission(
                this,
                readPermission
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                writePermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(readPermission, writePermission),
                PERMISSION_REQ_CODE
            )
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
        binding.tvComplete.isEnabled = s.isNotEmpty() && postRecipeImageListAdapter.getRecipeImageList().isNotEmpty()
    }

    private fun intentPhoto(photoUrl: String?) {
        val intent = PhotoActivity.getIntent(this, photoUrl)
        startActivity(intent)
    }

    private fun deletePhoto(position: Int) {
        postRecipeImageListAdapter.delete(position)
    }

    override fun onClick(view: View) {
    }
}