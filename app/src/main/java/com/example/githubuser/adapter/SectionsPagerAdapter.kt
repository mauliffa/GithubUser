package com.example.githubuser.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.fragment.FollowersFragment
import com.example.githubuser.fragment.FollowingFragment

class SectionsPagerAdapter (activity: AppCompatActivity) :
FragmentStateAdapter(activity){

    var username: String? = null

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment.newInstance(username)
            1 -> fragment = FollowingFragment.newInstance(username)
        }
        return fragment as Fragment
    }
}