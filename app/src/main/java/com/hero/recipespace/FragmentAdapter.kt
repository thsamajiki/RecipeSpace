package com.hero.recipespace

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hero.recipespace.view.main.account.AccountFragment
import com.hero.recipespace.view.main.chat.ChatListFragment
import com.hero.recipespace.view.main.recipe.RecipeListFragment

class FragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RecipeListFragment.newInstance()
            1 -> ChatListFragment.newInstance()
            else -> AccountFragment.newInstance()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}