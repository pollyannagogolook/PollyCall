package com.pollyanna.pollycall.di

import com.pollyanna.pollycall.data.PollyCallRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
//    @Binds
//    abstract fun provideCallRepository(repositoryImpl: PollyCallRepository): PollyCallRepository


}