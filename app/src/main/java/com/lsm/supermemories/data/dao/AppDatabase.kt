package com.lsm.supermemories.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lsm.supermemories.data.dao.MemoryDao
import com.lsm.supermemories.data.dao.SentenceDao
import com.lsm.supermemories.utils.DATABASE_NAME
import com.lsm.supermemories.workers.SeedDatabaseWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(
    entities = [
        Memory::class,
        Sentence::class
    ],version = 2, exportSchema = false
)

abstract class AppDatabase : RoomDatabase(){
    abstract fun memoryDao(): MemoryDao
    abstract fun sentenceDao(): SentenceDao

companion object {
    @Volatile
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }

    private fun buildDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .addCallback(
                object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                        GlobalScope.launch(Dispatchers.IO) { rePopulateDb(instance) }
                    }
                }
            )
            .build()
    }

    suspend fun rePopulateDb(instance: AppDatabase?) {
        instance?.let { db ->
            withContext(Dispatchers.IO) {
        //        val memoryDao: MemoryDao = db.memoryDao()
                val sentenceDao: SentenceDao = db.sentenceDao()


                val sentence1 = Sentence(contents = "Czasem warto skupić się na dobrych wspomnieniach. Przypominają człowiekowi, że szczęście jednak istnieje (...)", source = "Yvonne Woon, Piękni i martwi")
                sentenceDao.insert(sentence1)
                val sentence2 = Sentence (contents = "Najważniejsze są wspomnienia. Kiedy nie zostaje już nic, wspomnienia wciąż żyją.", source = "Winston Groom, Gump i spółka")
                sentenceDao.insert(sentence2)
                val sentence3 = Sentence(contents = "Dobre czasy to nie te, w których się żyło, ale które się wspomina.",source = "Wiesław Myśliwski, Ostatnie rozdanie")
                sentenceDao.insert(sentence3)
                val sentence4 = Sentence(contents = "Gromadzi wspomnienia. Na później.",source = "Lisa McMann, Koniec")
                sentenceDao.insert(sentence4)
                val sentence5 = Sentence(contents = "Czas zmarnowany nie istnieje we wspomnieniach",source="Stefan Kisielewski",randomDate="09-09")
                sentenceDao.insert(sentence5)

            }
        }
    }
}}
