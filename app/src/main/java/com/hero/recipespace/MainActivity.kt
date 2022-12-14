package com.hero.recipespace

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.databinding.ActivityMainBinding
import com.hero.recipespace.view.main.account.AboutUsDialog
import com.hero.recipespace.view.main.account.setting.SettingActivity
import com.hero.recipespace.view.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    View.OnClickListener,
    NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    companion object {
        fun getIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setFragmentAdapter()
        setupViewModel()
        setupClickListener()

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.view_pager) as NavHostFragment
//        val navController: NavController = navHostFragment.findNavController()
//
//        binding.bottomNav.setupWithNavController(navController)

        binding.bottomNav.setOnItemSelectedListener(this)

        Log.d("zxcv", "MainActivity: auth uid : " + FirebaseAuth.getInstance().uid)
        Log.d("zxcv", "MainActivity: currentUser uid : " + FirebaseAuth.getInstance().currentUser?.uid)
        Log.d("zxcv", "MainActivity: userName : " + FirebaseAuth.getInstance().currentUser?.displayName)
        Log.d("zxcv", "MainActivity: profileImageUrl : " + FirebaseAuth.getInstance().currentUser?.photoUrl)
    }

    private fun setupViewModel() {
//        with(viewModel) {
//            lifecycleScope.launch {
//                mainUiState.observe(this@MainActivity) { state ->
//                    when (state) {
//                        is MainUIState.Success -> TODO()
//                        is MainUIState.Failed -> TODO()
//                        else -> {}
//                    }
//                }
//            }
//        }
    }


    private fun setFragmentAdapter() {
        val fragmentAdapter = FragmentAdapter(this)
        binding.viewPager.adapter = fragmentAdapter

        val titleArr = resources.getStringArray(R.array.title_array)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNav.menu.getItem(position).isChecked = true
                binding.tvTitle.text = titleArr[position]
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    private fun setupClickListener() {
        binding.ivAccountOptionMenu.setOnClickListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_recipe -> {
                binding.viewPager.currentItem = 0
                binding.ivAccountOptionMenu.visibility = View.GONE
                binding.ivAccountOptionMenu.isClickable = false
            }
            R.id.menu_chat -> {
                binding.viewPager.currentItem = 1
                binding.ivAccountOptionMenu.visibility = View.GONE
            }
            R.id.menu_user -> {
                binding.viewPager.currentItem = 2
                binding.ivAccountOptionMenu.visibility = View.VISIBLE
                binding.ivAccountOptionMenu.isClickable = true
            }
        }
        return false
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_account_option_menu -> showAccountOptionMenu()
        }
    }

    private fun showAccountOptionMenu() {
        val popupMenu = PopupMenu(this,
            binding.ivAccountOptionMenu)
        popupMenu.menuInflater.inflate(R.menu.menu_account_actionbar_option, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_about_us -> showAboutUsDialog()
                R.id.menu_setting -> {
                    val intent = SettingActivity.getIntent(this)
                    startActivity(intent)
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun showAboutUsDialog() {
        val aboutUsDialog = AboutUsDialog(this@MainActivity)
        aboutUsDialog.getAboutUsDialog()
    }
}