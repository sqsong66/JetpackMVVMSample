package com.example.jetpacksample.base.di

import com.example.jetpacksample.repository.IRepository
import com.example.jetpacksample.repository.JetpackRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun provideRepository(repository: JetpackRepository): IRepository

}