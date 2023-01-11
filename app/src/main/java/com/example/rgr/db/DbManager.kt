package com.example.rgr.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class DbManager(context: Context) {
    private val myDbHelper = MyDbHelper(context)
    private var db: SQLiteDatabase? = null

    fun openDb() {
        db = myDbHelper.writableDatabase
    }

    fun removeItemFromDb(id: String) {
        val selection = BaseColumns._ID + "=$id"

        db?.delete(DbColName.TABLE_NAME, selection, null )
    }

    fun insertToDb(title: String, content: String, uri: String) {
        val values = ContentValues().apply {
            put(DbColName.COLUMN_NAME_TITLE, title)
            put(DbColName.COLUMN_NAME_CONTENT, content)
            put(DbColName.COLUMN_NAME_IMAGE_URI, uri)
        }
        db?.insert(DbColName.TABLE_NAME, null, values)
    }

    fun updateItem(title: String, content: String, uri: String, id: Int) {
        val selection = BaseColumns._ID + "=$id"

        val values = ContentValues().apply {
            put(DbColName.COLUMN_NAME_TITLE, title)
            put(DbColName.COLUMN_NAME_CONTENT, content)
            put(DbColName.COLUMN_NAME_IMAGE_URI, uri)
        }
        db?.update(DbColName.TABLE_NAME, values, selection, null)
    }

    @SuppressLint("Range")
    fun readDbData(searchText: String): ArrayList<ListOfNote> {
        val dataList = ArrayList<ListOfNote>()
        val selection = "${DbColName.COLUMN_NAME_TITLE} like ?"
        val cursor = db?.query(
            DbColName.TABLE_NAME,
            null,
            selection,
            arrayOf("%$searchText%"),
            null,
            null,
            null)

        with(cursor) {
            while (this?.moveToNext()!!) {
                val dataTitle = cursor!!.getString(cursor.getColumnIndex(DbColName.COLUMN_NAME_TITLE))
                val dataContent = cursor.getString(cursor.getColumnIndex(DbColName.COLUMN_NAME_CONTENT))
                val dataUri = cursor.getString(cursor.getColumnIndex(DbColName.COLUMN_NAME_IMAGE_URI))
                val dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))

                val item = ListOfNote()

                item.title = dataTitle
                item.desc = dataContent
                item.uri = dataUri
                item.id = dataId

                dataList.add(item)
            }
        }

        cursor?.close()
        return dataList
    }

    fun closeDb() {
        myDbHelper.close()
    }
}