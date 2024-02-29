package com.pollyanna.pollycall.di

import android.content.Context
import com.pollyanna.pollycall.data.local.CallDao
import com.pollyanna.pollycall.data.local.CallDatabase
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

    @Singleton
    @Provides
    fun provideCallDao(callDatabase: CallDatabase): CallDao {
        return callDatabase.callDao()
    }

}