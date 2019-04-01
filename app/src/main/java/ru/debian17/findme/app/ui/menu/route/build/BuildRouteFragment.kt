package ru.debian17.findme.app.ui.menu.route.build


import android.graphics.DashPathEffect
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
import org.osmdroid.views.overlay.Polyline
import ru.debian17.findme.R
import ru.debian17.findme.app.App
import ru.debian17.findme.app.ext.hide
import ru.debian17.findme.app.ext.longSnackBar
import ru.debian17.findme.app.ext.show
import ru.debian17.findme.app.mvp.BaseFragment
import ru.debian17.findme.app.util.DistanceUtil
import ru.debian17.findme.data.mapper.RoutePointMapper
import ru.debian17.findme.data.model.route.RoutePoint


class BuildRouteFragment : BaseFragment(), BuildRouteView, BuildRouteDialog.BuildRouteListener {

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

    private val defaultPoint = GeoPoint(47.23660, 39.71257)
    private val defaultZoom = 17.0

    private lateinit var myCurrentLocation: GeoPoint

    private lateinit var myLocationMarker: Marker
    private lateinit var startMarker: Marker
    private lateinit var endMarker: Marker
    private lateinit var routeLine: Polyline

    private var isBuildFromCurLocation = false
    private var isBuildFromDot = false

    private var isStartPointSelected = false
    private var isEndPointSelected = false

    private val mapEventsReceiver = object : MapEventsReceiver {
        override fun longPressHelper(p: GeoPoint?): Boolean {
            return false
        }

        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
            if (p != null) {

                if (isBuildFromCurLocation) {
                    isBuildFromCurLocation = false
                    mapView.overlays.remove(endMarker)
                    endMarker.position = p
                    mapView.overlays.add(endMarker)
                    mapView.invalidate()

                    ivClearRoute.show()

                    if (this@BuildRouteFragment::myCurrentLocation.isInitialized) {
                        presenter.buildRoute(myCurrentLocation, p)
                    }
                }

                if (isBuildFromDot) {
                    if (isStartPointSelected) {
                        if (!isEndPointSelected) {
                            isEndPointSelected = true

                            endMarker.position = p
                            mapView.overlays.add(endMarker)
                            mapView.invalidate()

                            ivClearRoute.show()

                            presenter.buildRoute(startMarker.position, endMarker.position)
                        }
                    } else {
                        isStartPointSelected = true
                        startMarker.position = p
                        mapView.overlays.add(startMarker)
                        mapView.invalidate()
                    }
                }
            }
            return true
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_build_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.controller.setCenter(defaultPoint)
        mapView.controller.setZoom(defaultZoom)

        val myLocationIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_location_accent)!!
        myLocationMarker = Marker(mapView)
        myLocationMarker.icon = myLocationIcon
        myLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        myLocationMarker.title = getString(R.string.current_location)

        val blueIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_location_blue)
        startMarker = Marker(mapView)
        startMarker.icon = blueIcon
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        startMarker.title = getString(R.string.start_point)

        val greenIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_location_green)
        endMarker = Marker(mapView)
        endMarker.icon = greenIcon
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        endMarker.title = getString(R.string.end_point)

        routeLine = Polyline(mapView)
        routeLine.paint.color = ContextCompat.getColor(context!!, R.color.blue)

        mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setMultiTouchControls(true)
            controller.setZoom(defaultZoom)
            controller.setCenter(defaultPoint)

            val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
            mapView.overlays.add(mapEventsOverlay)
        }

        ivMyLocation.setOnClickListener {
            if (this::myCurrentLocation.isInitialized) {
                mapView.controller.animateTo(myCurrentLocation)
            }
        }

        ivBuildDirection.setOnClickListener {
            if (this::myCurrentLocation.isInitialized) {
                BuildRouteDialog.newInstance(true)
                        .show(childFragmentManager, BuildRouteDialog.TAG)
            } else {
                BuildRouteDialog.newInstance(false)
                        .show(childFragmentManager, BuildRouteDialog.TAG)
            }
        }

        ivClearRoute.setOnClickListener {
            clearRoute()
        }

    }

    override fun updateLocation(location: Location) {
        myCurrentLocation = GeoPoint(location.latitude, location.longitude)

        mapView.overlays.remove(myLocationMarker)
        myLocationMarker.position = myCurrentLocation

        mapView.overlays.add(myLocationMarker)
        mapView.invalidate()
    }

    override fun onFromCurrentLocation() {
        mapView.overlays.remove(startMarker)
        mapView.overlays.remove(endMarker)
        if (this::routeLine.isInitialized) {
            mapView.overlays.remove(routeLine)
        }
        mapView.invalidate()

        ivClearRoute.hide()

        isBuildFromDot = false
        isStartPointSelected = false
        isEndPointSelected = false

        isBuildFromCurLocation = true
    }

    override fun onFromSelectedDot() {
        mapView.overlays.remove(startMarker)
        mapView.overlays.remove(endMarker)
        if (this::routeLine.isInitialized) {
            mapView.overlays.remove(routeLine)
        }
        mapView.invalidate()

        ivClearRoute.hide()

        isBuildFromCurLocation = false
        isStartPointSelected = false
        isEndPointSelected = false

        isBuildFromDot = true
    }

    override fun onBuildRouteError(code: Int) {
        clearRoute()

        when (code) {
            else -> {
                view?.longSnackBar(getString(R.string.default_error_message))
            }
        }

    }

    override fun onBuildRoute(routePoints: List<RoutePoint>) {
        val routeMapper = RoutePointMapper()
        val geoPoints = routeMapper.mapAll(routePoints)
        routeLine.setPoints(geoPoints)

        routeLine.setOnClickListener { polyline, mapView, eventPos ->

            return@setOnClickListener true
        }

        mapView.overlays.add(routeLine)
        mapView.invalidate()

        llRouteInfo.show()
        val distanceTemplate: String = if (routeLine.distance >= 1000) {
            getString(R.string.distance_template_kilo_meters)
        } else {
            getString(R.string.distance_template_meters)
        }
        val distance = DistanceUtil.roundDistance(routeLine.distance, 3)
        tvDistance.text = String.format(distanceTemplate, distance)
    }

    private fun clearRoute() {
        mapView.overlays.remove(startMarker)
        mapView.overlays.remove(endMarker)

        if (this::routeLine.isInitialized) {
            mapView.overlays.remove(routeLine)
        }

        isBuildFromCurLocation = false
        isBuildFromDot = false

        isStartPointSelected = false
        isEndPointSelected = false

        mapView.invalidate()
        ivClearRoute.hide()
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
        pbLoading.show()
        clMain.hide()
    }

    override fun showMain() {
        clMain.show()
        pbLoading.hide()
    }

    override fun showError(errorMessage: String?) {

    }

}
