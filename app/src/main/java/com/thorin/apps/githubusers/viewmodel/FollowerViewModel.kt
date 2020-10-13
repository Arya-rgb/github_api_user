package com.thorin.apps.githubusers.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.thorin.apps.githubusers.BuildConfig
import com.thorin.apps.githubusers.model.UserFollower
import com.thorin.apps.githubusers.view.FollowerFragment
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import kotlin.Exception

class FollowerViewModel : ViewModel() {
    private val token = BuildConfig.GITHUB_TOKEN
    private val listFollowerMutable = MutableLiveData<ArrayList<UserFollower>>()
    private val listFollowerNonMutable = ArrayList<UserFollower>()

    fun getListFollowers(): LiveData<ArrayList<UserFollower>> {
        return listFollowerMutable
    }

    fun getGitApiFollower(context: Context, id: String) {
        val httpClient = AsyncHttpClient()
        httpClient.addHeader("Authorization","token $token")
        httpClient.addHeader("User-Agent", "request")
        val urlClient = "https://api.github.com/users/$id/followers"

        httpClient.get(urlClient, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                Log.d(FollowerFragment.TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username1 = jsonObject.getString("login")
                        getDetailFollower(username1, context)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getDetailFollower(username1: String, context: Context) {
        val httpClient = AsyncHttpClient()
        httpClient.addHeader("Authorization", "token $token")
        httpClient.addHeader("User-Agent", "request")
       val urlClient = "https://api.github.com/users/$username1"

        httpClient.get(urlClient, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                Log.d(FollowerFragment.TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val userItem = UserFollower()
                    userItem.username = jsonObject.getString("login")
                    userItem.name = jsonObject.getString("name")
                    userItem.avatar = jsonObject.getString("avatar_url")
                    userItem.company = jsonObject.getString("company")
                    userItem.location = jsonObject.getString("location")
                    userItem.repository = jsonObject.getString("public_repos")
                    userItem.followers =  jsonObject.getString("followers")
                    userItem.following = jsonObject.getString("following")
                    listFollowerMutable.postValue(listFollowerNonMutable)
                    listFollowerNonMutable.add(userItem)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}