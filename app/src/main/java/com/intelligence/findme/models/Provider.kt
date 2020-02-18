package com.intelligence.findme.models

data class Provider(
    val id: Int,
    val full_name: String,
    val profile_photo: String,
    val business_name: String,
    val price: String,
    val business_email: String,
    val business_phone: String,
    val service_type: String,
    val service_description: String,
    val open: String,
    val close: String,
    val county: String,
    val reg_date: String,
    val lat: Double,
    val lng: Double,
    val distance: Double
)