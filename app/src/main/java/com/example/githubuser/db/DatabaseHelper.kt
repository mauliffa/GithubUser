package com.example.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuser.db.DatabaseContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)  {
    companion object{
        private const val DATABASE_NAME = "githubuser"
        private const val DATABASE_VERSION = 3

        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.UserColumns._ID} INTEGER PRIMARY KEY NOT NULL," +
                " ${DatabaseContract.UserColumns.USERNAME} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.PHOTO} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}