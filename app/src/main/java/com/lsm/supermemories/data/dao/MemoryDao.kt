package com.lsm.supermemories.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.android.db.BaseDao
import com.lsm.supermemories.data.Memory
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MemoryDao : BaseDao<Memory> {
    @Transaction
    @Query("SELECT * FROM Memory")
    abstract fun getMemories (): Flow<List<Memory>>
    @Transaction
    @Query("SELECT date,memoryText,uid,title,memoryImage,latitude,longitude FROM Memory GROUP BY date")
    abstract fun getDates(): Flow<List<Memory>>
    @Transaction
    @Query("SELECT * FROM Memory WHERE Date =:date")
    abstract fun getAllTitles(date: String): Flow<List<Memory>>
    @Transaction
    @Query("SELECT * FROM Memory WHERE title =:memoryTitle")
    abstract fun getMemory(memoryTitle: String): Flow<List<Memory>>
}