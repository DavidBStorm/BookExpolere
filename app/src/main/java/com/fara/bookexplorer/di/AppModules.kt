package com.fara.bookexplorer.di

import android.app.Application
import android.content.Context
import com.fara.bookexplorer.data.repository.MediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModules {

//add your dependencies here
@Provides
fun provideMediaRepository(application: Application): MediaRepository = MediaRepository(application)
}
