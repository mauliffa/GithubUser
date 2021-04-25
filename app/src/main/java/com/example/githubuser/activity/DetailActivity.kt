package com.example.githubuser.activity

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.data.UserData
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.db.DatabaseContract
import com.example.githubuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuser.helper.MappingHelper
import com.example.githubuser.viewmodel.DetailViewModel
import com.example.githubuser.widget.FavoriteWidget
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var uriWithId: Uri
    private var username : String? = ""
    private var user: UserData? = null
    private var isExist = false
    private var position: Int = 0


    companion object{
        const val EXTRA_USER = "extra_user"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val RESULT_DELETE = 301
        const val REQUEST_UPDATE = 200

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java
        )

        showLoading(true)

        user = intent.getParcelableExtra(EXTRA_USER)
        username = user?.username

        position = intent.getIntExtra(EXTRA_POSITION, 0)


        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user?.id)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)

        getUserDetailData()
        getTableLayout()
        checkingUser(cursor)

        binding.fabAdd.setOnClickListener{
            val intent = Intent()
            intent.putExtra(EXTRA_POSITION, position)

            if (isExist) {
                contentResolver.delete(uriWithId, null, null)
                sendUpdateBroadcast()

                isExist = false
                setStatusFavorite(isExist)
                val deleteUser = getString(R.string.delete_user)
                Toast.makeText(this, deleteUser, Toast.LENGTH_SHORT).show()

                finish()
            } else {
                val values = ContentValues()
                values.put(DatabaseContract.UserColumns.USERNAME, user?.username)
                values.put(DatabaseContract.UserColumns.PHOTO, user?.photo)
                values.put(DatabaseContract.UserColumns._ID, user?.id)

                contentResolver.insert(CONTENT_URI, values)
                sendUpdateBroadcast()

                isExist = true
                setStatusFavorite(isExist)
                val addUser = getString(R.string.add_user)
                Toast.makeText(this, addUser, Toast.LENGTH_SHORT).show()

            }
        }
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
                    .apply(RequestOptions().override(100, 100))
                    .into(binding.imgDetailPhoto)

                binding.txtCompany.text = it.company
                binding.txtLocation.text = it.location

                val repositoryText = "Repository: " + it.repository.toString()
                binding.txtRepository.text = repositoryText
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

    private fun checkingUser(dataFav: Cursor?){
        val helper = MappingHelper.mapCursorToArray(dataFav)

        for(userFav in helper){
            if(this.user?.id == userFav.id){
                isExist = true
            }
            setStatusFavorite(isExist)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setStatusFavorite(isExist: Boolean){
        if (isExist) {
            Glide.with(this@DetailActivity)
                .load(R.drawable.ic_baseline_favorite_24)
                .apply(RequestOptions().override(100, 100))
                .into(binding.fabAdd)
        } else {
            Glide.with(this@DetailActivity)
                .load(R.drawable.ic_baseline_unfavorite_24)
                .apply(RequestOptions().override(100, 100))
                .into(binding.fabAdd)
        }
    }

    private fun sendUpdateBroadcast(){
        val sendBroadcast = Intent(this, FavoriteWidget::class.java)
        sendBroadcast.action = FavoriteWidget.UPDATE_ITEM
        sendBroadcast(sendBroadcast)
    }

}