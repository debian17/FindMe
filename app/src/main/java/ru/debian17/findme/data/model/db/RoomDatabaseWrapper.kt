package ru.debian17.findme.data.model.db

import io.reactivex.Single
import ru.debian17.findme.app.dal.DatabaseSource
import ru.debian17.findme.data.model.db.model.CategoryModel

class RoomDatabaseWrapper(private val appDatabase: AppDatabase) : DatabaseSource {

    override fun getCategories(): Single<List<CategoryModel>> {
        return appDatabase.getCategoriesDao().getCategories()
    }

    override fun addCategories(categories: List<CategoryModel>) {
        appDatabase.getCategoriesDao().addCategories(categories)
    }

}