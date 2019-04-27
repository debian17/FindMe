package ru.debian17.findme.app.ui.menu.attribute.info.point

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_point_attribute_info.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import ru.debian17.findme.R
import ru.debian17.findme.app.mvp.BaseActivity
import ru.debian17.findme.data.model.attribute.PointAttribute
import ru.debian17.findme.data.model.category.Category
import ru.debian17.findme.data.model.route.RoutePoint

class PointAttributeInfoActivity : BaseActivity() {

    companion object {
        private const val POINT_ATTRIBUTE_KEY = "pointAttributeKey"
        private const val CATEGORY_KEY = "categoryKey"
        private const val ROUTE_POINTS_KEY = "routePointsKey"
        fun getStartIntent(
                context: Context,
                pointAttribute: PointAttribute,
                category: Category,
                routePoints: ArrayList<RoutePoint>?
        ): Intent {
            return Intent(context, PointAttributeInfoActivity::class.java).apply {
                putExtra(POINT_ATTRIBUTE_KEY, pointAttribute)
                putExtra(CATEGORY_KEY, category)
                putExtra(ROUTE_POINTS_KEY, routePoints)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_attribute_info)

        val pointAttribute = intent!!.getParcelableExtra<PointAttribute>(POINT_ATTRIBUTE_KEY)
        val category = intent!!.getParcelableExtra<Category>(CATEGORY_KEY)
        val routePoints = intent!!.getParcelableArrayListExtra<RoutePoint>(ROUTE_POINTS_KEY)

        mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setMultiTouchControls(true)
        }

        val point = GeoPoint(pointAttribute!!.latitude, pointAttribute.longitude)
        mapView.controller.setCenter(point)
        mapView.controller.setZoom(defaultZoom)

        if (routePoints != null) {

            val blueIcon = ContextCompat.getDrawable(this, R.drawable.ic_location_blue)
            val startMarker = Marker(mapView)
            startMarker.apply {
                icon = blueIcon
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                title = getString(R.string.start_point)

                val startRoutePoint = routePoints.first()
                val startPoint = GeoPoint(startRoutePoint.startLat, startRoutePoint.startLong)
                position = startPoint
            }

            val greenIcon = ContextCompat.getDrawable(this, R.drawable.ic_location_green)
            val endMarker = Marker(mapView)
            endMarker.apply {
                icon = greenIcon
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                title = getString(R.string.end_point)

                val endRoutePoint = routePoints.last()
                val endPoint = GeoPoint(endRoutePoint.endLat, endRoutePoint.endLong)
                position = endPoint
            }

            mapView.overlays.add(startMarker)
            mapView.overlays.add(endMarker)

            var i = 0
            val size = routePoints.size
            while (i < size) {
                val line = Polyline(mapView)
                line.title = null
                line.infoWindow = null
                line.paint.color = Color.BLUE
                val item = routePoints[i]
                val start = GeoPoint(item.startLat, item.startLong)
                val end = GeoPoint(item.endLat, item.endLong)
                line.addPoint(start)
                line.addPoint(end)
                mapView.overlays.add(line)
                i++
            }
        }

        val pointPolygon = Polygon(mapView)
        pointPolygon.apply {
            points = Polygon.pointsAsCircle(point, pointAttribute.radius.toDouble())
            fillColor = ContextCompat.getColor(this@PointAttributeInfoActivity, R.color.alpha_gray)
            strokeWidth = 2f
            strokeColor = Color.RED
            infoWindow = null
            title = null
        }

        tvCategory.text = "${getString(R.string.category)}: ${category.name}"
        tvComment.text = "${getString(R.string.comment)}: ${pointAttribute!!.comment}"
        tvRadius.text = "${getString(R.string.radius)}: ${pointAttribute!!.radius} м."

        mapView.apply {
            overlays.add(pointPolygon)
            invalidate()
        }
    }

}
