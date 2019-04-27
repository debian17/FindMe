package ru.debian17.findme.app.ui.menu.attribute.info.lon

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_long_attribute_info.*
import kotlinx.android.synthetic.main.activity_long_attribute_info.mapView
import kotlinx.android.synthetic.main.activity_long_attribute_info.tvCategory
import kotlinx.android.synthetic.main.activity_long_attribute_info.tvComment
import kotlinx.android.synthetic.main.activity_point_attribute_info.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.Distance
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Polyline
import ru.debian17.findme.R
import ru.debian17.findme.app.mvp.BaseActivity
import ru.debian17.findme.data.model.attribute.LongAttribute
import ru.debian17.findme.data.model.category.Category
import ru.debian17.findme.data.model.route.RoutePoint

class LongAttributeInfoActivity : BaseActivity() {

    companion object {
        private const val LONG_ATTRIBUTE_KEY = "pointAttributeKey"
        private const val CATEGORY_KEY = "categoryKey"
        private const val ROUTE_POINTS_KEY = "routePointsKey"
        fun getStartIntent(
            context: Context,
            longAttribute: LongAttribute,
            category: Category,
            routePoints: ArrayList<RoutePoint>
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

        val longAttribute = intent!!.getParcelableExtra<LongAttribute>(LONG_ATTRIBUTE_KEY)
        val category = intent!!.getParcelableExtra<Category>(CATEGORY_KEY)
        val routePoints = intent!!.getParcelableArrayListExtra<RoutePoint>(ROUTE_POINTS_KEY)

        val startPoint = GeoPoint(longAttribute.startLat, longAttribute.startLon)
        val endPoint = GeoPoint(longAttribute.endLat, longAttribute.endLon)
        val centerPoint = GeoPoint.fromCenterBetween(startPoint, endPoint)

        mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setMultiTouchControls(true)
            controller.setCenter(centerPoint)
            controller.setZoom(17.0)
        }

        val attributeLine = Polyline(mapView).apply {
            paint.color = Color.RED
            title = null
            infoWindow = null
            addPoint(startPoint)
            addPoint(endPoint)
        }

        mapView.overlays.add(attributeLine)

        var i = 0
        val size = routePoints.size

        while (i < size) {

            i++
        }

        mapView.invalidate()

        tvCategory.text = "${getString(R.string.category)}: ${category.name}"
        tvComment.text = "${getString(R.string.comment)}: ${longAttribute!!.comment}"
        //tvRadius.text = "${getString(R.string.radius)}: ${longAttribute!!} м."

    }
}
