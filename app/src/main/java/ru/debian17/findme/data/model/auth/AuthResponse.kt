package ru.debian17.findme.data.model.auth

import com.google.gson.annotations.SerializedName

class AuthResponse(@SerializedName("access_token") val accessToken: String)