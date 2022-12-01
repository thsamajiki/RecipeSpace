package com.hero.recipespace.view.main.account

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.adapters.SeekBarBindingAdapter.setProgress
import com.bumptech.glide.Glide
import com.hero.recipespace.R
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.databinding.ActivityEditProfileBinding
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.OnFileUploadListener
import com.hero.recipespace.listener.Response
import com.hero.recipespace.storage.FirebaseStorageApi
import com.hero.recipespace.util.LoadingProgress
import com.hero.recipespace.util.MyInfoUtil
import com.hero.recipespace.util.RealPathUtil
import de.hdodenhof.circleimageview.CircleImageView

class EditProfileActivity : AppCompatActivity(), View.OnClickListener,
    OnFileUploadListener, TextWatcher {

    private lateinit var binding: ActivityEditProfileBinding
    private var photoPath: String? = null
    private var profileUrl: String? = null
    private var userName: String? = null
    private val PERMISSION_REQ_CODE = 1010
    private val PHOTO_REQ_CODE = 2020

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvComplete.setOnClickListener {
            if (isNewProfile()) {
                uploadProfileImage()
            }
        }
        binding.fabProfileEdit.setOnClickListener {
            if (checkStoragePermission()) {
                intentGallery()
            }
        }

        binding.editUserName.addTextChangedListener(this)
    }

    private fun setUserData() {
        profileUrl = MyInfoUtil.getInstance().getProfileImageUrl(this)
        if (TextUtils.isEmpty(profileUrl)) {
            Glide.with(this).load(R.drawable.ic_user).into(binding.ivUserProfile)
        } else {
            Glide.with(this).load(profileUrl).into(binding.ivUserProfile)
        }
        userName = MyInfoUtil.getInstance().getUserName(this)
        binding.editUserName.setText(userName)
    }

    override fun onClick(v: View) {

    }

    private fun isNewProfile(): Boolean {
        return if (TextUtils.isEmpty(profileUrl)) {
            !TextUtils.isEmpty(photoPath)
        } else {
            if (TextUtils.isEmpty(photoPath)) {
                false
            } else {
                profileUrl != photoPath
            }
        }
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
            Glide.with(this).load(photoPath).into(binding.ivUserProfile)
            if (binding.editUserName.text.toString().isNotEmpty()) {
                binding.tvComplete.isEnabled = true
            }
        }
    }

    private fun uploadProfileImage() {
        LoadingProgress.initProgressDialog(this)
        FirebaseStorageApi.getInstance().setOnFileUploadListener(this)
        FirebaseStorageApi.getInstance()
            .uploadImage(FirebaseStorageApi.DEFAULT_IMAGE_PATH, photoPath)
    }

    private fun updateUserData(newProfileImageUrl: String) {
        val editData = HashMap<String, Any>()
        if (!TextUtils.isEmpty(newProfileImageUrl)) {
            editData[MyInfoUtil.EXTRA_PROFILE_IMAGE_URL] = newProfileImageUrl
        }
        val newUserName = binding.editUserName.text.toString()
        editData[MyInfoUtil.EXTRA_USERNAME] = newUserName
        val userKey: String = MyInfoUtil.getInstance().getKey()
        FirebaseData.getInstance()
            .updateUserData(userKey, editData, object : OnCompleteListener<Void> {
                override fun onComplete(isSuccess: Boolean, response: Response<Void>?) {
                    if (isSuccess) {
                        MyInfoUtil.getInstance()
                            .putUserName(this@EditProfileActivity, newUserName)
                        if (!TextUtils.isEmpty(newProfileImageUrl)) {
                            MyInfoUtil.getInstance()
                                .putProfileImageUrl(this@EditProfileActivity, newProfileImageUrl)
                        }
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(applicationContext,
                            "유저 정보 변경에 실패했습니다. 다시 시도해 주세요",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    override fun onFileUploadComplete(isSuccess: Boolean, downloadUrl: String?) {
        LoadingProgress.dismissProgressDialog()
        if (isSuccess) {
            updateUserData(downloadUrl)
        } else {
            Toast.makeText(this, "사진 업로드에 실패했습니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFileUploadProgress(percent: Float) {
        LoadingProgress.setProgress(percent.toInt())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (s.isEmpty()) {
            binding.tvComplete.isEnabled = false
        } else {
            binding.tvComplete.isEnabled = s.toString() != userName
        }
    }
}