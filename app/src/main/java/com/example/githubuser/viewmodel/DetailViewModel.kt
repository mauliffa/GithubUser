package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.UserData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailViewModel: ViewModel(){
    val listDetailUsers = MutableLiveData<UserData>()

    fun setUsers(username: String){
        val url = "https://api.github.com/users/${username}"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token ef26dd0ae08f23b0378b5a17148ec4965a76e649")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                val result = String(responseBody)

                try {
                    val jsonObject = JSONObject(result)
                    val user = UserData(
                        username = jsonObject.getString("login"),
                        name = jsonObject.getString("name"),
                        photo = jsonObject.getString("avatar_url"),
                        repository = jsonObject.getInt("public_repos"),
                        company = jsonObject.getString("company"),
                        location = jsonObject.getString("location"),
                        id = jsonObject.getInt("id")
                    )

                    listDetailUsers.postValue(user)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getUsers() : LiveData<UserData> {
        return listDetailUsers
    }
}