package ru.debian17.findme.app.ui.menu.route.build


import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_build_route.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import ru.debian17.findme.R
import ru.debian17.findme.app.mvp.BaseFragment


class BuildRouteFragment : BaseFragment(), BuildRouteView {

    companion object {
        const val TAG = "RouteFragmentTag"
        fun newInstance(): BuildRouteFragment {
            return BuildRouteFragment()
        }
    }

    @InjectPresenter
    lateinit var presenter: BuildRoutePresenter

    @ProvidePresenter
    fun providePresenter(): BuildRoutePresenter {
        return BuildRoutePresenter()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_build_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setMultiTouchControls(true)
        }

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun showLoading() {
    }

    override fun showMain() {

    }

    override fun showError(errorMessage: String?) {

    }

}
