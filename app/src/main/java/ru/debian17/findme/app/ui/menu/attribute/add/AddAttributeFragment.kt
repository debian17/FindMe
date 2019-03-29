package ru.debian17.findme.app.ui.menu.attribute.add


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.debian17.findme.R
import ru.debian17.findme.app.mvp.BaseFragment

class AddAttributeFragment : BaseFragment(), AddAttributeView {

    companion object {
        const val TAG = "AddAttributeFragmentTag"
        fun newInstance(): AddAttributeFragment {
            return AddAttributeFragment()
        }
    }

    @InjectPresenter
    lateinit var presenter: AddAttributePresenter

    @ProvidePresenter
    fun providePresenter(): AddAttributePresenter {
        return AddAttributePresenter()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_attribute, container, false)
    }

    override fun showLoading() {

    }

    override fun showMain() {

    }

    override fun showError(errorMessage: String?) {

    }

}
