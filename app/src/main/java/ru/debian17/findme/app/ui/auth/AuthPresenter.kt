package ru.debian17.findme.app.ui.auth

import com.arellomobile.mvp.InjectViewState
import ru.debian17.findme.app.dal.AuthDataSource
import ru.debian17.findme.app.ext.observeOnUI
import ru.debian17.findme.app.ext.subscribeOnIO
import ru.debian17.findme.app.mvp.BasePresenter

@InjectViewState
class AuthPresenter(private val authDataSource: AuthDataSource) : BasePresenter<AuthView>() {

    override fun onFirstViewAttach() {
        viewState.showLoading()
        unsubscribeOnDestroy(
                authDataSource.isUserLogin()
                        .subscribeOnIO()
                        .observeOnUI()
                        .subscribe(this::onAuthSuccess, this::onAuthError)
        )
    }

    private fun onAuthSuccess(isUserLogin: Boolean) {
        if (isUserLogin) {
            viewState.showMenu()
        } else {
            viewState.showMain()
            viewState.checkPermission()
        }
    }

    private fun onAuthError(throwable: Throwable) {

    }

}