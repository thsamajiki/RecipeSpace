package com.hero.recipespace.view.post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hero.recipespace.databinding.ActivityPhotoResultBinding
import java.io.File

class PhotoResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoResultBinding
    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoResultBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        initView()
        setOnClickListeners()
    }

    private fun initView() {
        imagePath = intent.getStringExtra(PHOTO_URL)
        val photoUri = Uri.parse(imagePath)
        Glide.with(this).load(photoUri).into(binding.ivPhoto)
    }

    private fun setOnClickListeners() {
        binding.btnDelete.setOnClickListener {
            deletePhoto(
                object : PhotoCallback {
                    override fun deleteSuccess() {
                        Toast.makeText(
                            this@PhotoResultActivity,
                            "사진이 삭제되었습니다.",
                            Toast.LENGTH_SHORT,
                        )
                            .show()
                        finish()
                    }

                    override fun deleteFailed() {
                        Toast.makeText(
                            this@PhotoResultActivity,
                            "사진 삭제에 실패했습니다.",
                            Toast.LENGTH_SHORT,
                        )
                            .show()
                    }
                },
                imagePath,
            )
        }

        binding.btnOk.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*",
        )
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
                    Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show()
                } else { // 이미지를 하나라도 선택한 경우
                    if (clipData == null) { // 이미지를 하나만 선택한 경우 clipData 가 null 이 올수 있음
                        val photoPath = it?.data?.data!!
                        Log.d("photoPath", "photoPath: $photoPath")

                        val photoList = ArrayList<String>()
                        photoList.add(photoPath.toString())

                        val intent = PostRecipeActivity.getIntent(this)
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_SINGLE_TOP)
                            .putStringArrayListExtra(PHOTO_LIST, photoList)
                        startActivity(intent)
                    } else {
                        clipData.let { clip ->
                            if (clipDataSize != null) {
                                if (clipDataSize > 10) {
                                    Toast.makeText(
                                        this,
                                        "사진은 10장까지 선택 가능합니다.",
                                        Toast.LENGTH_LONG,
                                    )
                                        .show()
                                } else { // 선택한 이미지가 1장 이상 10장 이하인 경우

                                    // 선택한 사진 수만큼 반복
                                    val photoList =
                                        (0 until clipDataSize).map { index ->
                                            val photoPath = clip.getItemAt(index).uri
                                            photoPath.toString()
                                        }

                                    Log.d(
                                        "openGalleryResultLauncher",
                                        "openGalleryResultLauncher: $photoList",
                                    )

                                    val intent = PostRecipeActivity.getIntent(this)
                                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_SINGLE_TOP)
                                        .putStringArrayListExtra(PHOTO_LIST, ArrayList(photoList))
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun deletePhoto(
        callback: PhotoCallback,
        imagePath: String?,
    ) {
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val cursor =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                contentResolver.query(Uri.parse(imagePath), projection, null, null)
            } else {
                contentResolver.query(
                    Uri.parse(imagePath),
                    projection,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC",
                    null,
                )
            }

        if (cursor?.moveToFirst() == true) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val filePath = cursor.getString(columnIndex)
            cursor.close()

            val file = File(filePath)

            if (file.delete()) {
                callback.deleteSuccess()
            } else {
                callback.deleteFailed()
            }
        }
    }

    companion object {
        const val PHOTO_URL = "photoUrl"
        const val PHOTO_LIST = "photoList"

        fun getIntent(context: Context) =
            Intent(
                context,
                PhotoResultActivity::class.java,
            )

        fun getIntent(
            context: Context,
            photoUrl: String?,
        ) =
            Intent(
                context,
                PhotoResultActivity::class.java,
            )
                .putExtra(PHOTO_URL, photoUrl)
    }
}

interface PhotoCallback {
    fun deleteSuccess()
    fun deleteFailed()
}
