package ru.debian17.findme.app.ui.menu.route.build


import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_build_route.*
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import ru.debian17.findme.R
import ru.debian17.findme.app.App
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
        return BuildRoutePresenter(
                (activity!!.application as App).getDataSourceComponent()
                        .provideLocationRepository()
        )
    }

    private val startPoint = GeoPoint(47.23660, 39.71257)
    private val defaultZoom = 17.0

    private lateinit var myLocationMarker: Marker
    private lateinit var myCurrentLocation: GeoPoint
    private lateinit var greenMarker: Marker

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_build_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.controller.setCenter(startPoint)
        mapView.controller.setZoom(defaultZoom)

        val myLocationIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_location_accent)!!
        myLocationMarker = Marker(mapView)
        myLocationMarker.icon = myLocationIcon
        myLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)

        val greenIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_location_green)
        greenMarker = Marker(mapView)
        greenMarker.icon = greenIcon

        mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setMultiTouchControls(true)
            controller.setZoom(defaultZoom)
            controller.setCenter(startPoint)
        }

        val mapEventReceiver = object : MapEventsReceiver {
            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }

            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                if (p != null) {
                    mapView.overlays.remove(greenMarker)

                    greenMarker.position = p

                    mapView.overlays.add(greenMarker)
                    mapView.invalidate()
                }
                return true
            }

        }

        val mapEventsOverlay = MapEventsOverlay(mapEventReceiver)
        mapView.overlays.add(mapEventsOverlay)

        ivMyLocation.setOnClickListener {
            if (this::myCurrentLocation.isInitialized) {
                mapView.controller.animateTo(myCurrentLocation)
            }
        }

    }

    override fun updateLocation(location: Location) {
        myCurrentLocation = GeoPoint(location.latitude, location.longitude)

        mapView.overlays.remove(myLocationMarker)
        myLocationMarker.position = myCurrentLocation

        mapView.overlays.add(myLocationMarker)
        mapView.invalidate()
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
