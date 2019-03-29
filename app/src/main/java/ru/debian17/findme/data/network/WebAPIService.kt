package ru.debian17.findme.data.network

import android.net.ConnectivityManager
import io.reactivex.Completable
import io.reactivex.Single

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




}