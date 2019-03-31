package ru.debian17.findme.app.dal

import io.reactivex.Completable
import io.reactivex.Single

interface AuthDataSource {

    fun registration(email: String, password: String): Completable

    fun auth(email: String, password: String): Completable

    fun isUserLogin(): Single<Boolean>

    fun logout(): Completable

}