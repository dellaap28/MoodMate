package com.example.moodmate.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MoodMateDB"
        private const val DATABASE_VERSION = 3

        //TABLE USER
        private const val TABLE_USER = "user"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        //TABLE DIARY
        private const val TABLE_DIARY = "diary"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_MOOD = "mood"
        private const val COLUMN_NOTE = "note"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_USER (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL
            )
        """
        db.execSQL(createTable)

        // Tabel Diary
        val createDiaryTable = """
        CREATE TABLE $TABLE_DIARY (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_DATE TEXT NOT NULL,
            $COLUMN_MOOD TEXT NOT NULL,
            $COLUMN_NOTE TEXT NOT NULL,
            $COLUMN_LATITUDE TEXT,
            $COLUMN_LONGITUDE TEXT
        )
    """
        db.execSQL(createDiaryTable)

        // Tambah user default
        val defaultUser = ContentValues().apply {
            put(COLUMN_USERNAME, "admin")
            put(COLUMN_PASSWORD, "admin")
        }
        db.insert(TABLE_USER, null, defaultUser)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIARY") // tambahkan ini
        onCreate(db)
    }

    //Fungsi Login
    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USER WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    //Fungsi Input Diary
    fun insertDiary(date: String, mood: String, note: String, latitude: String?, longitude: String?): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, date)
            put(COLUMN_MOOD, mood)
            put(COLUMN_NOTE, note)
            put(COLUMN_LATITUDE, latitude)
            put(COLUMN_LONGITUDE, longitude)
        }
        return try {
            val result = db.insert(TABLE_DIARY, null, values)
            result != -1L
        } catch (e: Exception) {
            false
        }
    }

    fun getAllDiary(): List<String> {
        val diaryList = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_DIARY ORDER BY $COLUMN_DATE DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val mood = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOOD))
                val note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))
                diaryList.add("$date - Mood: $mood\nCatatan: $note")
            } while (cursor.moveToNext())
        }

        cursor.close()
        return diaryList
    }
}