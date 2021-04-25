package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.R
import com.example.githubuser.data.UserData
import com.example.githubuser.databinding.ItemUserBinding

class UserAdapter: RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    var userData = ArrayList<UserData>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserData>) {
        userData.clear()
        userData.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)
        fun bind(userData: UserData){
            with(itemView){
                binding.txtUsername.text = userData.username
                Glide.with(itemView.context)
                    .load(userData.photo)
                    .apply(RequestOptions().override(55, 55))
                    .into(binding.imgPhoto)

                setOnClickListener{onItemClickCallback.onItemClicked(userData)}
            }
        }
     }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = userData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(userData[position])
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserData)
    }

}