package com.intelligence.findme.models

data class ServiceResponse(val error: Boolean, val message: String, val services: List<Service>)