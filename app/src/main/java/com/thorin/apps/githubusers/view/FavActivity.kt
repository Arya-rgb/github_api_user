package com.thorin.apps.githubusers.view

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.thorin.apps.githubusers.R
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.thorin.apps.githubusers.helper.FavQueryHelper
import com.thorin.apps.githubusers.helper.MappingHelper
import com.thorin.apps.githubusers.model.UserFavorite
import com.thorin.apps.githubusers.viewmodel.FavListAdapter
import kotlinx.android.synthetic.main.activity_fav.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavActivity : AppCompatActivity() {

    private lateinit var dbHelper: FavQueryHelper
    private lateinit var adapter: FavListAdapter

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)
        dbHelper = FavQueryHelper.getInstance(applicationContext)
        dbHelper.open()

        recyclerViewFav.layoutManager = LinearLayoutManager(this)
        recyclerViewFav.setHasFixedSize(true)
        adapter = FavListAdapter(this)
        recyclerViewFav.adapter = adapter

        val handleThread = HandlerThread("DataObserver")
        handleThread.start()
        val handler = Handler(handleThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadFavList()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadFavList()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }
        if (supportActionBar != null) {
            supportActionBar?.title = getString(R.string.user_fav)
        }
    }

    private fun loadFavList() {
        GlobalScope.launch(Dispatchers.Main) {
            loadingProgress.visibility = View.VISIBLE
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                cursor?.let { MappingHelper.mapToArrayList(it) }
            }
            val favData = deferredFav.await()
            loadingProgress.visibility = View.INVISIBLE
            if (favData?.size!! > 0) {
                adapter.listFavorite = favData
            } else {
                adapter.listFavorite = ArrayList()
                showSnackBar()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }
    fun showSnackBar() {
        Snackbar.make(recyclerViewFav, "DATA IS NULL", Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadFavList()
    }
}