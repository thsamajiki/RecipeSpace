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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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

    private lateinit var editRecipeImageListAdapter: EditRecipeImageListAdapter

    private val viewModel by viewModels<EditRecipeViewModel>()

    private var photoPath: String? = null

    private val openGalleryResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                val clipData = it?.data?.clipData
                val clipDataSize = clipData?.itemCount

                if (it.data == null) { // 어떤 이미지도 선택하지 않은 경우
                    Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show()
                } else { // 이미지를 하나라도 선택한 경우
                    if (clipData == null) { //이미지를 하나만 선택한 경우 clipData 가 null 이 올수 있음
                        val photoPath = it?.data?.data!!

                        viewModel.addRecipePhotoList(listOf(photoPath.toString()))
                    } else {
                        clipData.let { clip ->
                            if (clipDataSize != null) {
                                if (clipDataSize > 10) {
                                    Toast.makeText(this, "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG)
                                        .show()
                                } else { // 선택한 이미지가 1장 이상 10장 이하인 경우

                                    // 선택 한 사진수만큼 반복
                                    val photoList = (0 until clipDataSize).map { index ->
                                        val photoPath = clip.getItemAt(index).uri
                                        photoPath.toString()
                                    }

                                    viewModel.addRecipePhotoList(photoList)
                                }
                            }
                        }
                    }
                }

                binding.rvRecipeImages.visibility = View.VISIBLE
                binding.tvTouchHereAndAddPictures.isVisible = viewModel.recipeImageList.value?.size == 0

                if (binding.editContent.text.toString().isNotEmpty() &&
                    viewModel.recipeImageList.value?.isNotEmpty() == true
                ) {
                    binding.tvComplete.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                    binding.tvComplete.isEnabled = true
                }
            }
    }

    companion object {
        private const val PERMISSION_REQ_CODE = 1010
        const val EXTRA_RECIPE_ENTITY = "recipe"

        fun getIntent(context: Context, recipeKey: String) =
            Intent(context, EditRecipeActivity::class.java)
                .putExtra(EditRecipeViewModel.KEY_RECIPE_KEY, recipeKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_recipe)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

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
            onClick = ::onRecipePhotoClick,
            onCancelClick = ::deletePhoto
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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

            recipeImageList.observe(this@EditRecipeActivity) {
                editRecipeImageListAdapter.setRecipeImageList(it)
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
            viewModel.updateRecipe(
                binding.editContent.text.toString()
            )
        }
    }

    private fun addTextWatcher() {
        binding.editContent.addTextChangedListener(this)
    }

    private fun openGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        pickIntent.action = Intent.ACTION_PICK
        openGalleryResultLauncher.launch(pickIntent)
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
            openGallery()
        }
    }

    override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}

    override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}

    override fun afterTextChanged(s: Editable) {
        binding.tvComplete.isEnabled = s.isNotEmpty() && !TextUtils.isEmpty(photoPath)
    }

    private fun onRecipePhotoClick(photoUrl: String?) {
        val intent = PhotoActivity.getIntent(this, photoUrl)
        startActivity(intent)
    }

    private fun deletePhoto(position: Int) {
        viewModel.deletePhoto(position)
    }

    object Result {
        const val RECIPE_KEY = "recipe_key"
    }

    override fun onClick(view: View) {
    }
}