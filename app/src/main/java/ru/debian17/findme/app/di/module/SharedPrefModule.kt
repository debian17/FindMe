package ru.debian17.findme.app.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.debian17.findme.data.manager.AccessTokenManager

@Module(includes = [ContextModule::class])
class SharedPrefModule {

    @Provides
    fun provideAccessTokenManager(context: Context): AccessTokenManager {
        return AccessTokenManager(context)
    }

}