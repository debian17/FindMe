package ru.debian17.findme.app.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.debian17.findme.app.dal.*
import ru.debian17.findme.data.manager.AccessTokenManager
import ru.debian17.findme.data.network.WebAPIService
import ru.debian17.findme.data.repository.AttributesRepository
import ru.debian17.findme.data.repository.AuthRepository
import ru.debian17.findme.data.repository.CategoriesRepository
import ru.debian17.findme.data.repository.LocationRepository

@Module(
    includes = [WebAPIModule::class, SharedPrefModule::class, ContextModule::class,
        DatabaseModule::class]
)
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

    @Provides
    fun provideCategoriesRepository(
        webAPIService: WebAPIService,
        dataBaseSource: DatabaseSource
    ): CategoriesDataSource {
        return CategoriesRepository(webAPIService, dataBaseSource)
    }

}