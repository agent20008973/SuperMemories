package com.lsm.supermemories.di

import android.content.Context
import com.lsm.supermemories.data.AppDatabase
import com.lsm.supermemories.data.dao.MemoryDao
import com.lsm.supermemories.data.dao.SentenceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideMemoryDao(appDatabase: AppDatabase): MemoryDao {
        return appDatabase.memoryDao()
    }
    @Provides
    fun provideSentenceDao(appDatabase: AppDatabase): SentenceDao {
        return appDatabase.sentenceDao()
    }
}