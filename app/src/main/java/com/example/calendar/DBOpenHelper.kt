package com.example.calendar

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper(context: Context) :
    SQLiteOpenHelper(context, DBStructure.DB_NAME, null, DBStructure.DB_VERSION) {

    private val CREATE_TABLE: String = "create table ${DBStructure.EVENT_TABLE_NAME}" +
            "(ID integer primary key autoincrement, ${DBStructure.EVENT} text, ${DBStructure.TIME} text," +
            "${DBStructure.DATE} text, ${DBStructure.MONTH} text, ${DBStructure.YEAR} text);"

    private val DROP_TABLE = "drop table if exists ${DBStructure.EVENT_TABLE_NAME};"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun saveEvent(event:String, time:String,date:String, month:String ,year:String,db:SQLiteDatabase){
        val content = ContentValues()
        content.put(DBStructure.EVENT, event)
        content.put(DBStructure.TIME, time)
        content.put(DBStructure.DATE, date)
        content.put(DBStructure.MONTH, month)
        content.put(DBStructure.YEAR, year)
        db.insert(DBStructure.EVENT_TABLE_NAME,null,content)
    }

    fun readEventDate(date:String, db :SQLiteDatabase) :Cursor{
        val pro = arrayOf(DBStructure.EVENT,DBStructure.TIME,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR)
        val select = "${DBStructure.DATE} = ?"
        val selectArgs = arrayOf(date)
        return db.query(DBStructure.EVENT_TABLE_NAME,pro,select,selectArgs,null,null,null)
    }

    fun readEventMonth(month:String, year:String, db :SQLiteDatabase) :Cursor{
        val pro = arrayOf(DBStructure.EVENT,DBStructure.TIME,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR)
        val select = "${DBStructure.MONTH} = ? and ${DBStructure.YEAR} = ?"
        val selectArgs = arrayOf(month,year)
        return db.query(DBStructure.EVENT_TABLE_NAME,pro,select,selectArgs,null,null,null)
    }

    fun deleteAllEvent(db:SQLiteDatabase){
        db.execSQL("delete from "+ DBStructure.EVENT_TABLE_NAME)
    }

    fun deleteEvent(event: String , date: String, time : String, db : SQLiteDatabase){
        val select = "${DBStructure.EVENT} = ? and ${DBStructure.DATE} = ? and ${DBStructure.TIME} = ?"
        val selectArg = arrayOf(event,date,time)
        db.delete(DBStructure.EVENT_TABLE_NAME,select,selectArg)
    }

    fun readIDEvents(date:String, event:String, time:String, db:SQLiteDatabase): Cursor {
        val projection = arrayOf(DBStructure.ID)
        val selection = DBStructure.DATE + "= ? and " + DBStructure.EVENT +"= ? and " +DBStructure.TIME +"=?"
        val arg = arrayOf(date,event,time)
        return db.query(DBStructure.EVENT_TABLE_NAME,projection,selection,arg,null,null,null)
    }
}


