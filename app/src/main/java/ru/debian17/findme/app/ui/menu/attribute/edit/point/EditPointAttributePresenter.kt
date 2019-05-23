package ru.debian17.findme.app.ui.menu.attribute.edit.point

import com.arellomobile.mvp.InjectViewState
import ru.debian17.findme.app.dal.AttributesDataSource
import ru.debian17.findme.app.dal.CategoriesDataSource
import ru.debian17.findme.app.ext.observeOnUI
import ru.debian17.findme.app.ext.subscribeOnIO
import ru.debian17.findme.app.mvp.BasePresenter
import ru.debian17.findme.data.model.category.Category

@InjectViewState
class EditPointAttributePresenter(
        private val categoriesDataSource: CategoriesDataSource,
        private val attributesDataSource: AttributesDataSource
) :
        BasePresenter<EditPointAttributeView>() {

    override fun onFirstViewAttach() {
        viewState.showLoading()
        unsubscribeOnDestroy(
                categoriesDataSource.getCategories()
                        .subscribeOnIO()
                        .doOnError {
                            errorBody = getError(it)
                        }
                        .observeOnUI()
                        .subscribe(this::onCategoriesLoaded, this::onError)
        )
    }

    private fun onCategoriesLoaded(categories: List<Category>) {
        viewState.showMain()
        viewState.onCategoriesLoaded(categories)
    }

    private fun onError(throwable: Throwable) {
        viewState.showMain()
        viewState.showError(errorBody?.message)
    }

    fun editLocalBarrier(barrierId: Int,
                         categoryId: Int,
                         radius: Double,
                         comment: String,
                         latitude: Double,
                         longitude: Double) {
        viewState.showLoading()
        unsubscribeOnDestroy(attributesDataSource.editLocalBarrier(barrierId,
                categoryId,
                radius,
                comment,
                latitude,
                longitude)
                .subscribeOnIO()
                .observeOnUI()
                .subscribe(this::onEditSuccess, this::onError))

    }

    private fun onEditSuccess() {
        viewState.showMain()
        viewState.onEditSuccess()
    }

}