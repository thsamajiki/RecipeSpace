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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.hero.recipespace.databinding.ActivityMainBinding
import com.hero.recipespace.view.main.account.AboutUsDialog
import com.hero.recipespace.view.main.account.AccountFragment
import com.hero.recipespace.view.main.account.setting.SettingActivity
import com.hero.recipespace.view.main.chat.ChatListFragment
import com.hero.recipespace.view.main.recipe.RecipeListFragment
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

        val titleArr = resources.getStringArray(R.array.title_array)
        binding.tvTitle.text = titleArr[0]

//        setSupportActionBar(binding.toolBar)
        setFragmentAdapter()
        setupClickListener()
        setupNavigation()
    }

    private fun setFragmentAdapter() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.commit()
    }

    private fun setupClickListener() {
        binding.ivAccountOptionMenu.setOnClickListener {
            showAccountOptionMenu()
        }
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController

//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.fragment_recipe_list, R.id.fragment_chat_list, R.id.fragment_account
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNav.setupWithNavController(navController)

        binding.bottomNav.setOnItemSelectedListener(this)
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
                val recipeListFragment = supportFragmentManager.fragments.find { it is RecipeListFragment }
                if (recipeListFragment != null) {
                    supportFragmentManager.beginTransaction().show(recipeListFragment).commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .add(binding.fcvMain.id, RecipeListFragment.newInstance())
                        .commit()
                }
                binding.tvTitle.text = titleArr[0]
                item.isChecked = true
                binding.ivAccountOptionMenu.visibility = View.GONE
                binding.ivAccountOptionMenu.isClickable = false
            }
            R.id.menu_chat -> {
                val chatListFragment = supportFragmentManager.fragments.find { it is ChatListFragment }
                if (chatListFragment != null) {
                    supportFragmentManager.beginTransaction().show(chatListFragment).commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .add(binding.fcvMain.id, ChatListFragment.newInstance())
                        .commit()
                }
                binding.tvTitle.text = titleArr[1]
                item.isChecked = true
                binding.ivAccountOptionMenu.visibility = View.GONE
            }
            R.id.menu_user -> {
                val accountFragment = supportFragmentManager.fragments.find { it is AccountFragment }
                if (accountFragment != null) {
                    supportFragmentManager.beginTransaction().show(accountFragment).commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .add(binding.fcvMain.id, AccountFragment.newInstance())
                        .commit()
                }
                binding.tvTitle.text = titleArr[2]
                item.isChecked = true
                binding.ivAccountOptionMenu.visibility = View.VISIBLE
                binding.ivAccountOptionMenu.isClickable = true
            }
        }

        return false
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

    companion object {
        fun getIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }
}