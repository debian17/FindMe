package ru.debian17.findme.data.model.auth

import com.google.gson.annotations.SerializedName

class AuthParams(@SerializedName("email") val email: String,
                 @SerializedName("password") val password: String)