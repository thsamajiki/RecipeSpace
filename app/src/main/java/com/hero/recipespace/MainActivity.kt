package com.hero.recipespace

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
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
class MainActivity : AppCompatActivity(),
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
        binding.ivAccountOptionMenu.setOnClickListener {
            showAccountOptionMenu()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val prevFragment = supportFragmentManager.fragments.find {
            it.isVisible
        }

        if (prevFragment != null) {
            supportFragmentManager.beginTransaction().hide(prevFragment).commitNow()
        }

        val titleArr = resources.getStringArray(R.array.title_array)

        when (item.itemId) {
            R.id.menu_recipe -> {
                binding.viewPager.currentItem = 0
                binding.ivAccountOptionMenu.visibility = View.GONE
                binding.ivAccountOptionMenu.isClickable = false
            }

            R.id.menu_chat -> {
                binding.viewPager.currentItem = 1
                item.isChecked = true
                binding.ivAccountOptionMenu.visibility = View.GONE
            }

            R.id.menu_user -> {
                binding.viewPager.currentItem = 2
                item.isChecked = true
                binding.ivAccountOptionMenu.visibility = View.VISIBLE
                binding.ivAccountOptionMenu.isClickable = true
            }
        }

        return false
    }

    private fun showAccountOptionMenu() {
        val popupMenu = PopupMenu(
            this,
            binding.ivAccountOptionMenu
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