package ru.debian17.findme.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.debian17.findme.app.dal.CategoriesDataSource
import ru.debian17.findme.app.dal.DatabaseSource
import ru.debian17.findme.data.model.category.Category
import ru.debian17.findme.data.model.db.model.CategoryModel
import ru.debian17.findme.data.network.WebAPIService

class CategoriesRepository(
        private val webAPIService: WebAPIService,
        private val database: DatabaseSource
) : CategoriesDataSource {

    override fun getCategories(): Single<List<Category>> {
        return database.getCategories()
                .map { categories ->
                    return@map categories.map {
                        Category(it.id, it.name, it.isPoint, it.isLong)
                    }
                }
    }

    override fun updateCategories(): Completable {
        return webAPIService.getCategories()
                .doOnSuccess { categories ->
                    database.addCategories(categories.map {
                        CategoryModel(it.id, it.name, it.isPoint, it.isLong)
                    })
                }.ignoreElement()
    }

}