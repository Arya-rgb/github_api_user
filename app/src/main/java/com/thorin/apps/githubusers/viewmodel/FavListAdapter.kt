package com.thorin.apps.githubusers.viewmodel

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.thorin.apps.githubusers.R
import com.thorin.apps.githubusers.model.UserFavorite
import com.thorin.apps.githubusers.model.UserItem
import com.thorin.apps.githubusers.view.DetailActivity
import kotlinx.android.synthetic.main.user_item.view.*

class FavListAdapter(private val activity: Activity) : RecyclerView.Adapter<FavListAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavListAdapter.ListViewHolder {
       return ListViewHolder(
               LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
       )
    }

    override fun onBindViewHolder(holder: FavListAdapter.ListViewHolder, position: Int) {
        holder.bind(listFavorite[position])
        val data = listFavorite[position]
        holder.itemView.setOnClickListener{
            val userData = UserItem(
                    data.username,
                    data.name,
                    data.avatar,
                    data.company,
                    data.location,
                    data.repository,
                    data.followers,
                    data.following
            )
            val moveIntent = Intent(it.context, DetailActivity::class.java)
            moveIntent.putExtra(DetailActivity.EXTRA_DETAIL, userData)
            it.context.startActivity(moveIntent)
        }
    }

    override fun getItemCount(): Int = listFavorite.size

    var listFavorite = ArrayList<UserFavorite>()
    set(listFavorite) {
        if (listFavorite.size > 0) {
            this.listFavorite.clear()
        }
        this.listFavorite.addAll(listFavorite)
        notifyDataSetChanged()
    }
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favorite: UserFavorite) {
            with(itemView) {
                Glide.with(itemView.context)
                        .load(favorite.avatar)
                        .apply(RequestOptions().override(100,100))
                        .error(R.drawable.ic_launcher_foreground)
                        .into(avatar)
                real_name.text = favorite.name
                username.text = favorite.username
                user_repo.text = StringBuilder(resources.getString(R.string.user_repo)).append(": ${favorite.repository}")
                user_followers.text = StringBuilder(resources.getString(R.string.follower)).append(": ${favorite.followers}")
                user_following.text = StringBuilder(resources.getString(R.string.following)).append(": ${favorite.following}")
            }
        }
    }
}