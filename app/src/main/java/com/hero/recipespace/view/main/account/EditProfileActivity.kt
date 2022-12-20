package com.hero.recipespace.view.main.account

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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityEditProfileBinding
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.util.RealPathUtil
import com.hero.recipespace.view.LoadingState
import com.hero.recipespace.view.main.account.viewmodel.EditProfileUiState
import com.hero.recipespace.view.main.account.viewmodel.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity(),
    View.OnClickListener,
    TextWatcher {

    private lateinit var binding: ActivityEditProfileBinding
    private var photoPath: String? = null
    private var profileImageUrl: String? = null
    private var userName: String? = null

    private var newUserName: String? = null
    private var newProfileImageUrl: String = ""

    private val viewModel by viewModels<EditProfileViewModel>()

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                photoPath = RealPathUtil.getRealPath(this, it.data?.data!!)
                Glide.with(this).load(photoPath).into(binding.ivUserProfile)
                if (binding.editUserName.text.toString().isNotEmpty()) {
                    binding.tvComplete.isEnabled = true
                }
            }
        }

    companion object {
        const val PERMISSION_REQ_CODE = 1010
        const val PHOTO_REQ_CODE = 2020

        private const val USER_KEY = "userKey"

        fun getIntent(context: Context, userKey: String) =
            Intent(context, EditProfileActivity::class.java)
                .putExtra(USER_KEY, userKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setUserData()
        setupViewModel()
        setupListeners()
    }

    private fun setUserData() {
        profileImageUrl = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        if (TextUtils.isEmpty(profileImageUrl)) {
            Glide.with(this).load(R.drawable.ic_user).into(binding.ivUserProfile)
        } else {
            Glide.with(this).load(profileImageUrl).into(binding.ivUserProfile)
        }
        userName = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        binding.editUserName.setText(userName)
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
                editProfileUiState.collect { state ->
                    when (state) {
                        is EditProfileUiState.Success -> {
                            val intent = Intent()
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                        is EditProfileUiState.Failed -> {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "프로필 사진 변경에 실패했습니다. 다시 시도해주세요 ${state.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        EditProfileUiState.Idle -> {}
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
            if (isNewProfile()) {
                viewModel.requestUpdateProfile(binding.editUserName.text.toString().trim(), newProfileImageUrl)
            }
        }
        binding.fabProfileEdit.setOnClickListener {
            if (checkStoragePermission()) {
                openGallery()
            }
        }

        binding.editUserName.addTextChangedListener(this)
    }

    private fun isNewProfile(): Boolean {
        return if (TextUtils.isEmpty(profileImageUrl)) {
            !TextUtils.isEmpty(photoPath)
        } else {
            if (TextUtils.isEmpty(photoPath)) {
                false
            } else {
                profileImageUrl != photoPath
            }
        }
    }

    private fun openGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        openGalleryLauncher.launch(pickIntent)
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

    private fun updateUserData(newProfileImageUrl: String) {
        val editData = HashMap<String, Any>()
        if (!TextUtils.isEmpty(newProfileImageUrl)) {
            editData["profileImageUrl"] = newProfileImageUrl
        }
        val newUserName = binding.editUserName.text.toString().trim()
        editData["name"] = newUserName
        val userKey: String? = FirebaseAuth.getInstance().uid
        // TODO: 2022-12-20 UserServiceImpl 의 add 메소드로 옮김
//        FirebaseData.getInstance()
//            .updateUserData(userKey.orEmpty(), editData, object : OnCompleteListener<Void> {
//                override fun onComplete(isSuccess: Boolean, response: Response<Void>?) {
//                    if (isSuccess) {
//                        MyInfoUtil.getInstance()
//                            .putUserName(this@EditProfileActivity, newUserName)
//                        if (!TextUtils.isEmpty(newProfileImageUrl)) {
//                            MyInfoUtil.getInstance()
//                                .putProfileImageUrl(this@EditProfileActivity, newProfileImageUrl)
//                        }
//                        setResult(RESULT_OK)
//                        finish()
//                    } else {
//                        Toast.makeText(applicationContext, "사용자 정보 변경에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            })
    }

//    fun onFileUploadComplete(isSuccess: Boolean, downloadUrl: String?) {
//        hideLoading()
//        if (isSuccess) {
//            updateUserData(downloadUrl.orEmpty())
//        } else {
//            Toast.makeText(this, "사진 업로드에 실패했습니다", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (s.isEmpty()) {
            binding.tvComplete.isEnabled = false
        } else {
            binding.tvComplete.isEnabled = s.toString() != userName
        }
    }

    override fun onClick(view: View) {
    }
}