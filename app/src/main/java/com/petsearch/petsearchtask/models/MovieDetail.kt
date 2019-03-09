package com.petsearch.petsearchtask.models

data class MovieDetail(
        val adult: Boolean,
        val backdrop_path: String?=null,
        val belongs_to_collection: Any?,
        val budget: Int,
        val genres: List<Genre>,
        val homepage: String?=null,
        val id: Int,
        val imdb_id: String?=null,
        val original_language: String,
        val original_title: String,
        val overview: String?=null,
        val popularity: Double,
        val poster_path: String?=null,
        val production_companies: List<ProductionCompany>,
        val production_countries: List<ProductionCountry>,
        val release_date: String,
        val revenue: Int,
        val runtime: Int?=null,
        val spoken_languages: List<SpokenLanguage>,
        val status: String,
        val tagline: String?=null,
        val title: String,
        val video: Boolean,
        val vote_average: Double,
        val vote_count: Int
)


