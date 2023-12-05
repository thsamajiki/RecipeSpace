package com.hero.recipespace.view.post

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hero.recipespace.R
import com.hero.recipespace.databinding.BottomSheetPostRecipeImageBinding

class BottomSheetPostRecipeImage : BottomSheetDialogFragment() {
    private var _binding: BottomSheetPostRecipeImageBinding? = null
    private val binding: BottomSheetPostRecipeImageBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetPostRecipeImageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListeners()
    }

    override fun getTheme(): Int = R.style.RounderBottomSheetDialog

    private fun setupView() {
        binding.cvPostImageOptionMenu.background = GradientDrawable().apply {
            val radius = resources.getDimension(R.dimen.bottom_sheet_radius)
            cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
        }
    }

    private fun setupListeners() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.layoutOptionCamera.setOnClickListener {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.layoutOptionGallery.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                galleryPermissionLauncher.launch(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                )
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                galleryPermissionLauncher.launch(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                )
            } else {
                galleryPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    private fun openCamera() {
        val intent = CameraActivity.getIntent(requireContext())
        startActivity(intent)
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "카메라 권한 거부됨", Toast.LENGTH_SHORT).show()
            }
        }

    private val galleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var isGalleryPermissionGranted = false
            var isWritePermissionGranted = false
            var isReadPermissionGranted = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                isGalleryPermissionGranted =
                    it[Manifest.permission.READ_MEDIA_IMAGES] ?: false
            } else {
                isWritePermissionGranted =
                    it[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false
                isReadPermissionGranted =
                    it[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
            }
            if ((isWritePermissionGranted && isReadPermissionGranted) || isGalleryPermissionGranted) {
                Toast.makeText(requireContext(), "갤러리 권한 거부됨", Toast.LENGTH_SHORT).show()
            } else {
                openGallery()
            }
        }


    private fun openGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        pickIntent.action = Intent.ACTION_PICK
        openGalleryResultLauncher.launch(pickIntent)
        Log.d("openGallery", "openGallery: 갤러리 열기 성공2!")
    }

    private val openGalleryResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                val clipData = it?.data?.clipData
                val clipDataSize = clipData?.itemCount

                if (it.data == null) { // 어떤 이미지도 선택하지 않은 경우
                    Toast.makeText(requireContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show()
                } else { // 이미지를 하나라도 선택한 경우
                    if (clipData == null) { // 이미지를 하나만 선택한 경우 clipData 가 null 이 올수 있음
                        val photoPath = it?.data?.data!!
                        Log.d("photoPath", "photoPath: $photoPath")

//                        viewModel.addRecipePhotoList(listOf(photoPath.toString()))
                        setFragmentResult(TAG, Bundle().apply {
                            putString(RESULT_ACTION, ACTION_POST_IMAGE)
                            putString(PHOTO_LIST, photoPath.toString())
                        })

                        dismiss()
                    } else {
                        clipData.let { clip ->
                            if (clipDataSize != null) {
                                if (clipDataSize > 10) {
                                    Toast.makeText(requireContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG)
                                        .show()
                                } else { // 선택한 이미지가 1장 이상 10장 이하인 경우

                                    // 선택한 사진 수만큼 반복
                                    val photoList = (0 until clipDataSize).map { index ->
                                        val photoPath = clip.getItemAt(index).uri
                                        photoPath.toString()
                                    }

                                    Log.d("openGalleryResultLauncher", "openGalleryResultLauncher: $photoList")

                                    setFragmentResult(TAG, Bundle().apply {
                                        putString(RESULT_ACTION, ACTION_POST_IMAGE)
                                        putStringArrayList(PHOTO_LIST, ArrayList<String>(photoList))
                                    })

                                    dismiss()
                                }
                            }
                        }
                    }
                }
            }
        }

    // 절대 경로 변환
    private fun absolutelyPath(path: Uri?, context: Context): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        val index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()

        val result = cursor?.getString(index!!)

        return result!!
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val RESULT_ACTION = "result_action"
        const val ACTION_POST_IMAGE = "action_post_image"
        const val PHOTO_LIST = "photo_list"

        fun newInstance(): BottomSheetPostRecipeImage =
            BottomSheetPostRecipeImage()

        const val TAG = "BottomSheetPostRecipeImage"
    }
}