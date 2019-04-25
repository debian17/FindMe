package ru.debian17.findme.data.model.db.dao

import androidx.room.*
import io.reactivex.Single
import ru.debian17.findme.data.model.db.model.CategoryModel

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM CategoryModel")
    fun getCategories(): Single<List<CategoryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategories(categories: List<CategoryModel>)

}