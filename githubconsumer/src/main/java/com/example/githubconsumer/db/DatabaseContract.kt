package com.example.githubconsumer.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.example.githubuser"
    const val SCHEME = "content"

    internal class UserColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val USERNAME = "username"
            const val PHOTO = "photo"
            const val _ID = "id"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}