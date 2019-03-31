package ru.debian17.findme.app.ui.auth.registration

import android.os.Bundle
import android.os.PatternMatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_registration.*
import ru.debian17.findme.R
import ru.debian17.findme.app.App
import ru.debian17.findme.app.ext.hide
import ru.debian17.findme.app.ext.hideKeyboard
import ru.debian17.findme.app.ext.longSnackBar
import ru.debian17.findme.app.ext.show
import ru.debian17.findme.app.mvp.BaseFragment
import ru.debian17.findme.data.network.error.ErrorCode
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegistrationFragment : BaseFragment(), RegistrationView {

    companion object {
        const val TAG = "RegistrationFragmentTag"
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }

    @InjectPresenter
    lateinit var presenter: RegistrationPresenter

    @ProvidePresenter
    fun providePresenter(): RegistrationPresenter {
        return RegistrationPresenter(
                (activity!!.application as App)
                        .getDataSourceComponent()
                        .provideAuthRepository()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivBack.setOnClickListener {
            navigator.back()
        }

        btnRegistration.setOnClickListener {

            val email = etEmail.text.toString().trim()

            val isEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()

            if (!isEmail) {
                view.longSnackBar(getString(R.string.email_validation_error))
                return@setOnClickListener
            }

            val password = etPassword.text.toString().trim()

            if (password.isEmpty()) {
                view.longSnackBar(getString(R.string.password_empty_error))
                return@setOnClickListener
            }

            if (password.length < 5) {
                view.longSnackBar(getString(R.string.password_length_error))
                return@setOnClickListener
            }

            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (confirmPassword.isEmpty()) {
                view.longSnackBar(getString(R.string.confirm_password_empty_error))
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                view.longSnackBar(getString(R.string.password_confirm_different))
                return@setOnClickListener
            }

            view.hideKeyboard()
            presenter.registration(email, password)

        }

    }

    override fun onRegistrationSuccess() {
        etEmail.text.clear()
        etPassword.text.clear()
        etConfirmPassword.text.clear()

        view?.longSnackBar(getString(R.string.registration_success))
    }

    override fun onRegistrationError(code: Int) {
        when (code) {
            ErrorCode.EMAIL_ALREADY_EXIST -> {
                view?.longSnackBar(getString(R.string.email_already_exist))
            }
            ErrorCode.VALIDATION -> {
                view?.longSnackBar(getString(R.string.validation_error))
            }
        }
    }

    override fun showLoading() {
        pbLoading.show()
        llMain.hide()
    }

    override fun showMain() {
        llMain.show()
        pbLoading.hide()
    }

    override fun showError(errorMessage: String?) {

    }

}
