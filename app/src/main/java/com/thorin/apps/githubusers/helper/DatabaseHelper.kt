package com.thorin.apps.githubusers.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.FAVORITE
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.NAME
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.thorin.apps.githubusers.database.DatabaseContract.FavoriteColumns.Companion.USERNAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAV)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "user_fav_db"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_FAV = "CREATE TABLE $TABLE_NAME" +
                "($USERNAME TEXT NOT NULL," +
                "$NAME TEXT NOT NULL," +
                "$AVATAR TEXT NOT NULL," +
                "$COMPANY TEXT NOT NULL," +
                "$LOCATION TEXT NOT NULL," +
                "$REPOSITORY TEXT NOT NULL," +
                "$FOLLOWERS TEXT NOT NULL," +
                "$FOLLOWING TEXT NOT NULL," +
                "$FAVORITE TEXT NOT NULL)"
    }

}