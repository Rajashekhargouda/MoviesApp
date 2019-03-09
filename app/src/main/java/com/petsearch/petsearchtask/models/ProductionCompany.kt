package com.petsearch.petsearchtask.models

data class ProductionCompany(
        val id: Int,
        val logo_path: String?=null,
        val name: String,
        val origin_country: String
)