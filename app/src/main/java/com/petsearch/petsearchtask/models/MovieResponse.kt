package com.petsearch.petsearchtask.models

import com.google.gson.annotations.SerializedName

data class MovieResponse(@SerializedName("total_pages")var totalPages:Int,
                         @SerializedName("total_results")var totalResults:Int,
                         @SerializedName("page")var page:Int,
                         @SerializedName("results")var results:List<Movie>) {


}