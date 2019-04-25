package ru.debian17.findme.data.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.debian17.findme.data.model.db.dao.CategoriesDao
import ru.debian17.findme.data.model.db.model.CategoryModel

@Database(entities = [CategoryModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object{
        const val DATABASE_NAME = "AppDatabase"
    }

    abstract fun getCategoriesDao(): CategoriesDao

}