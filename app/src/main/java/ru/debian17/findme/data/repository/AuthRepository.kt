package ru.debian17.findme.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.debian17.findme.app.dal.AuthDataSource
import ru.debian17.findme.data.manager.AccessTokenManager
import ru.debian17.findme.data.network.WebAPIService

class AuthRepository(
        private val tokenManager: AccessTokenManager,
        private val webApiService: WebAPIService
) : AuthDataSource {

    override fun isUserLogin(): Single<Boolean> {
        return Single.fromCallable {
            tokenManager.isUserLogin()
        }
    }

    override fun logout(): Completable {
        return Completable.fromAction {
            tokenManager.logout()
        }
    }

}