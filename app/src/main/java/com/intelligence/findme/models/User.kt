package com.intelligence.findme.models

import java.io.Serializable

data class User(
    val id: Int,
    val username: String,
    val phone: String
) : Serializable