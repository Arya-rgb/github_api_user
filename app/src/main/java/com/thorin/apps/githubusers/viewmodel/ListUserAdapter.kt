package com.thorin.apps.githubusers.viewmodel

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.thorin.apps.githubusers.R
import com.thorin.apps.githubusers.model.UserItem
import com.thorin.apps.githubusers.view.DetailActivity
import kotlinx.android.synthetic.main.user_item.view.*

class ListUserAdapter(private val listUserData: ArrayList<UserItem>) :
        RecyclerView.Adapter<ListUserAdapter.ListUserHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListUserAdapter.ListUserHolder {
        return ListUserHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListUserAdapter.ListUserHolder, position: Int) {
        holder.bind(listUserData[position])
        val user = listUserData[position]
        holder.itemView.setOnClickListener {
            val dataUserIntent = UserItem(
                user.username,
                user.name,
                user.avatar,
                user.company,
                user.location,
                user.repository,
                user.followers,
                user.following
            )
            val moveIntent = Intent(it.context, DetailActivity::class.java)
            moveIntent.putExtra(DetailActivity.EXTRA_DETAIL, dataUserIntent)
            it.context.startActivity(moveIntent)
        }
    }

    override fun getItemCount(): Int {
        return listUserData.size
    }

    fun setData(items: ArrayList<UserItem>) {
        listUserData.clear()
        listUserData.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListUserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(userItem: UserItem) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(userItem.avatar)
                    .apply(RequestOptions().override(100,100))
                    .into(avatar)

                real_name.text = userItem.name
                username.text = userItem.username
                user_repo.text = StringBuilder(resources.getString(R.string.user_repo)).append(": ${userItem.repository}")
                user_followers.text = StringBuilder(resources.getString(R.string.follower)).append(": ${userItem.followers}")
                user_following.text = StringBuilder(resources.getString(R.string.following)).append(": ${userItem.following}")
            }
        }
    }
}