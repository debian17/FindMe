package ru.debian17.findme.app.dal

import io.reactivex.Completable
import io.reactivex.Single
import ru.debian17.findme.data.model.category.Category

interface CategoriesDataSource {

    fun getCategories(): Single<List<Category>>

    fun updateCategories(): Completable

}