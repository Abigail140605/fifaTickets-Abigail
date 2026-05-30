package com.example.paninisupporttickets.data.remote.model

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("userName") val userName: String
)
