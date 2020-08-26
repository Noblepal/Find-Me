package com.intelligence.findme.models

data class ProviderResponse(
    val error: Boolean,
    val message: String,
    val contractors: List<Provider>
)