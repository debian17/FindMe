package ru.debian17.findme.app.dal

import io.reactivex.Completable
import io.reactivex.Single

interface AuthDataSource {

    fun isUserLogin(): Single<Boolean>

    fun logout(): Completable

}