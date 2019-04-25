package ru.debian17.findme.app.ui.menu.attribute.add.point


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_add_point_attribute.*
import kotlinx.android.synthetic.main.fragment_add_point_attribute.mapView
import kotlinx.android.synthetic.main.fragment_build_route.*
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Polygon
import ru.debian17.findme.R
import ru.debian17.findme.app.ext.show
import ru.debian17.findme.app.mvp.BaseFragment


class AddPointAttributeFragment : BaseFragment(), AddPointAttributeView {

    companion object {
        const val TAG = "AddPointAttributeFragmentTag"
        fun newInstance(): AddPointAttributeFragment {
            return AddPointAttributeFragment()
        }
    }

    @InjectPresenter
    lateinit var presenter: AddPointAttributePresenter

    @ProvidePresenter
    fun providePresenter(): AddPointAttributePresenter {
        return AddPointAttributePresenter()
    }

    private lateinit var pointPolygon: Polygon

    private var radius = 10.0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_point_attribute, container, false)
    }

    private val mapEventsReceiver = object : MapEventsReceiver {
        override fun longPressHelper(p: GeoPoint?): Boolean {
            return false
        }

        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
            if (p != null) {

                llAdd.show()

                mapView.overlays.remove(pointPolygon)
                pointPolygon.points = Polygon.pointsAsCircle(p, radius)

                mapView.overlays.add(pointPolygon)
                mapView.invalidate()
            }
            return true
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setMultiTouchControls(true)

            controller.setCenter(defaultPoint)
            controller.setZoom(defaultZoom)

            val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
            mapView.overlays.add(mapEventsOverlay)
        }

        pointPolygon = Polygon(mapView)
        pointPolygon.apply {
            fillColor = ContextCompat.getColor(context!!, R.color.alpha_gray)
            strokeWidth = 2f
            strokeColor = Color.RED
            infoWindow = null
            title = null
            setOnClickListener { _, _, _ -> false }
        }

    }

    override fun showLoading() {

    }

    override fun showMain() {

    }

    override fun showError(errorMessage: String?) {

    }

}
