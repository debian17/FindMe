package ru.debian17.findme.app

import androidx.multidex.MultiDexApplication
import ru.debian17.findme.app.di.component.AppComponent
import ru.debian17.findme.app.di.component.DaggerAppComponent
import ru.debian17.findme.app.di.component.DaggerDataSourceComponent
import ru.debian17.findme.app.di.component.DataSourceComponent
import ru.debian17.findme.app.di.module.ContextModule

class App : MultiDexApplication() {

    private lateinit var appComponent: AppComponent

    private lateinit var dataSourceComponent: DataSourceComponent

    override fun onCreate() {
        super.onCreate()

        val contextModule = ContextModule(this)

        appComponent = DaggerAppComponent.builder()
            .contextModule(contextModule)
            .build()

        dataSourceComponent = DaggerDataSourceComponent.builder()
            .contextModule(contextModule)
            .build()

    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }

    fun getDataSourceComponent(): DataSourceComponent {
        return dataSourceComponent
    }


}