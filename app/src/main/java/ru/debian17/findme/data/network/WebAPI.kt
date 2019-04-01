package ru.debian17.findme.data.network

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.debian17.findme.data.model.auth.AuthParams
import ru.debian17.findme.data.model.auth.AuthResponse
import ru.debian17.findme.data.model.registration.RegistrationParams
import ru.debian17.findme.data.model.route.RouteParam
import ru.debian17.findme.data.model.route.RoutePoint

interface WebAPI {

    @POST("sign_up")
    fun registration(@Body registrationParams: RegistrationParams): Completable

    @POST("sign_in")
    fun auth(@Body authParams: AuthParams): Single<AuthResponse>

    @Headers(RequestTokenInterceptor.NEED_AUTH_TOKEN)
    @POST("calculate_route")
    fun buildRoute(@Body routeParam: RouteParam): Single<List<RoutePoint>>

}