package com.example.myoungji_walk_android

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myoungji_walk_android.Model.History
import com.example.myoungji_walk_android.dao.HistoryDao

@Database(entities = [History::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}