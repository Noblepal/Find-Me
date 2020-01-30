package com.intelligence.findme.models

data class Service(
    val id: Int,
    val category: String,
    val profession: String,
    val image_url: String,
    val contractorCount: Int
)