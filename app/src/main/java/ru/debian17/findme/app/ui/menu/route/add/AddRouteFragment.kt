package ru.debian17.findme.app.ui.menu.route.add


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.debian17.findme.R
import ru.debian17.findme.app.mvp.BaseFragment


class AddRouteFragment : BaseFragment(), AddRouteView {

    companion object {
        const val TAG = "AddRouteFragmentTag"
        fun newInstance(): AddRouteFragment {
            return AddRouteFragment()
        }
    }

    @InjectPresenter
    lateinit var presenter: AddRoutePresenter

    @ProvidePresenter
    fun providePresenter(): AddRoutePresenter {
        return AddRoutePresenter()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_route, container, false)
    }

    override fun showLoading() {

    }

    override fun showMain() {

    }

    override fun showError(errorMessage: String?) {

    }

}
