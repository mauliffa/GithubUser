package com.example.githubuser.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.viewmodel.MainViewModel
import com.example.githubuser.viewmodel.SearchViewModel
import com.example.githubuser.data.UserData
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        searchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            SearchViewModel::class.java)

        getGithubUser()
        searchGithubUser()

        adapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserData){
                showSelectedUser(data)
            }
        })

        binding.btnSetting.setOnClickListener {
            val moveIntent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(moveIntent)
        }

        binding.btnFav.setOnClickListener {
            val moveIntent = Intent(this@MainActivity, FavUserActivity::class.java)
            startActivity(moveIntent)
        }

    }

    private fun getGithubUser(){
        showLoading(true)
        mainViewModel.setUsers()

        mainViewModel.getUsers().observe(this, { user ->
            if (user != null){
                adapter.setData(user)
                showLoading(false)
            }
        })
    }

    private fun searchGithubUser(){
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewModel.setUsers(query)
                searchViewModel.getUsers().observe(this@MainActivity, { user ->
                    if (user != null){
                        adapter.setData(user)
                        showLoading(false)
                    }
                })
                showLoading(true)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }

        })
    }

    private fun showSelectedUser(userData: UserData) {
        val moveIntent = Intent(this@MainActivity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_USER, userData)
        }
        startActivity(moveIntent)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
