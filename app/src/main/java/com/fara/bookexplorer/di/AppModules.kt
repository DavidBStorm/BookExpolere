package com.fara.bookexplorer.di

import com.fara.bookexplorer.data.services.BookService
import com.fara.bookexplorer.data.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModules {

//add your dependencies here
@Provides
fun provideMediaRepository(bookService: BookService): BookRepository = BookRepository(bookService)
}
