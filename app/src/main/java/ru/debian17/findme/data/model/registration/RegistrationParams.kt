package ru.debian17.findme.data.model.registration

import com.google.gson.annotations.SerializedName

class RegistrationParams(@SerializedName("email") val email: String,
                         @SerializedName("password") val password: String)