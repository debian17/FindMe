package ru.debian17.findme.app.ui.menu.attribute.edit.lon

import com.arellomobile.mvp.InjectViewState
import org.osmdroid.util.GeoPoint
import ru.debian17.findme.app.dal.AttributesDataSource
import ru.debian17.findme.app.dal.CategoriesDataSource
import ru.debian17.findme.app.ext.observeOnUI
import ru.debian17.findme.app.ext.subscribeOnIO
import ru.debian17.findme.app.mvp.BasePresenter
import ru.debian17.findme.data.model.category.Category

@InjectViewState
class EditLongBarrierPresenter(private val categoriesDataSource: CategoriesDataSource,
                               private val attributesDataSource: AttributesDataSource
) : BasePresenter<EditLongBarrierView>() {


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
        viewState.showError(errorBody?.message)
    }

    fun editLongBarrier(barrierId: Int, categoryId: Int, comment: String, points: List<GeoPoint>) {
        viewState.showLoading()
        unsubscribeOnDestroy(attributesDataSource.editLongBarrier(
                barrierId,
                categoryId,
                comment,
                points)
                .subscribeOnIO()
                .doOnError {
                    errorBody = getError(it)
                }
                .observeOnUI()
                .subscribe(this::onEditeSuccess, this::onError))
    }

    private fun onEditeSuccess() {
        viewState.onEditSuccess()
    }

}