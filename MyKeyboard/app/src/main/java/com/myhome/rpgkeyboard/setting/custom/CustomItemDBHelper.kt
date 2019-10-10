package com.myhome.rpgkeyboard.setting.custom

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.nio.file.StandardWatchEventKinds

class CustomItemDBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER){
    companion object{
        private val DATABASE_VER = 1
        private val DATABASE_NAME = "CUSTOMITEM.db"
        private val TABLE_NAME = "Item"
        private val COOL_ID="item_num"
        private val COOL_TITLE = "item_title"
        private val COOL_CONTENT="item_content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME ($COOL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COOL_TITLE TEXT, $COOL_CONTENT TEXT);")
        db!!.execSQL(CREATE_TABLE_QUERY)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS _$TABLE_NAME")
        onCreate(db)
    }

    val allItem:ArrayList<CheckGridItem>
    get(){
        val items = ArrayList<CheckGridItem>()
        val selectQueryHandler = "SELECT * FROM $TABLE_NAME"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQueryHandler, null)
        if(cursor.moveToFirst())
        {
            do{

                val item = CheckGridItem(cursor.getString(cursor.getColumnIndex(COOL_TITLE)), cursor.getString(cursor.getColumnIndex(
                    COOL_CONTENT)),false)
                items.add(item)
            }while(cursor.moveToNext())
        }
        db.close()
        return items
    }

    fun addItem(item:CheckGridItem){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COOL_TITLE, item.itemTitle)
        values.put(COOL_CONTENT, item.itemContent)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateItem(item:CheckGridItem):Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COOL_TITLE, item.itemTitle)
        values.put(COOL_CONTENT, item.itemContent)
        return db.update(TABLE_NAME, values, "$COOL_TITLE=?", arrayOf(item.itemTitle))
    }

    fun deleteItem(item:CheckGridItem){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COOL_TITLE=?", arrayOf(item.itemTitle))
        db.close()
    }

    fun setDefaultLabel(db:SQLiteDatabase, item:CheckGridItem){
        val values = ContentValues()
        values.put(COOL_TITLE, item.itemTitle)
        values.put(COOL_CONTENT, item.itemContent)
        db.insert(TABLE_NAME, null, values)
    }
}

