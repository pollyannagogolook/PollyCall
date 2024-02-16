package com.pollyanna.pollycall.di

import com.pollyanna.pollycall.data.PollyCallRepository
import com.pollyanna.pollycall.data.PollyCallRepositoryImpl
import com.pollyanna.pollycall.iap.purchase.SubscriptionRepository
import com.pollyanna.pollycall.iap.purchase.SubscriptionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun provideCallRepository(repositoryImpl: PollyCallRepositoryImpl): PollyCallRepository


}