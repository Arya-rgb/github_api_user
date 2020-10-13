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
import com.thorin.apps.githubusers.model.UserItem
import com.thorin.apps.githubusers.view.MainActivity
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject


class MainActViewModel : ViewModel() {
    private val token = BuildConfig.GITHUB_TOKEN
    private val listUserMutable = MutableLiveData<ArrayList<UserItem>>()
    private val listUserNonMutable = ArrayList<UserItem>()

    fun listUser() : LiveData<ArrayList<UserItem>> {
        return listUserMutable
    }
    private fun getDetail(username1: String, context: Context) {
        val httpClient = AsyncHttpClient()
        httpClient.addHeader("Authorization", "token $token")
        httpClient.addHeader("User-Agent", "request")
        val urlClient = "https://api.github.com/users/$username1"

        httpClient.get(urlClient, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                Log.d(MainActivity.TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val userItem = UserItem()
                    userItem.username = jsonObject.getString("login")
                    userItem.name = jsonObject.getString("name")
                    userItem.avatar = jsonObject.getString("avatar_url")
                    userItem.company = jsonObject.getString("company")
                    userItem.location = jsonObject.getString("location")
                    userItem.repository = jsonObject.getString("public_repos")
                    userItem.followers =  jsonObject.getString("followers")
                    userItem.following = jsonObject.getString("following")
                    listUserMutable.postValue(listUserNonMutable)
                    listUserNonMutable.add(userItem)
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

    fun getGitApi(context: Context) {
        val httpClient = AsyncHttpClient()
        httpClient.addHeader("Authorization", "token $token")
        httpClient.addHeader("User-Agent", "request")
        val urlClient = "https://api.github.com/users"
        httpClient.get(urlClient, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                Log.d(MainActivity.TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username1 = jsonObject.getString("login")
                        getDetail(username1, context)
                    }
                }catch (e: Exception) {
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

    fun searchUser(query: String, context: Context) {
        val httpClient = AsyncHttpClient()
        httpClient.addHeader("Authorization", "token $token")
        httpClient.addHeader("User-Agent", "request")
        val urlClient = "https://api.github.com/search/users?q=$query"

        httpClient.get(urlClient, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                Log.d(MainActivity.TAG, result)
                try {
                    listUserNonMutable.clear()
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        getDetail(username, context)
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
}