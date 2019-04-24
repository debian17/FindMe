package ru.debian17.findme.app.di.component

import dagger.Component
import ru.debian17.findme.app.dal.AttributesDataSource
import ru.debian17.findme.app.dal.AuthDataSource
import ru.debian17.findme.app.dal.LocationDataSource
import ru.debian17.findme.app.di.module.DataSourceModule
import javax.inject.Singleton

@Singleton
@Component(modules = [DataSourceModule::class])
interface DataSourceComponent {

    fun provideAuthRepository(): AuthDataSource

    fun provideLocationRepository(): LocationDataSource

    fun provideAttributesRepository(): AttributesDataSource

}