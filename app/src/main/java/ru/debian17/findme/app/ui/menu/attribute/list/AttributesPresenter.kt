package ru.debian17.findme.app.ui.menu.attribute.list

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import ru.debian17.findme.app.dal.AttributesDataSource
import ru.debian17.findme.app.dal.CategoriesDataSource
import ru.debian17.findme.app.ext.observeOnUI
import ru.debian17.findme.app.ext.subscribeOnIO
import ru.debian17.findme.app.mvp.BasePresenter
import ru.debian17.findme.data.model.attribute.AttributeContainer
import ru.debian17.findme.data.model.category.Category

@InjectViewState
class AttributesPresenter(private val categoryDataSource: CategoriesDataSource,
                          private val attributesDataSource: AttributesDataSource) : BasePresenter<AttributesView>() {

    override fun onFirstViewAttach() {
        viewState.showLoading()
        unsubscribeOnDestroy(getAttributes())
    }

    private fun onDataLoaded(data: Pair<List<Category>, AttributeContainer>) {
        viewState.showMain()
        viewState.onDataLoaded(data.first, data.second)
    }

    private fun onError(throwable: Throwable) {
        viewState.showMain()
        viewState.onError(errorBody?.message)
    }

    fun deleteAttribute(id: Int) {
        viewState.showLoading()
        unsubscribeOnDestroy(attributesDataSource.deleteAttribute(id)
                .subscribeOnIO()
                .doOnError {
                    errorBody = getError(it)
                }
                .observeOnUI()
                .subscribe(this::onAttributeDeleted, this::onError))
    }

    private fun onAttributeDeleted() {
        unsubscribeOnDestroy(getAttributes())
    }

    private fun getAttributes(): Disposable {
        val categoriesSource = categoryDataSource.getCategories()
        val attributesSource = attributesDataSource.getAttributes()

        return Single.zip(categoriesSource, attributesSource,
                BiFunction<List<Category>, AttributeContainer, Pair<List<Category>, AttributeContainer>> { t1, t2 ->
                    return@BiFunction Pair(t1, t2)
                }).subscribeOnIO()
                .doOnError {
                    errorBody = getError(it)
                }
                .observeOnUI()
                .subscribe(this::onDataLoaded, this::onError)
    }

}