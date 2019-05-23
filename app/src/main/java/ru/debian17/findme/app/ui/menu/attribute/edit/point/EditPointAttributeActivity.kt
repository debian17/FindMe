package ru.debian17.findme.app.ui.menu.attribute.edit.point

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_edit_point_attribute.*
import kotlinx.android.synthetic.main.activity_edit_point_attribute.llMain
import kotlinx.android.synthetic.main.activity_edit_point_attribute.mapView
import kotlinx.android.synthetic.main.activity_edit_point_attribute.pbLoading
import kotlinx.android.synthetic.main.activity_edit_point_attribute.spCategories
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Polygon
import ru.debian17.findme.R
import ru.debian17.findme.app.App
import ru.debian17.findme.app.ext.hide
import ru.debian17.findme.app.ext.hideKeyboard
import ru.debian17.findme.app.ext.longSnackBar
import ru.debian17.findme.app.ext.show
import ru.debian17.findme.app.mvp.BaseActivity
import ru.debian17.findme.data.model.attribute.PointAttribute
import ru.debian17.findme.data.model.category.Category

class EditPointAttributeActivity : BaseActivity(), EditPointAttributeView {

    companion object {
        private const val MIN_RADIUS = 10
        private const val DEFAULT_COORDINATE = -1.0
        private const val POINT_ATTR_KEY = "pointAtrKey"
        fun getStartIntent(context: Context, pointAttribute: PointAttribute): Intent {
            return Intent(context, EditPointAttributeActivity::class.java).apply {
                putExtra(POINT_ATTR_KEY, pointAttribute)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: EditPointAttributePresenter

    @ProvidePresenter
    fun providePresenter(): EditPointAttributePresenter {
        val dataSource = (application as App).getDataSourceComponent()
        return EditPointAttributePresenter(
                dataSource.provideCategoriesRepository(),
                dataSource.provideAttributesRepository()
        )
    }

    private lateinit var pointPolygon: Polygon
    private lateinit var curCategory: Category
    private lateinit var adapter: ArrayAdapter<Category>
    private lateinit var pointAttribute: PointAttribute
    private lateinit var radiusStr: String
    private val selectedGeoPoint = GeoPoint(DEFAULT_COORDINATE, DEFAULT_COORDINATE)

    private var curRadius = MIN_RADIUS

    private val mapEventsReceiver = object : MapEventsReceiver {
        override fun longPressHelper(p: GeoPoint?): Boolean {
            return false
        }

        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
            if (p != null) {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_point_attribute)

        pointAttribute = intent.getParcelableExtra(POINT_ATTR_KEY)
        curRadius = pointAttribute.radius

        selectedGeoPoint.latitude = pointAttribute.latitude
        selectedGeoPoint.longitude = pointAttribute.longitude

        mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setMultiTouchControls(true)

            controller.setCenter(GeoPoint(pointAttribute.latitude, pointAttribute.longitude))
            controller.setZoom(defaultZoom)

            val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
            overlays.add(mapEventsOverlay)
        }

        pointPolygon = Polygon(mapView).apply {
            fillColor = ContextCompat.getColor(this@EditPointAttributeActivity, R.color.alpha_gray)
            strokeWidth = 2f
            strokeColor = Color.RED
            infoWindow = null
            title = null
            setOnClickListener { _, _, _ -> false }
        }
        pointPolygon.points = Polygon.pointsAsCircle(selectedGeoPoint, curRadius.toDouble())

        mapView.overlays.add(pointPolygon)
        mapView.invalidate()

        spCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                curCategory = adapter.getItem(position)!!
            }
        }

        radiusStr = getString(R.string.radius)
        tvRadius.text = "$radiusStr $curRadius м."
        sbRadius.progress = curRadius - MIN_RADIUS
        sbRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    curRadius = progress + MIN_RADIUS
                    tvRadius.text = "$radiusStr $curRadius м."

                    if (selectedGeoPoint.latitude != DEFAULT_COORDINATE && selectedGeoPoint.longitude != DEFAULT_COORDINATE) {
                        mapView.overlays.remove(pointPolygon)
                        pointPolygon.points = Polygon.pointsAsCircle(selectedGeoPoint, curRadius.toDouble())

                        mapView.overlays.add(pointPolygon)
                        mapView.invalidate()
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        etComment.setText(pointAttribute.comment)

        btnEditBarrier.setOnClickListener {

            val comment = etComment.text.toString().trim()
            if (comment.isEmpty()) {
                llMain.longSnackBar(getString(R.string.write_comment))
                return@setOnClickListener
            }

            llMain.hideKeyboard()
            presenter.editLocalBarrier(
                    pointAttribute.id,
                    curCategory.id,
                    curRadius.toDouble(),
                    comment,
                    selectedGeoPoint.latitude,
                    selectedGeoPoint.longitude
            )
        }

        llMain.hideKeyboard()
    }

    override fun onCategoriesLoaded(categories: List<Category>) {
        adapter = ArrayAdapter(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                categories
        )
        spCategories.adapter = adapter

        val category = categories.find { it.id == pointAttribute.categoryId }
        if (category != null) {
            val index = categories.indexOf(category)
            spCategories.setSelection(index)
        }
    }

    override fun onEditSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
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
