package com.thorin.apps.githubusers.viewmodel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.thorin.apps.githubusers.R
import com.thorin.apps.githubusers.model.UserFollowing
import kotlinx.android.synthetic.main.user_item.view.*

class ListUserFollowingAdapter(private val listUserFollowing: ArrayList<UserFollowing>) : RecyclerView.Adapter<ListUserFollowingAdapter.ListUserHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListUserFollowingAdapter.ListUserHolder {
        return ListUserHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListUserFollowingAdapter.ListUserHolder, position: Int) {
        holder.bind(listUserFollowing[position])
    }

    override fun getItemCount(): Int {
        return listUserFollowing.size
    }

    fun setData(item: ArrayList<UserFollowing>) {
        listUserFollowing.clear()
        listUserFollowing.addAll(item)
        notifyDataSetChanged()
    }

    inner class ListUserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(userFollowing: UserFollowing) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(userFollowing.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(avatar)

                real_name.text = userFollowing.name
                username.text = userFollowing.username
                user_repo.text = StringBuilder(resources.getString(R.string.user_repo)).append(": ${userFollowing.repository}")
                user_followers.text = StringBuilder(resources.getString(R.string.follower)).append(": ${userFollowing.followers}")
                user_following.text = StringBuilder(resources.getString(R.string.following)).append(": ${userFollowing.following}")
            }
        }
    }
}