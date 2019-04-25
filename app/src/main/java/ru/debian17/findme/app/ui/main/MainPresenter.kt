package ru.debian17.findme.app.ui.main

import com.arellomobile.mvp.InjectViewState
import ru.debian17.findme.app.dal.CategoriesDataSource
import ru.debian17.findme.app.ext.observeOnUI
import ru.debian17.findme.app.ext.subscribeOnIO
import ru.debian17.findme.app.mvp.BasePresenter

@InjectViewState
class MainPresenter(private val categoriesRepository: CategoriesDataSource) : BasePresenter<MainView>() {

    override fun onFirstViewAttach() {
        unsubscribeOnDestroy(
            categoriesRepository.updateCategories()
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({}, {})
        )
    }

}