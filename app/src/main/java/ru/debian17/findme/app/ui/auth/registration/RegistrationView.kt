package ru.debian17.findme.app.ui.auth.registration

import ru.debian17.findme.app.mvp.BaseView

interface RegistrationView : BaseView {

    fun onRegistrationSuccess()

    fun onRegistrationError(code: Int)

}