package ru.debian17.findme.app.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.debian17.findme.app.dal.AttributesDataSource
import ru.debian17.findme.app.dal.AuthDataSource
import ru.debian17.findme.app.dal.LocationDataSource
import ru.debian17.findme.data.manager.AccessTokenManager
import ru.debian17.findme.data.network.WebAPIService
import ru.debian17.findme.data.repository.AttributesRepository
import ru.debian17.findme.data.repository.AuthRepository
import ru.debian17.findme.data.repository.LocationRepository

@Module(includes = [WebAPIModule::class, SharedPrefModule::class, ContextModule::class])
class DataSourceModule {

    @Provides
    fun provideAuthRepository(
        accessTokenManager: AccessTokenManager,
        webAPIService: WebAPIService
    ): AuthDataSource {
        return AuthRepository(accessTokenManager, webAPIService)
    }

    @Provides
    fun provideLocationRepository(context: Context, webAPIService: WebAPIService): LocationDataSource {
        return LocationRepository(context, webAPIService)
    }

    @Provides
    fun provideAttributesRepository(webAPIService: WebAPIService): AttributesDataSource {
        return AttributesRepository(webAPIService)
    }

}