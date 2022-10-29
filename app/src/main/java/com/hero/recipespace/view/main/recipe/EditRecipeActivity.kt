package com.hero.recipespace.view.main.recipe

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.hero.recipespace.R
import com.hero.recipespace.data.RecipeData
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.OnFileUploadListener
import com.hero.recipespace.util.RealPathUtil

class EditRecipeActivity : AppCompatActivity(), View.OnClickListener, TextWatcher,
    OnFileUploadListener, OnCompleteListener<RecipeData> {

    private var btnBack: ImageView? =
        null, private  var ivRecipePhoto:android.widget.ImageView? = null
    private var editContent: EditText? = null
    private var btnComplete: TextView? = null
    private var photoPath: String? = null
    private val PERMISSION_REQ_CODE = 1010
    private val PHOTO_REQ_CODE = 2020

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)
        initView()
        addTextWatcher()
    }

    private fun initView() {
        btnBack = findViewById(R.id.btn_back)
        ivRecipePhoto = findViewById<ImageView>(R.id.iv_recipe_photo)
        editContent = findViewById(R.id.edit_content)
        btnComplete = findViewById(R.id.btn_complete)
        btnBack.setOnClickListener(this)
        btnComplete.setOnClickListener(this)
        ivRecipePhoto.setOnClickListener(this)
    }

    private fun addTextWatcher() {
        editContent!!.addTextChangedListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_recipe_photo -> if (checkStoragePermission()) {
                intentGallery()
            }
            R.id.btn_back -> finish()
            R.id.btn_complete -> uploadImage()
        }
    }

    private fun intentGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        startActivityForResult(pickIntent,
            com.seoultech.recipeschoolproject.view.main.recipe.RecipeEditActivity.PHOTO_REQ_CODE)
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
            ActivityCompat.requestPermissions(this, arrayOf(readPermission, writePermission),
                com.seoultech.recipeschoolproject.view.main.recipe.RecipeEditActivity.PERMISSION_REQ_CODE)
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            intentGallery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == com.seoultech.recipeschoolproject.view.main.recipe.RecipeEditActivity.PHOTO_REQ_CODE && resultCode == RESULT_OK && data != null) {
            photoPath = RealPathUtil.getRealPath(this, data.data)
            Glide.with(this).load(photoPath).into(ivRecipePhoto)
            if (editContent!!.text.toString().length > 0) {
                btnComplete!!.isEnabled = true
            }
        }
    }

    override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}

    override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (s.length > 0 && !TextUtils.isEmpty(photoPath)) {
            btnComplete!!.isEnabled = true
        } else {
            btnComplete!!.isEnabled = false
        }
    }

    private fun uploadImage() {
        LoadingProgress.initProgressDialog(this)
        FirebaseStorageApi.getInstance().setOnFileUploadListener(this)
        FirebaseStorageApi.getInstance()
            .uploadImage(FirebaseStorageApi.DEFAULT_IMAGE_PATH, photoPath)
    }

    override fun onFileUploadComplete(isSuccess: Boolean, downloadUrl: String?) {
        if (isSuccess) {
            Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show()
            val nickname: String = MyInfoUtil.getInstance().getNickname(this)
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

    override fun onComplete(isSuccess: Boolean, response: Response<RecipeData?>) {
        LoadingProgress.dismissProgressDialog()
        if (isSuccess) {
            val intent = Intent()
            intent.putExtra(EXTRA_RECIPE_DATA, response.getData())
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, "레시피 수정에 실패했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
    }
}