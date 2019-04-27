package ru.debian17.findme.app.ui.menu.attribute.add.lon

import com.arellomobile.mvp.InjectViewState
import ru.debian17.findme.app.dal.AttributesDataSource
import ru.debian17.findme.app.dal.CategoriesDataSource
import ru.debian17.findme.app.ext.observeOnUI
import ru.debian17.findme.app.ext.subscribeOnIO
import ru.debian17.findme.app.mvp.BasePresenter
import ru.debian17.findme.data.model.category.Category


@InjectViewState
class AddLongAttributePresenter(
        private val categoriesDataSource: CategoriesDataSource,
        private val attributesDataSource: AttributesDataSource
) : BasePresenter<AddLongAttributeView>() {

    override fun onFirstViewAttach() {
        viewState.showLoading()
        unsubscribeOnDestroy(categoriesDataSource.getCategories()
                .subscribeOnIO()
                .doOnError {
                    errorBody = getError(it)
                }
                .observeOnUI()
                .subscribe(this::onCategoriesLoaded, this::onError))
    }

    private fun onCategoriesLoaded(categories: List<Category>) {
        viewState.showMain()
        viewState.onCategoriesLoaded(categories)
    }

    private fun onError(throwable: Throwable) {
        viewState.showMain()
        viewState.onError(errorBody?.message)
    }


}