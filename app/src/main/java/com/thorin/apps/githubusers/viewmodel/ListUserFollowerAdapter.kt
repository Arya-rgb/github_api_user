package com.thorin.apps.githubusers.viewmodel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.thorin.apps.githubusers.R
import com.thorin.apps.githubusers.model.UserFollower
import kotlinx.android.synthetic.main.user_item.view.*

class ListUserFollowerAdapter(private val listUserFollower: ArrayList<UserFollower>) : RecyclerView.Adapter<ListUserFollowerAdapter.ListUserHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserFollowerAdapter.ListUserHolder {
        return ListUserHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListUserFollowerAdapter.ListUserHolder, position: Int) {
        holder.bind(listUserFollower[position])
    }

    override fun getItemCount(): Int {
        return listUserFollower.size
    }

    fun setData (item: ArrayList<UserFollower>) {
        listUserFollower.clear()
        listUserFollower.addAll(item)
        notifyDataSetChanged()
    }

    inner class ListUserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(userFollower: UserFollower) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(userFollower.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(avatar)

                real_name.text = userFollower.name
                username.text = userFollower.username
                user_repo.text =  StringBuilder(resources.getString(R.string.user_repo)).append(": ${userFollower.repository}")
                user_followers.text = StringBuilder(resources.getString(R.string.follower)).append(": ${userFollower.followers}")
                user_following.text = StringBuilder(resources.getString(R.string.following)).append(": ${userFollower.following}")
            }
        }
    }
}