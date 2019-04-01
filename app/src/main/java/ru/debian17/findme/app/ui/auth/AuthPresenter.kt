package ru.debian17.findme.app.ui.auth

import com.arellomobile.mvp.InjectViewState
import ru.debian17.findme.app.dal.AuthDataSource
import ru.debian17.findme.app.ext.observeOnUI
import ru.debian17.findme.app.ext.subscribeOnIO
import ru.debian17.findme.app.mvp.BasePresenter
import ru.debian17.findme.data.network.error.ErrorCode

@InjectViewState
class AuthPresenter(private val authDataSource: AuthDataSource) : BasePresenter<AuthView>() {

    override fun onFirstViewAttach() {
        viewState.showLoading()
        unsubscribeOnDestroy(
                authDataSource.isUserLogin()
                        .subscribeOnIO()
                        .observeOnUI()
                        .subscribe(this::onLoginSuccess, this::onLoginError)
        )
    }

    private fun onLoginSuccess(isUserLogin: Boolean) {
        if (isUserLogin) {
            viewState.showMenu()
        } else {
            viewState.showMain()
            viewState.checkPermission()
        }
    }

    private fun onLoginError(throwable: Throwable) {

    }

    fun auth(email: String, password: String) {
        viewState.showLoading()
        unsubscribeOnDestroy(authDataSource.auth(email, password)
                .subscribeOnIO()
                .doOnError {
                    errorBody = getError(it)
                }
                .observeOnUI()
                .subscribe(this::onAuthSuccess, this::onAuthError))
    }

    private fun onAuthSuccess() {
        viewState.showMenu()
    }

    private fun onAuthError(throwable: Throwable) {
        viewState.showMain()
        viewState.onAuthError(errorBody?.code ?: ErrorCode.UNKNOWN_ERROR)
    }

}