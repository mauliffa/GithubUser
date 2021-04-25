package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.UserData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowersViewModel: ViewModel() {
    val listUsers = MutableLiveData<ArrayList<UserData>>()

    fun setUsers(username: String){
        val listItems = ArrayList<UserData>()

        val url = "https://api.github.com/users/$username/followers"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token ef26dd0ae08f23b0378b5a17148ec4965a76e649")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                val result = String(responseBody)

                try {
                    val responseArray = JSONArray(result)

                    for (i in 0 until responseArray.length()) {
                        val responseObject = responseArray.getJSONObject(i)
                        val user = UserData()
                        user.followers = responseObject.getString("login")
                        user.photo = responseObject.getString("avatar_url")
                        listItems.add(user)
                    }

                    listUsers.postValue(listItems)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getUsers() : LiveData<ArrayList<UserData>> {
        return listUsers
    }
}