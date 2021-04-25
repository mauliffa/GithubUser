package com.example.githubconsumer.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubconsumer.CustomOnItemClickListener
import com.example.githubconsumer.R
import com.example.githubconsumer.activity.DetailActivity
import com.example.githubconsumer.data.UserData
import com.example.githubconsumer.databinding.ItemFavUserBinding

class FavUserAdapter (private val activity: Activity): RecyclerView.Adapter<FavUserAdapter.FavUserViewHolder>() {
    var listFavUser = ArrayList<UserData>()

        set(listFavUser) {
            if (listFavUser.size > -1 ) {
                this.listFavUser.clear()
            }
            this.listFavUser.addAll(listFavUser)
            notifyDataSetChanged()
        }

    inner class FavUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemFavUserBinding.bind(itemView)
        fun bind(favUser: UserData) {
            binding.txtFavUsername.text = favUser.username
            Glide.with(itemView.context)
                .load(favUser.photo)
                .apply(RequestOptions().override(55, 55))
                .into(binding.imgFavPhoto)

            itemView.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                override fun onItemClicked(view: View, position: Int) {
                    val intent = Intent(activity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USER, favUser)
                    activity.startActivity(intent)
                }
            }))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fav_user, parent, false)
        return FavUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavUserViewHolder, position: Int) {
        holder.bind(listFavUser[position])
    }

    override fun getItemCount(): Int = this.listFavUser.size

}