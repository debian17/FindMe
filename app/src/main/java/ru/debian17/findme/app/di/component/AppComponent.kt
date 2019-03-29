package ru.debian17.findme.app.di.component

import android.content.Context
import dagger.Component
import ru.debian17.findme.app.di.module.ContextModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class])
interface AppComponent {

    fun provideAppContext(): Context

}