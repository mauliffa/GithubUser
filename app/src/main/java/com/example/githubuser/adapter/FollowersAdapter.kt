package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.data.UserData
import com.example.githubuser.databinding.ItemFollowingFollowersBinding

class FollowersAdapter: RecyclerView.Adapter<FollowersAdapter.ListViewHolder>(){
    private val dataFollowers = ArrayList<UserData>()

    fun setData(items: ArrayList<UserData>) {
        dataFollowers.clear()
        dataFollowers.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(binding: ItemFollowingFollowersBinding): RecyclerView.ViewHolder(binding.root) {
        private val binding = ItemFollowingFollowersBinding.bind(itemView)
        fun bind(userData: UserData){
            binding.usernameFf.text = userData.followers
            Glide.with(itemView.context)
                .load(userData.photo)
                .apply(RequestOptions().override(55, 55))
                .into(binding.photoFf)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FollowersAdapter.ListViewHolder {
        val binding = ItemFollowingFollowersBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = dataFollowers.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dataFollowers[position])
    }

}