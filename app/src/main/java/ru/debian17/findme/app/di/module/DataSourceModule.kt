package ru.debian17.findme.app.di.module

import dagger.Module
import dagger.Provides
import ru.debian17.findme.app.dal.AuthDataSource
import ru.debian17.findme.data.manager.AccessTokenManager
import ru.debian17.findme.data.network.WebAPIService
import ru.debian17.findme.data.repository.AuthRepository

@Module(includes = [WebAPIModule::class, SharedPrefModule::class])
class DataSourceModule {

    @Provides
    fun provideAuthRepository(
        accessTokenManager: AccessTokenManager,
        webAPIService: WebAPIService
    ): AuthDataSource {
        return AuthRepository(accessTokenManager, webAPIService)
    }


}