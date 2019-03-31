package ru.debian17.findme.data.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.debian17.findme.data.manager.AccessTokenManager

class RequestTokenInterceptor(private val accessTokenManager: AccessTokenManager) : Interceptor {

    companion object {
        const val NEED_AUTH_TOKEN = "needAuthToken"
        const val AUTH_HEADER = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.header(NEED_AUTH_TOKEN) == null) {
            return chain.proceed(request)
        }

        val accessToken = accessTokenManager.getAccessToken()

        val newRequest = request.newBuilder()
                .removeHeader(NEED_AUTH_TOKEN)
                .addHeader(AUTH_HEADER, "Bearer $accessToken")
                .build()

        return chain.proceed(newRequest)
    }

}