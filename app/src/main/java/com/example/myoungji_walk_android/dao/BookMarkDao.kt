package com.example.myoungji_walk_android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myoungji_walk_android.Model.BookMark

@Dao
interface BookMarkDao {
    @Query("SELECT * FROM bookMark")
    fun getAll(): List<BookMark>

    @Insert
    fun insertHistory(bookMark: BookMark)

    @Query("DELETE FROM bookmark WHERE keyword == :keyword")
    fun delete(keyword: String)

    @Query("SELECT EXISTS (SELECT * FROM bookmark WHERE keyword == :keyword)")
    fun search(keyword: String) : Boolean
}
