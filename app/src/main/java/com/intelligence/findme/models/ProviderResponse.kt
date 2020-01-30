package com.intelligence.findme.models

data class ProviderResponse(
    val error: Boolean,
    val message: String,
    val distances: List<Distance>,
    val contractors: List<Provider>
)