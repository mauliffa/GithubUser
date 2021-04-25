package com.example.githubuser.helper

import android.database.Cursor
import com.example.githubuser.data.UserData
import com.example.githubuser.db.DatabaseContract

object MappingHelper {
    fun mapCursorToArray(userCursor: Cursor?): ArrayList<UserData>{
        val userList = ArrayList<UserData>()

        userCursor?.apply{
            while(moveToNext()){
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.PHOTO))
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                userList.add(UserData(username, photo, id))
            }
        }
        return userList
    }

}