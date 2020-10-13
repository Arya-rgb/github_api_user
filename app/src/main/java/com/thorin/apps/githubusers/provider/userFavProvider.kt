package com.thorin.apps.githubusers.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.thorin.apps.githubusers.database.DatabaseContract.Companion.AUTHORITY
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.thorin.apps.githubusers.helper.FavQueryHelper

class userFavProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        dbHelper = FavQueryHelper.getInstance(context as Context)
        dbHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAV -> dbHelper.queryAll()
            FAV_ID -> dbHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val insert: Long = when (FAV) {
            sUriMatcher.match(uri) -> dbHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$insert")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
       val deleted: Int = when (FAV_ID) {
           sUriMatcher.match(uri) -> dbHelper.deleteById(uri.lastPathSegment.toString())
           else -> 0
       }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val updated: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> values?.let { dbHelper.update(uri.lastPathSegment.toString(), it) }!!
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    companion object {
        private const val FAV = 1
        private const val FAV_ID = 2
        private lateinit var dbHelper: FavQueryHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV)
            sUriMatcher.addURI(
                    AUTHORITY, "$TABLE_NAME/#",
                    FAV_ID

            )
        }
    }

}