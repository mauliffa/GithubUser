package com.example.githubuser.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.db.DatabaseContract
import com.example.githubuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import java.util.concurrent.ExecutionException


class StackRemoteViewsFactory(private val mContext: Context)  : RemoteViewsService.RemoteViewsFactory {
    private var cursor: Cursor? = null

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null)
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = cursor?.count!!

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        if (cursor!!.moveToPosition(position)) {
            val photo = cursor!!.getString(cursor!!.getColumnIndexOrThrow(DatabaseContract.UserColumns.PHOTO))
            val bitmap: Bitmap
            try {
                bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(photo)
                    .submit()
                    .get()
                rv.setImageViewBitmap(R.id.imageView, bitmap)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
        }

        val extras = Bundle()
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position)

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}