package com.hero.recipespace.view.post

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityPostRecipeBinding
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.util.PermissionUtil
import com.hero.recipespace.view.LoadingState
import com.hero.recipespace.view.photoview.PhotoActivity
import com.hero.recipespace.view.post.viewmodel.PostRecipeUiState
import com.hero.recipespace.view.post.viewmodel.PostRecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostRecipeActivity : AppCompatActivity(), View.OnClickListener, TextWatcher {
    private lateinit var binding: ActivityPostRecipeBinding
    private lateinit var postRecipeImageAdapter: PostRecipeImageAdapter

    private var bottomSheet = BottomSheetPostRecipeImage()

    private val viewModel by viewModels<PostRecipeViewModel>()

    private val openGalleryResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                val clipData = it?.data?.clipData
                val clipDataSize = clipData?.itemCount

                if (it.data == null) { // 어떤 이미지도 선택하지 않은 경우
                    Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show()
                } else { // 이미지를 하나라도 선택한 경우
                    if (clipData == null) { // 이미지를 하나만 선택한 경우 clipData 가 null 이 올수 있음
                        val photoPath = it?.data?.data!!

                        viewModel.addRecipePhotoList(listOf(photoPath.toString()))
                    } else {
                        clipData.let { clip ->
                            if (clipDataSize != null) {
                                if (clipDataSize > 10) {
                                    Toast.makeText(this, "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG)
                                        .show()
                                } else { // 선택한 이미지가 1장 이상 10장 이하인 경우

                                    // 선택한 사진 수만큼 반복
                                    val photoList =
                                        (0 until clipDataSize).map { index ->
                                            val photoPath =
                                                clip.getItemAt(index).uri
                                            photoPath.toString()
                                        }

                                    viewModel.addRecipePhotoList(photoList)
                                }
                            }
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_recipe)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupView()
        setupViewModel()
        setupListeners()
        addTextWatcher()
    }

    private fun setupView() {
        initRecyclerView(binding.rvRecipeImageList)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        postRecipeImageAdapter =
            PostRecipeImageAdapter(
                onClick = ::onRecipePhotoClick,
                onCancelClick = ::deletePhoto,
            )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = postRecipeImageAdapter
            val spaceDecoration = HorizontalSpaceItemDecoration(1)
            removeItemDecoration(
                object :
                    DividerItemDecoration(this@PostRecipeActivity, HORIZONTAL) {
                },
            )
            addItemDecoration(spaceDecoration)
        }
    }

    // RecyclerView Item 간 간격 조정하기 위한 클래스
    inner class HorizontalSpaceItemDecoration(private val horizontalSpaceWidth: Int) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            val position = parent.getChildAdapterPosition(view)
            val count = state.itemCount

            when (position) {
                0 -> {
                    outRect.left = horizontalSpaceWidth
                }

                count - 1 -> {
                    outRect.right = horizontalSpaceWidth
                }

                else -> {
                    outRect.left = horizontalSpaceWidth
                    outRect.right = horizontalSpaceWidth
                }
            }
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
                                Toast.LENGTH_SHORT,
                            ).show()
                        }

                        PostRecipeUiState.Idle -> {}
                    }
                }
            }

            recipeImageList.observe(this@PostRecipeActivity) {
                postRecipeImageAdapter.setRecipeImageList(it)
                Log.d(
                    "setupViewModel",
                    "setupViewModel: ${postRecipeImageAdapter.getRecipeImageList()}",
                )

                // '카메라를 눌러 ~' 문구가 없으려면
                // => 이미지 개수가 최소 1개 이상

                if (postRecipeImageAdapter.getRecipeImageList().isNotEmpty()) {
                    binding.rvRecipeImageList.visibility = View.VISIBLE
                    binding.tvTouchHereAndAddPictures.visibility = View.INVISIBLE
                } else {
                    binding.rvRecipeImageList.visibility = View.GONE
                    binding.tvTouchHereAndAddPictures.visibility = View.VISIBLE
                }

                if (binding.editContent.text?.isNotEmpty() == true && recipeImageList.value?.isNotEmpty() == true) {
                    binding.tvComplete.setTextColor(
                        ContextCompat.getColor(
                            this@PostRecipeActivity,
                            R.color.colorPrimaryDark,
                        ),
                    )
                    binding.tvComplete.isEnabled = true
                } else {
                    binding.tvComplete.setTextColor(
                        ContextCompat.getColor(
                            this@PostRecipeActivity,
                            R.color.normal_gray,
                        ),
                    )
                    binding.tvComplete.isEnabled = false
                }
            }
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnPhoto.setOnClickListener {
            openOptionMenuBottomSheet()
        }

        binding.tvComplete.setOnClickListener {
            viewModel.uploadRecipe(
                binding.editContent.text.toString(),
                postRecipeImageAdapter.getRecipeImageList(),
            )
        }

        supportFragmentManager.setFragmentResultListener(
            BottomSheetPostRecipeImage.TAG,
            this@PostRecipeActivity,
        ) { _: String, result: Bundle ->
            val action = result.getString(BottomSheetPostRecipeImage.RESULT_ACTION)
            if (action == BottomSheetPostRecipeImage.ACTION_POST_IMAGE) {
                val photoList =
                    result.getStringArrayList(BottomSheetPostRecipeImage.PHOTO_LIST).orEmpty()
                        .toList()
                viewModel.addRecipePhotoList(photoList)

                Log.d("setFragmentResultListener", "setupListeners: $photoList")
                postRecipeImageAdapter.setRecipeImageList(photoList)

                binding.rvRecipeImageList.visibility = View.VISIBLE
                binding.tvTouchHereAndAddPictures.visibility = View.INVISIBLE
                if (binding.editContent.text.toString().isNotEmpty() &&
                    viewModel.recipeImageList.value?.isNotEmpty() == true
                ) {
                    binding.rvRecipeImageList.visibility = View.VISIBLE
                    binding.tvTouchHereAndAddPictures.visibility = View.INVISIBLE
                    binding.tvComplete.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPrimaryDark,
                        ),
                    )
                    binding.tvComplete.isEnabled = true
                } else {
                    binding.tvComplete.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.normal_gray,
                        ),
                    )
                    binding.tvComplete.isEnabled = false
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val photoList = intent?.getStringArrayListExtra(PhotoResultActivity.PHOTO_LIST)
        Log.d("onNewIntent", "photoList: $photoList")
        viewModel.addRecipePhotoList(photoList!!.toList())

        bottomSheet.dismiss()
    }

    private fun addTextWatcher() {
        binding.editContent.addTextChangedListener(this)
    }

    private fun openOptionMenuBottomSheet() {
        bottomSheet = BottomSheetPostRecipeImage.newInstance()
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    private fun openGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*",
        )
        pickIntent.putExtra(
            Intent.EXTRA_ALLOW_MULTIPLE,
            true,
        )
        pickIntent.action = Intent.ACTION_PICK
        openGalleryResultLauncher.launch(pickIntent)
    }

    private fun checkStoragePermission(): Boolean {
        val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val cameraPermission = Manifest.permission.CAMERA

        return if (ActivityCompat.checkSelfPermission(
                this,
                readPermission,
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                writePermission,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    readPermission,
                    writePermission,
                ),
                PERMISSION_REQ_CODE,
            )
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtil.REQUEST_CODE_PERMISSIONS) {
            if (PermissionUtil.allPermissionsGranted(this@PostRecipeActivity)) {
                openGallery()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int,
    ) {
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int,
    ) {
    }

    override fun afterTextChanged(s: Editable) {
        if (s.isNotEmpty() && viewModel.recipeImageList.value?.isNotEmpty() == true) {
            binding.tvComplete.setTextColor(
                ContextCompat.getColor(
                    this@PostRecipeActivity,
                    R.color.colorPrimaryDark,
                ),
            )
            binding.tvComplete.isEnabled = true
        } else {
            binding.tvComplete.setTextColor(
                ContextCompat.getColor(
                    this@PostRecipeActivity,
                    R.color.normal_gray,
                ),
            )
            binding.tvComplete.isEnabled = false
        }
    }

    private fun onRecipePhotoClick(photoUrl: String?) {
        val intent =
            PhotoActivity.getIntent(
                this,
                photoUrl,
            )
        startActivity(intent)
    }

    private fun deletePhoto(position: Int) {
        viewModel.deletePhoto(position)
    }

    override fun onClick(view: View) {
    }

    companion object {
        const val EXTRA_RECIPE_ENTITY = "recipe"
        const val PERMISSION_REQ_CODE = 1010
        private const val PHOTO_LIST = "photoList"

        fun getIntent(context: Context) =
            Intent(
                context,
                PostRecipeActivity::class.java,
            )

        fun getIntent(
            context: Context,
            photoList: ArrayList<String>,
        ) =
            Intent(
                context,
                PostRecipeActivity::class.java,
            )
                .putStringArrayListExtra(
                    PHOTO_LIST,
                    photoList,
                )
    }
}
