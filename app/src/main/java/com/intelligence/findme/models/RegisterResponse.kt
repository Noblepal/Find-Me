package com.intelligence.findme.models

class RegisterResponse(
    val error: Boolean,
    val message: String,
    val user: List<User>
) {
}