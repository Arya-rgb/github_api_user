package com.thorin.dicky.consumerfav.helper

import android.database.Cursor
import com.thorin.dicky.consumerfav.database.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.thorin.dicky.consumerfav.database.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.thorin.dicky.consumerfav.database.DatabaseContract.FavoriteColumns.Companion.FAVORITE
import com.thorin.dicky.consumerfav.database.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.thorin.dicky.consumerfav.database.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.thorin.dicky.consumerfav.database.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.thorin.dicky.consumerfav.database.DatabaseContract.FavoriteColumns.Companion.NAME
import com.thorin.dicky.consumerfav.database.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.thorin.dicky.consumerfav.database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.thorin.dicky.consumerfav.model.UserFavorite

object MappingHelper {

    fun mapToArrayList(cursor: Cursor): ArrayList<UserFavorite> {
        val favList = ArrayList<UserFavorite>()

        cursor.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val name = getString(getColumnIndexOrThrow(NAME))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val repository = getString(getColumnIndexOrThrow(REPOSITORY))
                val followers = getString(getColumnIndexOrThrow(FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(FOLLOWING))
                val userFav = getString(getColumnIndexOrThrow(FAVORITE))

                favList.add(
                    UserFavorite(
                        username,
                        name,
                        avatar,
                        company,
                        location,
                        repository,
                        followers,
                        following,
                        userFav
                    )
                )
            }
        }
            return favList
        }
    }
