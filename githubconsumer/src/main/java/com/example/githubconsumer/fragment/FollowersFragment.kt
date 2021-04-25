package com.example.githubuser.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubconsumer.R
import com.example.githubconsumer.adapter.FollowersAdapter
import com.example.githubconsumer.viewmodel.FollowersViewModel

class FollowersFragment : Fragment() {
    private lateinit var followersAdapter: FollowersAdapter
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var progressBar: ProgressBar

    companion object {
        private const val EXTRA_FOLLOWERS_USERNAME = "extra_followers_username"

        fun newInstance(username: String?): FollowersFragment{
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_FOLLOWERS_USERNAME, username)
            fragment.arguments=bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvFollowers: RecyclerView = view.findViewById(R.id.rv_followers)
        progressBar = view.findViewById(R.id.progress_bar_followers)

        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowersViewModel::class.java)

        followersAdapter = FollowersAdapter()
        followersAdapter.notifyDataSetChanged()

        rvFollowers.layoutManager = LinearLayoutManager(activity)
        rvFollowers.adapter = followersAdapter

        getFollowersData()
    }

    private fun getFollowersData(){
        val usernameFollowers = arguments?.getString(EXTRA_FOLLOWERS_USERNAME)
        Log.d("cek username followers", "$usernameFollowers")

        if (usernameFollowers != null) {
            progressBar.visibility = View.VISIBLE
            followersViewModel.setUsers(usernameFollowers)
        } else {
            Toast.makeText(activity, "data username di fragment null", Toast.LENGTH_SHORT).show()
        }

        followersViewModel.getUsers().observe(viewLifecycleOwner, { user ->
            if (user != null){
                progressBar.visibility = View.GONE
                followersAdapter.setData(user)
            }
        })
    }
}