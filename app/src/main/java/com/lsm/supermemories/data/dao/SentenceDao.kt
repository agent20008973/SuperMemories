package com.lsm.supermemories.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.android.db.BaseDao
import com.lsm.supermemories.data.Sentence
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SentenceDao: BaseDao<Sentence> {
    @Query("SELECT * FROM Sentence")
    abstract fun getAllSentences(): Flow<List<Sentence>>
}