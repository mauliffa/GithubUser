package com.example.githubuser.activity

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.activity.DetailActivity.Companion.EXTRA_USER
import com.example.githubuser.adapter.FavUserAdapter
import com.example.githubuser.data.UserData
import com.example.githubuser.databinding.ActivityFavUserBinding
import com.example.githubuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuser.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavUserActivity : AppCompatActivity() {
    private lateinit var adapter: FavUserAdapter
    private lateinit var binding: ActivityFavUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = FavUserAdapter(this)

        binding.rvFavUser.layoutManager = LinearLayoutManager(this)
        binding.rvFavUser.setHasFixedSize(true)
        binding.rvFavUser.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object: ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadUserAsync()
        } else {
            savedInstanceState.getParcelableArrayList<UserData>(EXTRA_USER)?.also { adapter.listFavUser = it }
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                DetailActivity.REQUEST_ADD -> if (resultCode == DetailActivity.RESULT_ADD) {
                    val favUser = data.getParcelableExtra<UserData>(EXTRA_USER) as UserData
                    adapter.addItem(favUser)
                    binding.rvFavUser.smoothScrollToPosition(adapter.itemCount - 1)
                }

                DetailActivity.REQUEST_UPDATE ->
                    when(resultCode){
                        DetailActivity.RESULT_DELETE -> {
                            val favUser = data.getIntExtra(DetailActivity.EXTRA_POSITION, 0)
                            adapter.removeItem(favUser)
                        }
                    }
            }
        }
    }

    private fun loadUserAsync(){
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArray(cursor)
            }

            val users = deferredNotes.await()
            if (users.size > 0) {
                adapter.listFavUser = users
                binding.progressBar.visibility = View.GONE
            } else {
                adapter.listFavUser = ArrayList()
                val text = getString(R.string.none)
                binding.txtNone.text = text

                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_USER, adapter.listFavUser)
    }

}