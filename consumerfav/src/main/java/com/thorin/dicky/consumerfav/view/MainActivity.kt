package com.thorin.dicky.consumerfav.view

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.thorin.dicky.consumerfav.R
import com.thorin.dicky.consumerfav.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.thorin.dicky.consumerfav.helper.MappingHelper
import com.thorin.dicky.consumerfav.model.UserFavorite
import com.thorin.dicky.consumerfav.viewmodel.FavListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import android.os.Handler

class MainActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    private lateinit var adapter: FavListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewFav.layoutManager = LinearLayoutManager(this)
        recyclerViewFav.setHasFixedSize(true)
        adapter = FavListAdapter(this)
        recyclerViewFav.adapter = adapter

        val handleThread = HandlerThread("DataObserver")
        handleThread.start()
        val handler = Handler(handleThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadListFav()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadListFav()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listFav = list
            }
        }
    }

    private fun loadListFav() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                cursor?.let { MappingHelper.mapToArrayList(it) }
            }

            val favData = deferredFav.await()
            progressbar.visibility = View.INVISIBLE
            if (favData?.size!! > 0) {
                adapter.listFav = favData
            } else {
                adapter.listFav = ArrayList()
                showSnackBar()
            }

        }
    }

    private fun showSnackBar() {
        Snackbar.make(recyclerViewFav, "Data Is Null !", Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadListFav()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFav)
    }

}