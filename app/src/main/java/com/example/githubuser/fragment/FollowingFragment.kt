package com.example.githubuser.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.adapter.FollowingAdapter
import com.example.githubuser.R
import com.example.githubuser.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {
    private lateinit var followingAdapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var progressBar: ProgressBar

    companion object {
        private const val EXTRA_FOLLOWING_USERNAME = "extra_following_username"

        fun newInstance(username: String?): FollowingFragment{
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_FOLLOWING_USERNAME, username)
            fragment.arguments=bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvFollowing: RecyclerView = view.findViewById(R.id.rv_following)
        progressBar = view.findViewById(R.id.progress_bar_following)

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowingViewModel::class.java)

        followingAdapter = FollowingAdapter()
        followingAdapter.notifyDataSetChanged()

        rvFollowing.layoutManager = LinearLayoutManager(activity)
        rvFollowing.adapter = followingAdapter

        getFollowingData()
    }


    private fun getFollowingData(){
        val usernameFollowing = arguments?.getString(EXTRA_FOLLOWING_USERNAME)
        Log.d("cek username following", "$usernameFollowing")

        if (usernameFollowing != null) {
            progressBar.visibility = View.VISIBLE
            followingViewModel.setUsers(usernameFollowing)
        }

        followingViewModel.getUsers().observe(viewLifecycleOwner, { user ->
            if (user != null){
                progressBar.visibility = View.GONE
                followingAdapter.setData(user)
            }
        })
    }
}