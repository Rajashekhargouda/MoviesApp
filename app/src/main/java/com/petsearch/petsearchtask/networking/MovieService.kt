package com.petsearch.petsearchtask.networking

import com.petsearch.petsearchtask.models.Movie
import com.petsearch.petsearchtask.models.MovieDetail
import com.petsearch.petsearchtask.models.MovieResponse
import retrofit2.Call
import retrofit2.http.*

interface MovieService {

    companion object {
        val api by lazy {
            Networking.retrofit.create(MovieService::class.java)
        }
    }

    @GET("discover/movie")
    @Headers("Content-Type:application/json")
    fun getMovies(@Query("api_key")authKey:String,
                  @Query("page")page:Int,@Query("sort_by")sortBy:String):Call<MovieResponse>

    @GET("movie/{movie_id}")
    @Headers("Content-Type:application/json")
    fun getMovieDetails(@Path("movie_id")movie_id:Int,
                        @Query("api_key")authKey:String):Call<MovieDetail>

}