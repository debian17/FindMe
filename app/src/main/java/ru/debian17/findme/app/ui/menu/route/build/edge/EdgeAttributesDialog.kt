package ru.debian17.findme.app.ui.menu.route.build.edge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.edge_attributes_bottom_dialog.*
import ru.debian17.findme.R
import ru.debian17.findme.app.androidx.MvpBottomSheetDialogFragment
import ru.debian17.findme.app.base.Header
import ru.debian17.findme.app.ui.menu.attribute.info.lon.LongAttributeInfoActivity
import ru.debian17.findme.app.ui.menu.attribute.info.point.PointAttributeInfoActivity
import ru.debian17.findme.data.model.attribute.LongAttribute
import ru.debian17.findme.data.model.attribute.PointAttribute
import ru.debian17.findme.data.model.category.Category
import ru.debian17.findme.data.model.route.RoutePoint


class EdgeAttributesDialog : MvpBottomSheetDialogFragment(),
    EdgeAttributesAdapter.EdgeAttributesListener {

    companion object {
        private const val POINT_ATTRIBUTES_KEY = "pointAttributesKey"
        private const val LONG_ATTRIBUTES_KEY = "longAttributesKey"
        private const val CATEGORIES_KEY = "CategoriesKey"
        private const val ROUTE_POINTS_KEY = "routePointsKey"
        const val TAG = "AttributesDialogTag"
        fun newInstance(
            pointAttributes: ArrayList<PointAttribute>,
            longAttributes: ArrayList<LongAttribute>,
            categories: ArrayList<Category>,
            routePoints: ArrayList<RoutePoint>
        ): EdgeAttributesDialog {
            return EdgeAttributesDialog().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(POINT_ATTRIBUTES_KEY, pointAttributes)
                    putParcelableArrayList(LONG_ATTRIBUTES_KEY, longAttributes)
                    putParcelableArrayList(CATEGORIES_KEY, categories)
                    putParcelableArrayList(ROUTE_POINTS_KEY, routePoints)
                }
            }
        }
    }

    private lateinit var pointAttributes: List<PointAttribute>
    private lateinit var longAttributes: List<LongAttribute>
    private lateinit var routePoints: ArrayList<RoutePoint>
    private lateinit var categories: List<Category>
    private lateinit var adapter: EdgeAttributesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.edge_attributes_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pointAttributes = arguments!!.getParcelableArrayList(POINT_ATTRIBUTES_KEY)!!
        longAttributes = arguments!!.getParcelableArrayList(LONG_ATTRIBUTES_KEY)!!
        categories = arguments!!.getParcelableArrayList(CATEGORIES_KEY)!!
        routePoints = arguments!!.getParcelableArrayList(ROUTE_POINTS_KEY)!!

        adapter = EdgeAttributesAdapter(categories, this)

        rvAttributes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@EdgeAttributesDialog.adapter
        }

        if (pointAttributes.isNotEmpty()) {
            val pointAttributesHeader = Header(getString(R.string.point_attributes))
            adapter.add(pointAttributesHeader)
            adapter.addAll(pointAttributes)
        }
        if (longAttributes.isNotEmpty()) {
            val longAttributesHeader = Header(getString(R.string.long_attributes))
            adapter.add(longAttributesHeader)
            adapter.addAll(longAttributes)
        }

    }

    override fun onPointAttributeClick(pointAttribute: PointAttribute) {
        val category = categories.find { it.id == pointAttribute.categoryId }
        if (category != null) {
            val intent = PointAttributeInfoActivity.getStartIntent(
                context!!,
                pointAttribute,
                category,
                routePoints
            )
            startActivity(intent)
        }
    }

    override fun onLongAttributeClick(longAttribute: LongAttribute) {
        val category = categories.find { it.id == longAttribute.categoryId }
        if (category != null) {
            val intent = LongAttributeInfoActivity.getStartIntent(
                context!!,
                longAttribute,
                category,
                routePoints
            )
            startActivity(intent)
        }
    }

}