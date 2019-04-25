package ru.debian17.findme.app.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.debian17.findme.data.model.db.AppDatabase
import ru.debian17.findme.app.dal.DatabaseSource
import ru.debian17.findme.data.model.db.RoomDatabaseWrapper
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): DatabaseSource {
        val appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .build()
        return RoomDatabaseWrapper(appDatabase)
    }

}