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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityEditRecipeBinding
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.view.LoadingState
import com.hero.recipespace.view.main.recipe.viewmodel.EditRecipeUiState
import com.hero.recipespace.view.main.recipe.viewmodel.EditRecipeViewModel
import com.hero.recipespace.view.photoview.PhotoActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        const val EXTRA_RECIPE_ENTITY = "recipe"
        private const val RECIPE_KEY = "recipeKey"

        fun getIntent(context: Context, recipeKey: String) =
            Intent(context, EditRecipeActivity::class.java)
                .putExtra(RECIPE_KEY, recipeKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_recipe)

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
            onClick = ::intentPhoto,
            onCancelClick = ::deletePhoto
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = editRecipeImageListAdapter
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
                editRecipeUiState.collect { state ->
                    when (state) {
                        is EditRecipeUiState.Success -> {
                            val intent = Intent()
                            intent.putExtra(EXTRA_RECIPE_ENTITY, state.recipe)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                        is EditRecipeUiState.Failed -> {
                            Toast.makeText(
                                this@EditRecipeActivity,
                                "업로드에 실패했습니다. 다시 시도해주세요 ${state.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        EditRecipeUiState.Idle -> {}
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvComplete.setOnClickListener {
            viewModel.updateRecipe(binding.editContent.text.toString(), recipePhotoPathList)
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

    private fun intentPhoto(photoUrl: String?) {
        val intent = PhotoActivity.getIntent(this, photoUrl)
        startActivity(intent)
    }

    private fun deletePhoto(position: Int) {
        // TODO: 2022-12-16 기존의 RecipeData 에 있는 이미지 목록(RecyclerView)에서 원하는 이미지를 제외하는 것 구현하기
        editRecipeImageListAdapter.delete(position, recipePhotoPathList)
    }

    object Result {
        const val RECIPE_KEY = "recipe_key"
    }

    override fun onClick(view: View) {
    }
}