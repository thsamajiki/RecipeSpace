package com.hero.recipespace

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationBarView
import com.hero.recipespace.databinding.ActivityMainBinding
import com.hero.recipespace.view.main.account.AboutUsDialog
import com.hero.recipespace.view.main.account.setting.SettingActivity
import com.hero.recipespace.view.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setFragmentAdapter()
        setupClickListener()

        binding.bottomNav.setOnItemSelectedListener(this)
    }

    private fun setFragmentAdapter() {
        val fragmentAdapter = FragmentAdapter(this)
        binding.viewPager.adapter = fragmentAdapter

        val titleArr = resources.getStringArray(R.array.title_array)

        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
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

                    if (position == 2) {
                        binding.ivAccountOptionMenu.visibility = View.VISIBLE
                        binding.ivAccountOptionMenu.isClickable = true
                    } else {
                        binding.ivAccountOptionMenu.visibility = View.INVISIBLE
                        binding.ivAccountOptionMenu.isClickable = false
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }
            },
        )
    }

    private fun setupClickListener() {
        binding.ivAccountOptionMenu.setOnClickListener {
            showAccountOptionMenu()
        }
        val menuItemClickListener =
            Toolbar.OnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.menu_about_us -> {
                        showAboutUsDialog()
                        true
                    }

                    R.id.menu_setting -> {
                        val intent = SettingActivity.getIntent(this)
                        startActivity(intent)
                        true
                    }

                    else -> {
                        false
                    }
                }
            }

        if (binding.viewPager.currentItem == 2) {
            binding.toolBar.inflateMenu(R.menu.menu_account_actionbar_option)
            binding.toolBar.setOnMenuItemClickListener(menuItemClickListener)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val prevFragment =
            supportFragmentManager.fragments.find {
                it.isVisible
            }

        if (prevFragment != null) {
            supportFragmentManager.beginTransaction().hide(prevFragment).commitNow()
        }

        when (item.itemId) {
            R.id.menu_recipe -> {
                binding.viewPager.currentItem = 0
                binding.ivAccountOptionMenu.visibility = View.INVISIBLE
                binding.ivAccountOptionMenu.isClickable = false
            }

            R.id.menu_chat -> {
                binding.viewPager.currentItem = 1
                binding.ivAccountOptionMenu.visibility = View.INVISIBLE
                binding.ivAccountOptionMenu.isClickable = false
            }

            R.id.menu_user -> {
                binding.viewPager.currentItem = 2
                binding.ivAccountOptionMenu.visibility = View.VISIBLE
                binding.ivAccountOptionMenu.isClickable = true
            }
        }

        return true
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        if (binding.viewPager.currentItem == 2) {
//            menuInflater.inflate(R.menu.menu_account_actionbar_option, menu)
//        }
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.menu_about_us -> {
//                showAboutUsDialog()
//            }
//            R.id.menu_setting -> {
//                val intent = SettingActivity.getIntent(this)
//                startActivity(intent)
//            }
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    private fun showAccountOptionMenu() {
        val popupMenu =
            PopupMenu(
                this,
                binding.ivAccountOptionMenu,
            )
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

    companion object {
        fun getIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }
}
