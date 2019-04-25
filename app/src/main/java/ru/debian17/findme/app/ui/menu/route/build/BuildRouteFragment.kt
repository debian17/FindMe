package ru.debian17.findme.app.ui.menu.route.build


import android.graphics.Color
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
import org.osmdroid.util.Distance
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import ru.debian17.findme.R
import ru.debian17.findme.app.App
import ru.debian17.findme.app.ext.hide
import ru.debian17.findme.app.ext.longSnackBar
import ru.debian17.findme.app.ext.show
import ru.debian17.findme.app.mvp.BaseFragment
import ru.debian17.findme.app.ui.menu.route.build.edge.EdgeAttributesDialog
import ru.debian17.findme.app.util.DistanceUtil
import ru.debian17.findme.data.model.attribute.LongAttribute
import ru.debian17.findme.data.model.attribute.PointAttribute
import ru.debian17.findme.data.model.category.Category
import ru.debian17.findme.data.model.edge.EdgeInfo
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
        val dataSourceComponent = (activity!!.application as App).getDataSourceComponent()
        return BuildRoutePresenter(
            dataSourceComponent.provideLocationRepository(),
            dataSourceComponent.provideAttributesRepository(),
            dataSourceComponent.provideCategoriesRepository()
        )
    }

    private lateinit var myCurrentLocation: GeoPoint

    private lateinit var myLocationMarker: Marker
    private lateinit var startMarker: Marker
    private lateinit var endMarker: Marker

    private val routePoints = ArrayList<RoutePoint>()
    private val routeLines = ArrayList<Polyline>()
    private val pointAttributes = ArrayList<Polygon>()

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

    private val routeLineOnClickListener = Polyline.OnClickListener { polyline, mapView, eventPos ->
        if (routePoints.isNotEmpty()) {

            val selectedEdge = routePoints.minBy {
                Distance.getSquaredDistanceToLine(
                    eventPos.longitude,
                    eventPos.latitude,
                    it.startLong,
                    it.startLat,
                    it.endLong,
                    it.endLat
                )
            }

            if (selectedEdge != null && selectedEdge.attributes.isNotEmpty()) {
                presenter.getAttributesOfEdge(selectedEdge.edgeId)
            }

        }
        return@OnClickListener true
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
        routeLines.forEach {
            mapView.overlays.remove(it)
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
        routeLines.forEach {
            mapView.overlays.remove(it)
        }
        mapView.invalidate()

        ivClearRoute.hide()

        isBuildFromCurLocation = false
        isStartPointSelected = false
        isEndPointSelected = false

        isBuildFromDot = true
    }

    override fun onBuildRoute(routePoints: List<RoutePoint>) {
        this.routePoints.addAll(routePoints)
        var i = 0
        val size = routePoints.size

        var distance = 0.0

        while (i < size) {
            val line = Polyline(mapView)
            line.title = null
            line.infoWindow = null
            line.setOnClickListener(routeLineOnClickListener)

            val item = routePoints[i]
            val start = GeoPoint(item.startLat, item.startLong)
            val end = GeoPoint(item.endLat, item.endLong)

            when (item.attributes.size) {
                0 -> line.paint.color = Color.BLUE
                1 -> line.paint.color = Color.YELLOW
                else -> line.paint.color = Color.RED
            }

            line.addPoint(start)
            line.addPoint(end)

            routeLines.add(line)

            distance += line.distance

            mapView.overlays.add(line)

            i++
        }
        mapView.invalidate()

        llRouteInfo.show()
        val distanceTemplate: String = if (distance >= 1000) {
            getString(R.string.distance_template_kilo_meters)
        } else {
            getString(R.string.distance_template_meters)
        }
        val distanceResult = DistanceUtil.roundDistance(distance, 3)
        tvDistance.text = String.format(distanceTemplate, distanceResult)
        llRouteInfo.show()
    }

    private fun clearRoute() {
        mapView.overlays.remove(startMarker)
        mapView.overlays.remove(endMarker)
        routeLines.forEach {
            mapView.overlays.remove(it)
        }
        pointAttributes.forEach {
            mapView.overlays.remove(it)
        }

        llRouteInfo.hide()

        routePoints.clear()
        pointAttributes.clear()

        isBuildFromCurLocation = false
        isBuildFromDot = false

        isStartPointSelected = false
        isEndPointSelected = false

        mapView.invalidate()
        ivClearRoute.hide()
    }

    override fun onEdgeInfoLoaded(edgeInfo: EdgeInfo, categories: List<Category>) {
        val pointAttributes = ArrayList<PointAttribute>(edgeInfo.pointAttributes)
        val longAttributes = ArrayList<LongAttribute>(edgeInfo.longAttributes)
        EdgeAttributesDialog.newInstance(pointAttributes, longAttributes, ArrayList(categories), routePoints)
            .show(childFragmentManager, EdgeAttributesDialog.TAG)
    }

    override fun onBuildRouteError(code: Int) {
        clearRoute()
        when (code) {
            else -> {
                view?.longSnackBar(getString(R.string.default_error_message))
            }
        }

    }

    override fun onAttributesOfEdgeError(code: Int) {

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
