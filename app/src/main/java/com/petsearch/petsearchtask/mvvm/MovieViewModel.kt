package com.petsearch.petsearchtask.mvvm

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.petsearch.petsearchtask.R
import com.petsearch.petsearchtask.models.Movie
import com.petsearch.petsearchtask.models.MovieDetail
import com.petsearch.petsearchtask.networking.MovieService
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import processRequest

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    val somethingWentWrong = application.applicationContext.getString(R.string.something_went_wrong)
    val noItemsFound = application.applicationContext.getString(R.string.no_results_found)


    val movieDetailLiveData = MutableLiveData<MovieDetailResponse>()
    val movieListLiveData = MutableLiveData<MovieListResponse>()

    fun getMovies(authKey:String,page:Int,sortBy:String){
        try {
            movieListLiveData.value = MovieListResponse.Loading
            launch(UI) {
                movieListLiveData.value = async(CommonPool) {
                    getMoviesFromServer(authKey,page,sortBy)
                }.await()
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }


    private fun getMoviesFromServer(authKey:String,page:Int,sortBy:String): MovieListResponse {
       val results =  processRequest(MovieService.api.getMovies(authKey,page,sortBy))
        return when(results){
            is Either.Right ->{
                MovieListResponse.Success(results.b.results)
            }
            is Either.Left ->{
                MovieListResponse.Error(when (results.a) {
                    is Failure.ServerError -> somethingWentWrong
                    is Failure.NoResults -> noItemsFound
                    else -> somethingWentWrong
                })

            }
        }
    }


    fun getMovieDetail(authKey:String, movieId:Int){
        try {
            movieDetailLiveData.value = MovieDetailResponse.Loading
            launch(UI) {
                movieDetailLiveData.value = async(CommonPool) {
                    getMovieDetailsFromServer(authKey,movieId)
                }.await()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun getMovieDetailsFromServer(authKey:String, movieId:Int): MovieDetailResponse {

        val results =  processRequest(MovieService.api.getMovieDetails(movieId,authKey))
        return when(results){
            is Either.Right ->{
                MovieDetailResponse.Success(results.b)
            }
            is Either.Left ->{
                MovieDetailResponse.Error(when (results.a) {
                    is Failure.ServerError -> somethingWentWrong
                    is Failure.NoResults -> somethingWentWrong
                    else -> somethingWentWrong
                })
            }
        }

    }

    sealed class MovieDetailResponse{
        data class Success(var movieDetail:MovieDetail): MovieDetailResponse()
        data class Error(var msg:String): MovieDetailResponse()
        object Loading: MovieDetailResponse()
    }

    sealed class MovieListResponse{
        data class Success(var movieList: List<Movie>): MovieListResponse()
        data class Error(var msg:String): MovieListResponse()
        object Loading: MovieListResponse()
    }

}