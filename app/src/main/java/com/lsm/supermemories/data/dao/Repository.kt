package com.lsm.supermemories.data

import com.lsm.supermemories.data.dao.MemoryDao
import com.lsm.supermemories.data.dao.SentenceDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    val memoryDao: MemoryDao,
    val sentenceDao: SentenceDao
){
 fun getAllSentences() = sentenceDao.getAllSentences()
    fun getAllMemories()= memoryDao.getMemories()
    fun getAllDates() = memoryDao.getDates()
    fun getAllTitles(_date:String)=memoryDao.getAllTitles(_date)
    fun getMemory(memoryTitle: String) = memoryDao.getMemory(memoryTitle)

}