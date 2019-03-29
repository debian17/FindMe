package ru.debian17.findme.app.ui.auth.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_registration.*
import ru.debian17.findme.R
import ru.debian17.findme.app.App
import ru.debian17.findme.app.mvp.BaseFragment

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

    }

    override fun showLoading() {

    }

    override fun showMain() {

    }

    override fun showError(errorMessage: String?) {

    }

}
