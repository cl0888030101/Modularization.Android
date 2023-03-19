package com.eyeque.enterprisedatamodule.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val roles: MutableList<String>,
    @SerializedName("access_token")
    val token: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)
