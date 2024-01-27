package com.example.pollycall.di

import android.content.Context
import com.example.pollycall.data.PollyCallRepository
import com.example.pollycall.data.PollyCallRepositoryImpl
import com.example.pollycall.data.local.CallDao
import com.example.pollycall.data.local.CallDatabase
import com.example.pollycall.data.remote.PollyCallRemoteDataSource
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
    fun provideAppDatabase(@ApplicationContext context: Context): CallDatabase {
        return CallDatabase.getInstance(context)
    }

    @Provides
    fun provideCallDao(callDatabase: CallDatabase): CallDao {
        return callDatabase.callDao()
    }

    @Provides
    fun provideCallRepository(callDao: CallDao, remoteDataSource: PollyCallRemoteDataSource): PollyCallRepository{
        return PollyCallRepositoryImpl(remoteDataSource, callDao)
    }

}