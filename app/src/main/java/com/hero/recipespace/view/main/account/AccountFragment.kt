package com.hero.recipespace.view.main.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hero.recipespace.R
import com.hero.recipespace.databinding.FragmentAccountBinding
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.view.LoadingState
import com.hero.recipespace.view.login.LoginActivity
import com.hero.recipespace.view.main.account.viewmodel.AccountViewModel
import com.hero.recipespace.view.main.account.viewmodel.SignOutUiState
import com.hero.recipespace.view.photoview.PhotoActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment: Fragment(),
    View.OnClickListener {

    private var _binding: FragmentAccountBinding? = null
    private val binding: FragmentAccountBinding
        get() = _binding!!

    private val viewModel by viewModels<AccountViewModel>()

    private val editProfileResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            viewModel.refreshUserProfile()
        }
    }

    companion object {
        fun newInstance() = AccountFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                loadingState.collect { state ->
                    when (state) {
                        LoadingState.Hidden -> hideLoading()
                        LoadingState.Loading -> showLoading()
                        is LoadingState.Progress -> setProgressPercent(state.value)
                        LoadingState.Idle -> {}
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                signOutUiState.collect { state ->
                    when (state) {
                        is SignOutUiState.Failed -> Toast.makeText(requireActivity(), "로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show()

                        is SignOutUiState.Success -> {
                            val intent = LoginActivity.getIntent(requireActivity())
                            startActivity(intent)
                            finishAffinity(requireActivity())
                        }
                        SignOutUiState.Idle -> {}
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnEditProfile.setOnClickListener {
            val user: UserEntity? = viewModel.user.value
            if (user != null) {
                intentEditProfile(user)
            }
        }

        binding.ivUserProfile.setOnClickListener {
            val profileImageUrl = viewModel.profileImageUrl.value
            if (profileImageUrl != null) {
                intentPhoto(profileImageUrl)
            }
        }

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun intentEditProfile(user: UserEntity) {
        val intent = EditProfileActivity.getIntent(requireActivity(), user.key)
        editProfileResultLauncher.launch(intent)
    }

    private fun intentPhoto(profileImageUrl: String) {
        val intent = PhotoActivity.getIntent(requireActivity(), profileImageUrl)
        startActivity(intent)
    }

    private fun showLogoutDialog() {
        val logoutMessage = "로그아웃하시겠습니까?"
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("로그아웃")
            .setMessage(logoutMessage)
            .setPositiveButton("예")
            { dialog, _ ->
                viewModel.signOut()
                dialog.dismiss()
            }
            .setNegativeButton("아니오")
            { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onClick(view: View) {
    }
}