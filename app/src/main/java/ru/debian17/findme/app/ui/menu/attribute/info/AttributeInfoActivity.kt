package ru.debian17.findme.app.ui.menu.attribute.info

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_attribute_info.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import ru.debian17.findme.R
import ru.debian17.findme.app.ext.getCategories
import ru.debian17.findme.app.mvp.BaseActivity
import ru.debian17.findme.data.model.attribute.LongAttribute
import ru.debian17.findme.data.model.attribute.PointAttribute
import ru.debian17.findme.data.model.route.RoutePoint

class AttributeInfoActivity : BaseActivity() {

    companion object {
        private const val ATTRIBUTE_KEY = "attributeKey"
        private const val ROUTE_POINTS_KEY = "routePointsKey"
        fun getStartIntent(
                context: Context,
                pointAttribute: PointAttribute,
                routePoints: ArrayList<RoutePoint>
        ): Intent {
            return Intent(context, AttributeInfoActivity::class.java).apply {
                putExtra(ATTRIBUTE_KEY, pointAttribute)
                putExtra(ROUTE_POINTS_KEY, routePoints)
            }
        }

        fun getStartIntent(
                context: Context,
                longAttribute: LongAttribute,
                routePoints: ArrayList<RoutePoint>
        ): Intent {
            return Intent(context, AttributeInfoActivity::class.java).apply {
                putExtra(ATTRIBUTE_KEY, longAttribute)
                putExtra(ROUTE_POINTS_KEY, routePoints)
            }
        }

    }

    private var pointAttribute: PointAttribute? = null
    private var longAttribute: LongAttribute? = null
    private var routePoints: ArrayList<RoutePoint>? = null
    private lateinit var startMarker: Marker
    private lateinit var endMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attribute_info)

        intent?.let {
            pointAttribute = it.getParcelableExtra(ATTRIBUTE_KEY) as? PointAttribute
            if (pointAttribute == null) {
                longAttribute = it.getParcelableExtra(ATTRIBUTE_KEY)
            }
            routePoints = it.getParcelableArrayListExtra(ROUTE_POINTS_KEY)
        }

        mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setMultiTouchControls(true)
        }

        if (pointAttribute != null) {

            mapView.controller.setZoom(17.0)
            val point = GeoPoint(pointAttribute!!.latitude, pointAttribute!!.longitude)
            mapView.controller.setCenter(point)

            if (routePoints != null) {

                val blueIcon = ContextCompat.getDrawable(this, R.drawable.ic_location_blue)
                startMarker = Marker(mapView)
                startMarker.icon = blueIcon
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                startMarker.title = getString(R.string.start_point)

                val greenIcon = ContextCompat.getDrawable(this, R.drawable.ic_location_green)
                endMarker = Marker(mapView)
                endMarker.icon = greenIcon
                endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                endMarker.title = getString(R.string.end_point)

                var i = 0
                val size = routePoints!!.size
                while (i < size) {

                    if (i == 0) {
                        val startPoint = GeoPoint(routePoints!![i].startLat, routePoints!![i].startLong)
                        startMarker.position = startPoint
                    }
                    if (i == size - 1) {
                        val endPoint = GeoPoint(routePoints!![i].endLat, routePoints!![i].endLong)
                        endMarker.position = endPoint
                    }

                    val line = Polyline(mapView)
                    line.title = null
                    line.infoWindow = null
                    line.paint.color = Color.BLUE
                    val item = routePoints!![i]
                    val start = GeoPoint(item.startLat, item.startLong)
                    val end = GeoPoint(item.endLat, item.endLong)
                    line.addPoint(start)
                    line.addPoint(end)
                    mapView.overlays.add(line)
                    i++
                }

                mapView.overlays.add(startMarker)
                mapView.overlays.add(endMarker)
            }

            val pointPolygon = Polygon(mapView)
            pointPolygon.points = Polygon.pointsAsCircle(point, pointAttribute!!.radius.toDouble())
            pointPolygon.fillColor = ContextCompat.getColor(this, R.color.alpha_gray)
            pointPolygon.strokeWidth = 2f
            pointPolygon.strokeColor = Color.RED
            pointPolygon.infoWindow = null
            pointPolygon.title = null

            mapView.overlays.add(pointPolygon)

            val categories = getCategories()
            val categoryId = pointAttribute!!.categoryId
            val category = categories.find { it.id == categoryId }!!

            tvCategory.text = "${getString(R.string.category)}: ${category.name}"
            tvComment.text = "${getString(R.string.comment)}: ${pointAttribute!!.comment}"
            tvLong.text = "${getString(R.string.radius)}: ${pointAttribute!!.radius} м."

        } else {


        }

        mapView.invalidate()
    }

}
