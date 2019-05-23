package ru.debian17.findme.app.ui.menu.attribute.edit.point

import ru.debian17.findme.app.mvp.BaseView
import ru.debian17.findme.data.model.category.Category

interface EditPointAttributeView : BaseView {

    fun onCategoriesLoaded(categories: List<Category>)

    fun onEditSuccess()

}