package com.eyeque.reactiveprogramming

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReactiveModule {
    @Provides
    @Singleton
    fun provideReactiveDataRepository(longJobs: LongJobs) = ReactiveDataRepository(longJobs)

    @Provides
    fun provideLongTimeJob() = LongJobs()
}