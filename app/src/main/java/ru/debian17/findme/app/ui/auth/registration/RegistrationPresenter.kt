package ru.debian17.findme.app.ui.auth.registration

import com.arellomobile.mvp.InjectViewState
import ru.debian17.findme.app.dal.AuthDataSource
import ru.debian17.findme.app.mvp.BasePresenter

@InjectViewState
class RegistrationPresenter(private val authDataSource: AuthDataSource) : BasePresenter<RegistrationView>() {


}