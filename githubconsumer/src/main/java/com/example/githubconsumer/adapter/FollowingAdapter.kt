package com.example.githubconsumer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubconsumer.data.UserData
import com.example.githubconsumer.databinding.ItemFollowingFollowersBinding

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {
   private val dataFollowing = ArrayList<UserData>()

    fun setData(items: ArrayList<UserData>) {
        dataFollowing.clear()
        dataFollowing.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(binding: ItemFollowingFollowersBinding): RecyclerView.ViewHolder(binding.root) {
        private val binding = ItemFollowingFollowersBinding.bind(itemView)
        fun bind(userData: UserData){
                binding.usernameFf.text = userData.following
                Glide.with(itemView.context)
                    .load(userData.photo)
                    .apply(RequestOptions().override(55, 55))
                    .into(binding.photoFf)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemFollowingFollowersBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = dataFollowing.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dataFollowing[position])
    }

}