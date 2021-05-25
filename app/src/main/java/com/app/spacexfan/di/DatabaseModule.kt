package com.app.spacexfan.di

import com.app.spacexfan.data.repository.SpaceXRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideSpaceXRepository(): SpaceXRepository {
        return SpaceXRepository()
    }
}