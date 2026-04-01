package com.dhruv.fitgenieai.di

import com.dhruv.fitgenieai.data.repository.FitGenieAIRepositoryImpl
import com.dhruv.fitgenieai.domain.repository.FitGenieAIRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideFitGenieAIImpl(fitGenieAIRepositoryImpl: FitGenieAIRepositoryImpl): FitGenieAIRepository

}