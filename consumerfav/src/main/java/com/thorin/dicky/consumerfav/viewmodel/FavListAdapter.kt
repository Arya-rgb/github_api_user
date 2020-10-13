package com.thorin.dicky.consumerfav.viewmodel

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.thorin.dicky.consumerfav.R
import com.thorin.dicky.consumerfav.model.UserFavorite
import kotlinx.android.synthetic.main.user_item.view.*

class FavListAdapter(private val activity: Activity) : RecyclerView.Adapter<FavListAdapter.ListViewHolder>() {

    var listFav = ArrayList<UserFavorite>()
        set(listFav) {
            if (listFav.size > 0) {
                this.listFav.clear()
            }
            this.listFav.addAll(listFav)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavListAdapter.ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false))
    }

    override fun onBindViewHolder(holder: FavListAdapter.ListViewHolder, position: Int) {
        holder.bind(listFav[position])
    }

    override fun getItemCount(): Int = listFav.size

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