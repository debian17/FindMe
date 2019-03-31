package ru.debian17.findme.app.ui.auth.registration

import com.arellomobile.mvp.InjectViewState
import ru.debian17.findme.app.dal.AuthDataSource
import ru.debian17.findme.app.ext.observeOnUI
import ru.debian17.findme.app.ext.subscribeOnIO
import ru.debian17.findme.app.mvp.BasePresenter

@InjectViewState
class RegistrationPresenter(private val authDataSource: AuthDataSource) : BasePresenter<RegistrationView>() {

    fun registration(email: String, password: String) {
        viewState.showLoading()
        unsubscribeOnDestroy(
                authDataSource.registration(email, password)
                        .subscribeOnIO()
                        .doOnError {
                            errorBody = getError(it)
                        }
                        .observeOnUI()
                        .subscribe(this::onRegistrationSuccess, this::onRegistrationError)
        )
    }

    private fun onRegistrationSuccess() {
        viewState.showMain()
        viewState.onRegistrationSuccess()
    }

    private fun onRegistrationError(throwable: Throwable) {
        viewState.showMain()
        viewState.onRegistrationError(errorBody.code)
    }

}