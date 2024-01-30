package com.example.pollycall.di

import com.example.pollycall.data.PollyCallRepository
import com.example.pollycall.data.PollyCallRepositoryImpl
import com.example.pollycall.data.iap.SubscriptionRepository
import com.example.pollycall.data.iap.SubscriptionRepositoryImpl
import com.example.pollycall.data.local.CallDao
import com.example.pollycall.data.remote.PollyCallRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun provideCallRepository(repositoryImpl: PollyCallRepositoryImpl): PollyCallRepository
    @Binds
    abstract fun provideSubscriptionRepository(repositoryImpl: SubscriptionRepositoryImpl): SubscriptionRepository

}