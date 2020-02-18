package com.intelligence.findme.models

import com.google.gson.annotations.SerializedName

data class Service(
    @SerializedName("id") val id: Int,
    @SerializedName("category") val category: String,
    @SerializedName("profession") val profession: String,
    @SerializedName("image_url") val image_url: String,
    @SerializedName("contractorCount") val contractorCount: Int,
    @SerializedName("popularity") val popularity: Int
)