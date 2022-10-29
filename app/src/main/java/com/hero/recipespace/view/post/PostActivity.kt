package com.hero.recipespace.view.post

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.adapters.SeekBarBindingAdapter.setProgress
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.hero.recipespace.R
import com.hero.recipespace.data.RecipeData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.databinding.ActivityPostBinding
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.OnFileUploadListener
import com.hero.recipespace.listener.Response
import com.hero.recipespace.storage.FirebaseStorageApi
import com.hero.recipespace.util.LoadingProgress
import com.hero.recipespace.util.MyInfoUtil
import com.hero.recipespace.util.RealPathUtil

class PostActivity : AppCompatActivity(), View.OnClickListener, TextWatcher,
    OnFileUploadListener, OnCompleteListener<RecipeData> {

    private lateinit var binding: ActivityPostBinding
    private var btnBack: ImageView? = null
    private  var ivRecipePhoto: ImageView? = null
    private var editContent: EditText? = null
    private var btnComplete: TextView? = null
    private var btnPhoto: LinearLayout? = null
    private var photoPath: String? = null
    private val PERMISSION_REQ_CODE = 1010
    private val PHOTO_REQ_CODE = 2020

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        addTextWatcher()
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
       binding.btnComplete.setOnClickListener {
           uploadImage()
       }
    }

    private fun addTextWatcher() {
        binding.editContent.addTextChangedListener(this)
    }

    override fun onClick(view: View) {
    }

    private fun intentGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        startActivityForResult(pickIntent, PHOTO_REQ_CODE)
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
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            intentGallery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_REQ_CODE && resultCode == RESULT_OK && data != null) {
            photoPath = RealPathUtil.getRealPath(this, data.data)
            Glide.with(this).load(photoPath).into(binding.ivRecipePhoto)
            btnPhoto!!.visibility = View.GONE
            if (binding.editContent.text.toString().isNotEmpty()) {
                binding.btnComplete.isEnabled = true
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        binding.btnComplete.isEnabled = s.length > 0 && !TextUtils.isEmpty(photoPath)
    }

    private fun uploadImage() {
        LoadingProgress.initProgressDialog(this)
        FirebaseStorageApi.getInstance().setOnFileUploadListener(this)
        FirebaseStorageApi.getInstance()
            .uploadImage(FirebaseStorageApi.DEFAULT_IMAGE_PATH, photoPath)
    }

    override fun onFileUploadComplete(isSuccess: Boolean, downloadUrl: String?) {
        if (isSuccess) {
            Toast.makeText(this, "업로드 완료", Toast.LENGTH_SHORT).show()
            val nickname: String = MyInfoUtil.getInstance().getUserName(this)
            val profileUrl: String = MyInfoUtil.getInstance().getProfileImageUrl(this)
            val recipeData = RecipeData()
            recipeData.setPhotoUrl(downloadUrl)
            recipeData.setContent(editContent!!.text.toString())
            recipeData.setPostDate(Timestamp.now())
            recipeData.setRate(0)
            recipeData.setUserNickname(nickname)
            recipeData.setProfileUrl(profileUrl)
            recipeData.setUserKey(MyInfoUtil.getInstance().getKey())
            FirebaseData.getInstance().uploadRecipeData(recipeData, this)
        }
    }

    override fun onFileUploadProgress(percent: Float) {
        LoadingProgress.setProgress(percent.toInt())
    }

    override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
        LoadingProgress.dismissProgressDialog()
        if (isSuccess) {
            val intent = Intent()
            intent.putExtra(EXTRA_RECIPE_DATA, response.getData())
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, "업로드에 실패했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
    }
}