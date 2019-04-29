package ru.debian17.findme.app.ui.menu.attribute.add.lon


import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_add_long_attribute.*
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
import ru.debian17.findme.app.ext.hideKeyboard
import ru.debian17.findme.app.ext.longSnackBar
import ru.debian17.findme.app.ext.show
import ru.debian17.findme.app.mvp.BaseFragment
import ru.debian17.findme.data.model.category.Category
import ru.debian17.findme.data.model.route.RoutePoint


class AddLongAttributeFragment : BaseFragment(), AddLongAttributeView {

    companion object {
        const val TAG = "AddLongAttributeFragmentTag"
        fun newInstance(): AddLongAttributeFragment {
            return AddLongAttributeFragment()
        }
    }

    @InjectPresenter
    lateinit var presenter: AddLongAttributePresenter

    @ProvidePresenter
    fun providePresenter(): AddLongAttributePresenter {
        val dataSourceComponent = (activity!!.applicationContext as App).getDataSourceComponent()
        return AddLongAttributePresenter(
                dataSourceComponent.provideCategoriesRepository(),
                dataSourceComponent.provideAttributesRepository()
        )
    }

    private val points = ArrayList<GeoPoint>()
    private val markers = ArrayList<Marker>()
    private val lines = ArrayList<Polyline>()

    private lateinit var adapter: ArrayAdapter<Category>
    private lateinit var curCategory: Category
    private lateinit var blueIcon: Drawable

    private val mapEventsReceiver = object : MapEventsReceiver {
        override fun longPressHelper(p: GeoPoint?): Boolean {
            return false
        }

        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
            if (p != null) {

                points.add(p)

                val marker = Marker(mapView).apply {
                    position = p
                    icon = blueIcon
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    title = null
                    infoWindow = null
                }
                markers.add(marker)

                if (points.size > 1) {
                    val preLastIndex = points.size - 2
                    val lastIndex = points.size - 1

                    val preLastPoint = points[preLastIndex]
                    val lastPoint = points[lastIndex]

                    val line = Polyline(mapView).apply {
                        color = Color.BLUE
                        title = null
                        infoWindow = null
                        addPoint(preLastPoint)
                        addPoint(lastPoint)
                    }

                    lines.add(line)
                    mapView.overlays.add(line)
                }

                mapView.overlays.add(marker)
                mapView.invalidate()
            }
            return true
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_long_attribute, container, false)
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
            overlays.add(mapEventsOverlay)
        }

        blueIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_location_blue)!!

        spCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                curCategory = adapter.getItem(position)!!
            }
        }

        btnAddAttribute.setOnClickListener {
            if (points.size < 2) {
                view.longSnackBar(getString(R.string.points_size_error))
                return@setOnClickListener
            }

            val comment = etComment.text.toString().trim()
            if (comment.isEmpty()) {
                view.longSnackBar(getString(R.string.write_comment))
                return@setOnClickListener
            }

            view.hideKeyboard()
            presenter.addLongAttribute(curCategory.id, comment, points)
        }

    }

    override fun onCategoriesLoaded(categories: List<Category>) {
        adapter = ArrayAdapter(context!!,
                R.layout.support_simple_spinner_dropdown_item,
                categories)
        spCategories.adapter = adapter
    }

    override fun onAttributeAdded() {
        points.clear()

        markers.forEach {
            mapView.overlays.remove(it)
        }
        lines.forEach {
            mapView.overlays.remove(it)
        }

        mapView.invalidate()

        etComment.text.clear()

        view?.longSnackBar(getString(R.string.attribute_added))
    }

    override fun onError(errorMessage: String?) {
        view?.longSnackBar(errorMessage)
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
