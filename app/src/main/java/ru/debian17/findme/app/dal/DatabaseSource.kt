package ru.debian17.findme.app.dal

import io.reactivex.Single
import ru.debian17.findme.data.model.db.model.CategoryModel

interface DatabaseSource {

    fun getCategories(): Single<List<CategoryModel>>

    fun addCategories(categories: List<CategoryModel>)

}