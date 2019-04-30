package ru.debian17.findme.app.ui.menu.attribute.info.lon

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_long_attribute_info.*
import kotlinx.android.synthetic.main.activity_long_attribute_info.mapView
import kotlinx.android.synthetic.main.activity_long_attribute_info.tvCategory
import kotlinx.android.synthetic.main.activity_long_attribute_info.tvComment
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Polyline
import ru.debian17.findme.R
import ru.debian17.findme.app.mvp.BaseActivity
import ru.debian17.findme.app.util.DistanceUtil
import ru.debian17.findme.data.model.attribute.LongAttributeInfo
import ru.debian17.findme.data.model.category.Category
import ru.debian17.findme.data.model.route.RoutePoint

class LongAttributeInfoActivity : BaseActivity() {

    companion object {
        private const val LONG_ATTRIBUTE_KEY = "pointAttributeKey"
        private const val CATEGORY_KEY = "categoryKey"
        private const val ROUTE_POINTS_KEY = "routePointsKey"
        fun getStartIntent(
                context: Context,
                longAttribute: LongAttributeInfo,
                category: Category,
                routePoints: ArrayList<RoutePoint>?
        ): Intent {
            return Intent(context, LongAttributeInfoActivity::class.java).apply {
                putExtra(LONG_ATTRIBUTE_KEY, longAttribute)
                putExtra(CATEGORY_KEY, category)
                putExtra(ROUTE_POINTS_KEY, routePoints)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_long_attribute_info)

        val longAttribute = intent!!.getParcelableExtra<LongAttributeInfo>(LONG_ATTRIBUTE_KEY)
        val category = intent!!.getParcelableExtra<Category>(CATEGORY_KEY)
        val routePoints = intent!!.getParcelableArrayListExtra<RoutePoint>(ROUTE_POINTS_KEY)

        mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setMultiTouchControls(true)
            controller.setCenter(defaultPoint)
            controller.setZoom(defaultZoom)
        }

        if (routePoints != null) {
            var i = 0
            val size = routePoints.size

            while (i < size) {

                val point = routePoints[i]

                val startPoint = GeoPoint(point.startLat, point.startLong)
                val endPoint = GeoPoint(point.endLat, point.endLong)

                val line = Polyline(mapView).apply {
                    color = Color.BLUE
                    title = null
                    infoWindow = null
                    addPoint(startPoint)
                    addPoint(endPoint)
                }

                mapView.overlays.add(line)
                i++
            }
        }

        var length = 0.0
        var i = 0
        val edges = longAttribute.edges
        val edgesCount = edges.size
        while (i < edgesCount) {

            val edge = edges[i]
            length += edge.length

            val startPoint = GeoPoint(edge.startLat, edge.startLon)
            val endPoint = GeoPoint(edge.endLat, edge.endLon)

            val line = Polyline(mapView).apply {
                color = Color.RED
                title = null
                infoWindow = null
                addPoint(startPoint)
                addPoint(endPoint)
            }

            mapView.overlays.add(line)

            i++
        }

        mapView.invalidate()

        tvCategory.text = "${getString(R.string.category)}: ${category.name}"

        val distanceTemplate: String = if (length >= 1000) {
            getString(R.string.length_template_kilo_meters)
        } else {
            getString(R.string.length_template_meters)
        }
        val distanceResult = DistanceUtil.roundDistance(length, 3)
        tvLong.text = String.format(distanceTemplate, distanceResult)

        tvComment.text = "${getString(R.string.comment)}: ${longAttribute!!.comment}"

    }
}
