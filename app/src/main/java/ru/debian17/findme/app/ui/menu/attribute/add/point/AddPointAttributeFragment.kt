package ru.debian17.findme.app.ui.menu.attribute.add.point


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_add_point_attribute.*
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Polygon
import ru.debian17.findme.R
import ru.debian17.findme.app.App
import ru.debian17.findme.app.ext.hide
import ru.debian17.findme.app.ext.longSnackBar
import ru.debian17.findme.app.ext.show
import ru.debian17.findme.app.mvp.BaseFragment
import ru.debian17.findme.data.model.category.Category


class AddPointAttributeFragment : BaseFragment(), AddPointAttributeView {

    companion object {
        private const val MIN_RADIUS = 10
        private const val DEFAULT_COORDINATE = -1.0
        const val TAG = "AddPointAttributeFragmentTag"
        fun newInstance(): AddPointAttributeFragment {
            return AddPointAttributeFragment()
        }
    }

    @InjectPresenter
    lateinit var presenter: AddPointAttributePresenter

    @ProvidePresenter
    fun providePresenter(): AddPointAttributePresenter {
        val dataSourceComponent = (activity!!.applicationContext as App).getDataSourceComponent()
        val categoriesRepository = dataSourceComponent.provideCategoriesRepository()
        val attributesRepository = dataSourceComponent.provideAttributesRepository()
        return AddPointAttributePresenter(categoriesRepository, attributesRepository)
    }

    private lateinit var pointPolygon: Polygon
    private lateinit var curCategory: Category
    private lateinit var adapter: ArrayAdapter<Category>
    private lateinit var radiusStr: String
    private val selectedGeoPoint = GeoPoint(DEFAULT_COORDINATE, DEFAULT_COORDINATE)

    private var curRadius = MIN_RADIUS

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

                selectedGeoPoint.apply {
                    latitude = p.latitude
                    longitude = p.longitude
                }

                mapView.overlays.remove(pointPolygon)
                pointPolygon.points = Polygon.pointsAsCircle(selectedGeoPoint, curRadius.toDouble())

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
        }

        val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
        mapView.overlays.add(mapEventsOverlay)

        pointPolygon = Polygon(mapView).apply {
            fillColor = ContextCompat.getColor(context!!, R.color.alpha_gray)
            strokeWidth = 2f
            strokeColor = Color.RED
            infoWindow = null
            title = null
            setOnClickListener { _, _, _ -> false }
        }

        spCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                curCategory = adapter.getItem(position)!!
            }
        }

        radiusStr = getString(R.string.radius)
        tvRadius.text = "$radiusStr $curRadius м."

        sbRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                curRadius = progress + MIN_RADIUS
                tvRadius.text = "$radiusStr $curRadius м."

                if (selectedGeoPoint.latitude != DEFAULT_COORDINATE && selectedGeoPoint.longitude != DEFAULT_COORDINATE) {
                    mapView.overlays.remove(pointPolygon)
                    pointPolygon.points = Polygon.pointsAsCircle(selectedGeoPoint, curRadius.toDouble())

                    mapView.overlays.add(pointPolygon)
                    mapView.invalidate()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        btnAddAttribute.setOnClickListener {

            if (selectedGeoPoint.latitude == DEFAULT_COORDINATE || selectedGeoPoint.longitude == DEFAULT_COORDINATE) {
                view.longSnackBar(getString(R.string.selected_dot_on_map))
                return@setOnClickListener
            }

            val comment = etComment.text.toString().trim()
            if (comment.isEmpty()) {
                view.longSnackBar(getString(R.string.write_comment))
                return@setOnClickListener
            }

            presenter.addAttribute(curCategory.id, curRadius.toDouble(), comment, selectedGeoPoint.latitude, selectedGeoPoint.longitude)

        }

    }

    override fun onCategoriesLoaded(categories: List<Category>) {
        adapter = ArrayAdapter(context!!,
                R.layout.support_simple_spinner_dropdown_item,
                categories)
        spCategories.adapter = adapter

    }

    override fun onAttributeAdded() {
        mapView.overlays.remove(pointPolygon)
        mapView.invalidate()

        selectedGeoPoint.apply {
            latitude = DEFAULT_COORDINATE
            longitude = DEFAULT_COORDINATE
        }

        sbRadius.progress = 0

        etComment.text.clear()

        view?.longSnackBar(getString(R.string.attribute_added))
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
