package com.example.githubconsumer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubconsumer.R
import com.example.githubconsumer.adapter.SectionsPagerAdapter
import com.example.githubconsumer.data.UserData
import com.example.githubconsumer.databinding.ActivityDetail2Binding
import com.example.githubconsumer.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetail2Binding
    private lateinit var detailViewModel: DetailViewModel
    private var username : String? = ""
    private var user: UserData? = null


    companion object{
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetail2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java)

        showLoading(true)

        user = intent.getParcelableExtra(EXTRA_USER)
        username = user?.username

        getUserDetailData()
        getTableLayout()
    }

    private fun getUserDetailData(){
        if (username != null) {
            detailViewModel.setUsers(username!!)
        } else {
            Toast.makeText(this, "data username null", Toast.LENGTH_SHORT).show()
        }


        detailViewModel.getUsers().observe(this@DetailActivity, {
            binding.apply {
                showLoading(false)

                binding.txtDetailName.text = it.name
                binding.txtDetailUsername.text = it.username

                Glide.with(this@DetailActivity)
                    .load(it.photo)
                    .apply(RequestOptions().override(100,100))
                    .into(binding.imgDetailPhoto)

                binding.txtCompany.text=it.company
                binding.txtLocation.text=it.location

                val repositoryText = "Repository: "+it.repository.toString()
                binding.txtRepository.text= repositoryText
            }
        })
    }

    private fun getTableLayout(){
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}