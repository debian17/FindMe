package ru.debian17.findme.app.ui.menu.route.add


import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_add_route.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import ru.debian17.findme.R
import ru.debian17.findme.app.App
import ru.debian17.findme.app.ext.hide
import ru.debian17.findme.app.ext.show
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
        val dataSourceComponent = (activity!!.application as App).getDataSourceComponent()
        return AddRoutePresenter(dataSourceComponent.provideLocationRepository())
    }

    private lateinit var myLocationMarker: Marker
    private lateinit var lastPosition: GeoPoint

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setMultiTouchControls(true)
            controller.setZoom(defaultZoom)
        }

        val myLocationIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_location_accent)!!
        myLocationMarker = Marker(mapView).apply {
            icon = myLocationIcon
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            title = getString(R.string.current_location)
        }

        btnSaveRoute.setOnClickListener {
            presenter.saveRoute()
        }

    }

    override fun onLocationChanged(location: Location) {
        if (!this::lastPosition.isInitialized) {
            lastPosition = GeoPoint(location.latitude, location.longitude)
        } else {
            val newPoint = GeoPoint(location.latitude, location.longitude)

            mapView.overlays.remove(myLocationMarker)
            myLocationMarker.position = newPoint
            mapView.overlays.add(myLocationMarker)
            mapView.controller.setCenter(newPoint)

            val line = Polyline(mapView).apply {
                color = Color.BLUE
                title = null
                infoWindow = null
                addPoint(lastPosition)
                addPoint(newPoint)
            }

            lastPosition = newPoint

            mapView.overlays.add(line)
            mapView.invalidate()
        }
    }

    override fun onAddRouteSuccess() {
        activity?.onBackPressed()
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
