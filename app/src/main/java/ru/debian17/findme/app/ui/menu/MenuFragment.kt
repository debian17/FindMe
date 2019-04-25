package ru.debian17.findme.app.ui.menu


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_menu.*
import ru.debian17.findme.R
import ru.debian17.findme.app.App
import ru.debian17.findme.app.ext.hide
import ru.debian17.findme.app.ext.show
import ru.debian17.findme.app.mvp.BaseFragment
import ru.debian17.findme.app.ui.auth.AuthFragment
import ru.debian17.findme.app.ui.menu.attribute.add.ChooseAttributeTypeDialog
import ru.debian17.findme.app.ui.menu.attribute.add.point.AddPointAttributeFragment
import ru.debian17.findme.app.ui.menu.attribute.list.AttributesFragment
import ru.debian17.findme.app.ui.menu.route.add.AddRouteFragment
import ru.debian17.findme.app.ui.menu.route.build.BuildRouteFragment

class MenuFragment : BaseFragment(), MenuView, ChooseAttributeTypeDialog.ChooseAttributeTypeListener {

    companion object {
        const val TAG = "MenuFragmentTag"
        fun newInstance(): MenuFragment {
            return MenuFragment()
        }
    }

    @InjectPresenter
    lateinit var presenter: MenuPresenter

    @ProvidePresenter
    fun providePresenter(): MenuPresenter {
        return MenuPresenter((activity!!.application as App).getDataSourceComponent()
                .provideAuthRepository())
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        llBuildRoute.setOnClickListener {
            navigator.addFragment(BuildRouteFragment.TAG)
        }

        llAddRoute.setOnClickListener {
            navigator.addFragment(AddRouteFragment.TAG)
        }

        llAttributes.setOnClickListener {
            navigator.addFragment(AttributesFragment.TAG)
        }

        llAddAttribute.setOnClickListener {
            ChooseAttributeTypeDialog.newInstance()
                    .show(childFragmentManager, ChooseAttributeTypeDialog.TAG)
        }

        llLogout.setOnClickListener {
            presenter.logout()
        }

    }

    override fun onPointAttribute() {
        navigator.addFragment(AddPointAttributeFragment.TAG)
    }

    override fun onLongAttribute() {

    }

    override fun onLogOutSuccess() {
        navigator.replaceScreen(AuthFragment.TAG)
    }

    override fun showLoading() {
        pbLoading.show()
        llMain.hide()
    }

    override fun showMain() {

    }

    override fun showError(errorMessage: String?) {

    }

}
