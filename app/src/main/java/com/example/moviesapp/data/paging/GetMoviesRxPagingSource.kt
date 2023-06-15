package com.example.moviesapp.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.data.network.MovieApi
import com.example.moviesapp.data.network.MovieResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


class GetMoviesRxPagingSource(
    private val service: MovieApi
) : RxPagingSource<Int, Movie>() {

    companion object {
        private const val DEFAULT_SORTING = "popularity.desc"
        private const val DEFAULT_START_PAGE = 1
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Movie>> {
        val position = params.key ?: DEFAULT_START_PAGE
        return service.getMoviesRx(DEFAULT_SORTING, position)
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it, position) }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(data: MovieResponse, position: Int): LoadResult<Int, Movie> {
        return LoadResult.Page(
            data = data.listOfMovies ?: emptyList(),
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position == data.totalPages) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        val anchorPosition: Int = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null

        if (anchorPage.prevKey != null) {
            return anchorPage.prevKey!! + 1
        }
        if (anchorPage.nextKey != null) {
            return anchorPage.nextKey!! - 1
        }
        return null
    }
}