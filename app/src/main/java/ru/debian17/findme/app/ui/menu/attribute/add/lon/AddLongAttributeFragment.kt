package ru.debian17.findme.app.ui.menu.attribute.add.lon


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_add_long_attribute.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import ru.debian17.findme.R
import ru.debian17.findme.app.App
import ru.debian17.findme.app.ext.hide
import ru.debian17.findme.app.ext.longSnackBar
import ru.debian17.findme.app.ext.show
import ru.debian17.findme.app.mvp.BaseFragment
import ru.debian17.findme.data.model.category.Category


class AddLongAttributeFragment : BaseFragment(), AddLongAttributeView {

    companion object {
        const val TAG = "AddLongAttributeFragmentTag"
        fun newInstance(): AddLongAttributeFragment {
            return AddLongAttributeFragment()
        }
    }

    @InjectPresenter
    lateinit var presenter: AddLongAttributePresenter

    @ProvidePresenter
    fun providePresenter(): AddLongAttributePresenter {
        val dataSourceComponent = (activity!!.applicationContext as App).getDataSourceComponent()
        return AddLongAttributePresenter(
                dataSourceComponent.provideCategoriesRepository(),
                dataSourceComponent.provideAttributesRepository()
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_long_attribute, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setMultiTouchControls(true)

            controller.setCenter(defaultPoint)
            controller.setZoom(defaultZoom)
        }

    }

    override fun onCategoriesLoaded(categories: List<Category>) {

    }

    override fun onError(errorMessage: String?) {
        view?.longSnackBar(errorMessage)
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
