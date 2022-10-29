package com.hero.recipespace.view.main.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hero.recipespace.R
import com.hero.recipespace.databinding.FragmentAccountBinding
import com.hero.recipespace.util.MyInfoUtil
import com.hero.recipespace.view.login.LoginActivity

class AccountFragment: Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentAccountBinding
    private var ivProfile: ImageView? = null
    private var tvUserNickname: TextView? = null
    private var btnProfileEdit: MaterialButton? = null
    private  var btnLogout: MaterialButton? = null
    private val PROFILE_EDIT_REQ = 1010

    companion object {
        fun newInstance() = AccountFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root

        setupListeners()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUserData()
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
        val profileUrl: String = MyInfoUtil.getInstance().getProfileImageUrl(requireActivity())
        if (TextUtils.isEmpty(profileUrl)) {
            Glide.with(requireActivity()).load(R.drawable.ic_user).into(binding.ivUserProfile)
        } else {
            Glide.with(requireActivity()).load(profileUrl).into(binding.ivUserProfile)
        }
        val userNickname: String = MyInfoUtil.getInstance().getUserName(requireActivity())
        tvUserNickname!!.text = userNickname
    }

    override fun onClick(v: View) {
    }

    private fun intentProfileEdit() {
        val intent = Intent(requireActivity(), EditProfileActivity::class.java)
        startActivityForResult(intent, PROFILE_EDIT_REQ)
    }

    private fun showLogoutDialog() {
        val logout_message = "로그아웃하시겠습니까?"
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("로그아웃")
            .setMessage(logout_message)
            .setPositiveButton("예"
            ) { dialog, which -> signOut() }
            .setNegativeButton("아니오", null)
            .create()
            .show()
    }

    private fun signOut() {
        MyInfoUtil.getInstance().signOut(requireActivity())
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finishAffinity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PROFILE_EDIT_REQ && resultCode == Activity.RESULT_OK) {
            setUserData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_account_actionbar_option, menu)
    }
}