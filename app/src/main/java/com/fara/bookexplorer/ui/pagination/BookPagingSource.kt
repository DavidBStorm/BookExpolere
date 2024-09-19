package com.fara.bookexplorer.ui.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fara.bookexplorer.data.services.BookService
import com.fara.bookexplorer.domain.model.Doc

class BookPagingSource(
    private val bookService: BookService,
    private val query: String
) : PagingSource<Int, Doc>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Doc> {
        return try {
            val page = params.key ?: PAGE_ONE
            val response = bookService.searchBooks(query, page,LIMIT)

            if (response.isSuccessful) {
                val books = response.body()?.docs ?: emptyList()
                LoadResult.Page(
                    data = books,
                    prevKey = if (page == PAGE_ONE) null else page - PAGE_ONE,
                    nextKey = if (books.isNotEmpty()) page + PAGE_ONE else null
                )
            } else {
                LoadResult.Error(Exception("Failed to load data"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Doc>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object{
        const val PAGE_ONE = 1
        const val LIMIT = 20
    }
}
