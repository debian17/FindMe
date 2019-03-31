package ru.debian17.findme.data.network.error

import com.google.gson.Gson
import retrofit2.HttpException
import java.io.StringWriter

class ErrorParser {

    fun getErrorResponse(throwable: Throwable): ErrorResponse {
        return if (throwable is HttpException) {
            val wr = StringWriter()
            throwable.response().errorBody()?.charStream()?.copyTo(wr)
            val jsonError = wr.toString()
            val gson = Gson()
            gson.fromJson(jsonError, ErrorResponse::class.java)
        } else {
            ErrorResponse(0, "")
        }
    }

}