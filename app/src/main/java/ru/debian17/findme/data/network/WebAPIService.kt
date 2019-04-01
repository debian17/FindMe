package ru.debian17.findme.data.network

import android.net.ConnectivityManager
import io.reactivex.Completable
import io.reactivex.Single
import ru.debian17.findme.data.model.auth.AuthParams
import ru.debian17.findme.data.model.auth.AuthResponse
import ru.debian17.findme.data.model.registration.RegistrationParams
import ru.debian17.findme.data.model.route.RouteInfo
import ru.debian17.findme.data.model.route.RouteParam

class WebAPIService(
        private val webAPI: WebAPI,
        private val connectivityManager: ConnectivityManager
) {

    private fun checkNetworkState() {
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo == null || !networkInfo.isConnected) {
            throw NoNetworkException()
        }
    }

    private fun <T> prepareSingleRequest(request: Single<T>): Single<T> {
        return Completable.fromAction(this::checkNetworkState).andThen(request)
    }

    private fun prepareCompletableRequest(request: Completable): Completable {
        return Completable.fromAction(this::checkNetworkState).andThen(request)
    }

    fun registration(registrationParams: RegistrationParams): Completable {
        return prepareCompletableRequest(webAPI.registration(registrationParams))
    }

    fun auth(authParams: AuthParams): Single<AuthResponse> {
        return prepareSingleRequest(webAPI.auth(authParams))
    }

    fun buildRoute(routeParam: RouteParam): Single<RouteInfo> {
        return prepareSingleRequest(webAPI.buildRoute(routeParam))
    }

}