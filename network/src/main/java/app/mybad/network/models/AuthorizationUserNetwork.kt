package app.mybad.network.models

import com.google.gson.annotations.SerializedName

data class AuthorizationUserNetwork(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)