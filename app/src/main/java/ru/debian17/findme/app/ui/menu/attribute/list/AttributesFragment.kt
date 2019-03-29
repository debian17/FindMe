package ru.debian17.findme.app.ui.menu.attribute.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter

import ru.debian17.findme.R
import ru.debian17.findme.app.mvp.BaseFragment

class AttributesFragment : BaseFragment(), AttributesView {

    companion object {
        const val TAG = "AttributesFragmentTag"
        fun newInstance(): AttributesFragment {
            return AttributesFragment()
        }
    }

    @InjectPresenter
    lateinit var presenter: AttributesPresenter

    @ProvidePresenter
    fun providePresenter(): AttributesPresenter {
        return AttributesPresenter()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_attributes, container, false)
    }

    override fun showLoading() {

    }

    override fun showMain() {

    }

    override fun showError(errorMessage: String?) {

    }

}
