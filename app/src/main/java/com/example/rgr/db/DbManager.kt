package com.example.rgr.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class DbManager(context: Context) {
    private val myDbHelper = MyDbHelper(context)
    private var db: SQLiteDatabase? = null

    fun openDb() {
        db = myDbHelper.writableDatabase
    }

    fun insertToDb(title: String, content: String, uri: String) {
        val values = ContentValues().apply {
            put(DbColName.COLUMN_NAME_TITLE, title)
            put(DbColName.COLUMN_NAME_CONTENT, content)
            put(DbColName.COLUMN_NAME_IMAGE_URI, uri)
        }
        db?.insert(DbColName.TABLE_NAME, null, values)
    }

    @SuppressLint("Range")
    fun readDbData(): ArrayList<String> {
        val dataList = ArrayList<String>()
        val cursor = db?.query(
            DbColName.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null)

        with(cursor) {
            while (this?.moveToNext()!!) {
                val dataText = cursor?.getString(cursor.getColumnIndex(DbColName.COLUMN_NAME_TITLE))
                dataList.add(dataText.toString())
            }
        }

        cursor?.close()
        return dataList
    }

    fun closeDb() {
        myDbHelper.close()
    }
}