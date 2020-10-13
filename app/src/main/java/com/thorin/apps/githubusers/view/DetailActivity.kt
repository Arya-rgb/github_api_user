package com.thorin.apps.githubusers.view

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.thorin.apps.githubusers.R
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.FAVORITE
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.NAME
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.thorin.apps.githubusers.helper.FavQueryHelper
import com.thorin.apps.githubusers.model.UserItem
import com.thorin.apps.githubusers.viewmodel.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    private lateinit var dbHelper: FavQueryHelper
    private var statFav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (supportActionBar != null) {
            supportActionBar?.title = resources.getString(R.string.detail_user)
        }

        dbHelper = FavQueryHelper.getInstance(applicationContext)
        dbHelper.open()

        val userVal = intent.getParcelableExtra<UserItem>(EXTRA_DETAIL) as UserItem
        val cursor: Cursor = dbHelper.queryByUsername(userVal.username.toString())
        if (cursor.moveToNext()) {
            statFav = true
            setStatFav(true)
        }
        fab_favorite.setOnClickListener(this)
        viewPagerConfig()
        setUser()
    }
    private fun viewPagerConfig() {
        val viewPagerDetail = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = viewPagerDetail
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }
    @SuppressLint("SetTextI18n")
    private fun setUser() {
        val dataUser = intent.getParcelableExtra<UserItem>(EXTRA_DETAIL) as UserItem
        Glide.with(this)
            .load(dataUser.avatar)
            .apply(RequestOptions().override(150, 150))
            .into(avatar_detail)
        real_name_detail.text = dataUser.name
        username_detail.text = "Github.com/${dataUser.username}"
        company_detail.text = "${getString(R.string.company)} ${dataUser.company}"
        location_detail.text = "${getString(R.string.location)} ${dataUser.location}"
        following_detail.text = "${getString(R.string.following)} ${dataUser.following}"
        follower_detail.text = "${getString(R.string.follower)} ${dataUser.followers}"
        user_repo_detail.text = "${getString(R.string.user_repo)} ${dataUser.repository}"
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_change_settings -> {
                val moveIntent = Intent(this, SetActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.fav_menu -> {
                val moveIntent = Intent(this, FavActivity::class.java)
                startActivity(moveIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        val data = intent.getParcelableExtra<UserItem>(EXTRA_DETAIL) as UserItem
        when(v?.id) {
            R.id.fab_favorite -> {
                if (statFav) {
                    val idUser = data.username.toString()
                    dbHelper.deleteById(idUser)
                    Toast.makeText(this, "Data Has Been Deleted From Favorite", Toast.LENGTH_SHORT).show()
                    setStatFav(false)
                    statFav = true
                } else {
                    val values = ContentValues()
                    values.put(USERNAME, data.username)
                    values.put(NAME, data.name)
                    values.put(AVATAR, data.avatar)
                    values.put(COMPANY, data.company)
                    values.put(LOCATION, data.location)
                    values.put(REPOSITORY, data.repository)
                    values.put(FOLLOWERS, data.followers)
                    values.put(FOLLOWING, data.following)
                    values.put(FAVORITE, "userFav")

                    statFav = false
                    contentResolver.insert(CONTENT_URI, values)
                    Toast.makeText(this, "Data Successfully Added !", Toast.LENGTH_SHORT).show()
                    setStatFav(true)
                }
                this.recreate()
            }
        }
    }

    private fun setStatFav(status: Boolean) {
        if (status) {
            fab_favorite.setImageResource(R.drawable.ic_favorited_star)
        }else{
            fab_favorite.setImageResource(R.drawable.ic_favorite_user)
        }
    }
}