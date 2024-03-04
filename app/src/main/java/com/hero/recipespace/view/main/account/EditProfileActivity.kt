package com.hero.recipespace.view.main.account

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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityEditProfileBinding
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.view.LoadingState
import com.hero.recipespace.view.main.account.viewmodel.EditProfileUiState
import com.hero.recipespace.view.main.account.viewmodel.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileActivity :
    AppCompatActivity(),
    View.OnClickListener,
    TextWatcher {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel by viewModels<EditProfileViewModel>()

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val path = it.data?.data
                if (path != null) {
                    viewModel.setNewProfileImagePath(path.toString())
                }

                if (binding.editUserName.text.toString().isNotEmpty()) {
                    binding.tvComplete.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPrimaryDark,
                        ),
                    )
                    binding.tvComplete.isEnabled = true
                }
            }
        }

    companion object {
        const val PERMISSION_REQ_CODE = 1010

        private const val USER_KEY = "userKey"

        fun getIntent(
            context: Context,
            userKey: String,
        ) =
            Intent(context, EditProfileActivity::class.java)
                .putExtra(USER_KEY, userKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupViewModel()
        setupListeners()
    }

    private fun setUserData(userName: String) {
        binding.editUserName.setText(userName)
        binding.editUserName.setSelection(binding.editUserName.length())
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
                            Toast.makeText(
                                this@EditProfileActivity,
                                "프로필 사진 변경에 성공했습니다.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            val intent = Intent()
                            setResult(RESULT_OK, intent)
                            finish()
                        }

                        is EditProfileUiState.Failed -> {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "프로필 사진 변경에 실패했습니다. 다시 시도해주세요 ${state.message}",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }

                        EditProfileUiState.Idle -> {}
                    }
                }
            }

            user.observe(this@EditProfileActivity) {
                setUserData(it.name.orEmpty())
            }
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvComplete.setOnClickListener {
            viewModel.requestUpdateProfile()
        }
        binding.fabProfileEdit.setOnClickListener {
            if (checkStoragePermission()) {
                openGallery()
            }
        }

        binding.editUserName.addTextChangedListener(this)
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
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, writePermission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(readPermission, writePermission),
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
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
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
        if (s.isEmpty()) {
            binding.tvComplete.isEnabled = false
        } else {
            binding.tvComplete.isEnabled = s.toString() != viewModel.userName
        }
    }

    override fun onClick(view: View) {
    }
}
