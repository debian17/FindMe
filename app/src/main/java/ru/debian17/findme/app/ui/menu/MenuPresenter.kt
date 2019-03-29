package ru.debian17.findme.app.ui.menu

import com.arellomobile.mvp.InjectViewState
import ru.debian17.findme.app.dal.AuthDataSource
import ru.debian17.findme.app.ext.observeOnUI
import ru.debian17.findme.app.ext.subscribeOnIO
import ru.debian17.findme.app.mvp.BasePresenter

@InjectViewState
class MenuPresenter(private val authDataSource: AuthDataSource) : BasePresenter<MenuView>() {

    fun logout() {
        viewState.showLoading()
        unsubscribeOnDestroy(authDataSource.logout()
                .subscribeOnIO()
                .observeOnUI()
                .subscribe(this::onLogoutSuccess, this::onLogoutError))
    }

    private fun onLogoutSuccess() {
        viewState.onLogOutSuccess()
    }

    private fun onLogoutError(throwable: Throwable) {

    }

}