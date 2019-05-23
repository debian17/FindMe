package ru.debian17.findme.app.ui.menu.attribute.edit.lon

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_edit_long_barrier.*
import kotlinx.android.synthetic.main.activity_edit_long_barrier.llMain
import kotlinx.android.synthetic.main.activity_edit_long_barrier.mapView
import kotlinx.android.synthetic.main.activity_edit_long_barrier.pbLoading
import kotlinx.android.synthetic.main.activity_edit_long_barrier.spCategories
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
import ru.debian17.findme.app.mvp.BaseActivity
import ru.debian17.findme.data.model.category.Category

class EditLongBarrierActivity : BaseActivity(), EditLongBarrierView {

    companion object {
        private const val BARRIER_ID_KEY = "barrierIdKey"
        private const val LAT_KEY = "latKey"
        private const val LON_KEY = "lonKey"
        private const val CATEGORY_ID = "categoryIdKey"
        private const val COMMENT_KEY = "commentKey"

        fun getStartIntent(
                context: Context,
                barrierId: Int,
                latitude: Double,
                longitude: Double,
                categoryId: Int,
                comment: String
        ): Intent {
            return Intent(context, EditLongBarrierActivity::class.java).apply {
                putExtra(BARRIER_ID_KEY, barrierId)
                putExtra(LAT_KEY, latitude)
                putExtra(LON_KEY, longitude)
                putExtra(CATEGORY_ID, categoryId)
                putExtra(COMMENT_KEY, comment)
            }
        }

    }

    private var barrierId = -1
    private var categoryId = -1
    private var comment = ""

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

    @InjectPresenter
    lateinit var presenter: EditLongBarrierPresenter

    @ProvidePresenter
    fun providePresenter(): EditLongBarrierPresenter {
        val dataSourceComponent = (application as App).getDataSourceComponent()
        return EditLongBarrierPresenter(dataSourceComponent.provideCategoriesRepository(),
                dataSourceComponent.provideAttributesRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_long_barrier)

        barrierId = intent!!.getIntExtra(BARRIER_ID_KEY, -1)
        categoryId = intent!!.getIntExtra(CATEGORY_ID, -1)
        comment = intent!!.getStringExtra(COMMENT_KEY)
        blueIcon = ContextCompat.getDrawable(this, R.drawable.ic_location_blue)!!

        val latitude = intent!!.getDoubleExtra(LAT_KEY, 0.0)
        val longitude = intent!!.getDoubleExtra(LON_KEY, 0.0)

        mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setMultiTouchControls(true)

            controller.setCenter(GeoPoint(latitude, longitude))
            controller.setZoom(defaultZoom)

            val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
            overlays.add(mapEventsOverlay)
        }

        spCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                curCategory = adapter.getItem(position)!!
            }
        }

        etComment.setText(comment)

        btnEditBarrier.setOnClickListener {
            if (points.size < 2) {
                llMain.longSnackBar(getString(R.string.points_size_error))
                return@setOnClickListener
            }

            val comment = etComment.text.toString().trim()
            if (comment.isEmpty()) {
                llMain.longSnackBar(getString(R.string.write_comment))
                return@setOnClickListener
            }

            llMain.hideKeyboard()
            presenter.editLongBarrier(barrierId, curCategory.id, comment, points)
        }

        llMain.hideKeyboard()
    }

    override fun onCategoriesLoaded(categories: List<Category>) {
        adapter = ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,
                categories)
        spCategories.adapter = adapter

        val category = categories.find { it.id == categoryId }
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
