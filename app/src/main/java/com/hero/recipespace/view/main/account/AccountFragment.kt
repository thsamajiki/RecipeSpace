package com.hero.recipespace.view.main.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.R
import com.hero.recipespace.databinding.FragmentAccountBinding
import com.hero.recipespace.view.login.LoginActivity
import com.hero.recipespace.view.main.account.viewmodel.AccountViewModel
import com.hero.recipespace.view.photoview.PhotoActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment: Fragment(),
    View.OnClickListener {

    private var _binding: FragmentAccountBinding? = null
    private val binding: FragmentAccountBinding
        get() = _binding!!

    private val viewModel by viewModels<AccountViewModel>()

    private val editProfileResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            setUserData()
        }
    }
    private val photoResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val intent = Intent(requireActivity(), PhotoActivity::class.java)
            val userKey = FirebaseAuth.getInstance().currentUser?.uid
        }
    }

    companion object {
        fun newInstance() = AccountFragment()

        const val PROFILE_EDIT_REQ = 1010
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUserData()
        setupViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        with(viewModel) {

        }
    }

    private fun setupListeners() {
        binding.btnProfileEdit.setOnClickListener {
            intentProfileEdit()
        }
        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun setUserData() {
        val profileImageUrl: String = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        if (TextUtils.isEmpty(profileImageUrl)) {
            Glide.with(requireActivity()).load(R.drawable.ic_user).into(binding.ivUserProfile)
        } else {
            Glide.with(requireActivity()).load(profileImageUrl).into(binding.ivUserProfile)
        }
        val userName: String = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        binding.tvUserName.text = userName
    }

    private fun intentProfileEdit() {
        val intent = Intent(requireActivity(), EditProfileActivity::class.java)
        startActivityForResult(intent, PROFILE_EDIT_REQ)
    }

    private fun showLogoutDialog() {
        val logoutMessage = "로그아웃하시겠습니까?"
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("로그아웃")
            .setMessage(logoutMessage)
            .setPositiveButton("예"
            ) { dialog, which -> signOut() }
            .setNegativeButton("아니오", null)
            .create()
            .show()
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finishAffinity()
    }

    override fun onClick(view: View) {
    }
}