package ru.debian17.findme.app.ui.menu.route.build

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.edge_attributes_bottom_dialog.*
import ru.debian17.findme.R
import ru.debian17.findme.app.base.Header
import ru.debian17.findme.app.ext.getCategories
import ru.debian17.findme.app.ui.menu.attribute.info.AttributeInfoActivity
import ru.debian17.findme.data.model.attribute.LongAttribute
import ru.debian17.findme.data.model.attribute.PointAttribute
import ru.debian17.findme.data.model.route.RoutePoint


class EdgeAttributesDialog : BottomSheetDialogFragment(), EdgeAttributesAdapter.EdgeAttributesListener {

    companion object {
        private const val POINT_ATTRIBUTES_KEY = "pointAttributesKey"
        private const val LONG_ATTRIBUTES_KEY = "longAttributesKey"
        private const val ROUTE_POINTS_KEY = "routePointsKey"
        const val TAG = "AttributesDialogTag"
        fun newInstance(
            pointAttributes: ArrayList<PointAttribute>,
            longAttributes: ArrayList<LongAttribute>,
            routePoints: ArrayList<RoutePoint>
        ): EdgeAttributesDialog {
            return EdgeAttributesDialog().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(POINT_ATTRIBUTES_KEY, pointAttributes)
                    putParcelableArrayList(LONG_ATTRIBUTES_KEY, longAttributes)
                    putParcelableArrayList(ROUTE_POINTS_KEY, routePoints)
                }
            }
        }
    }

    private lateinit var adapter: EdgeAttributesAdapter
    private val routePoints = ArrayList<RoutePoint>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.edge_attributes_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pointAttributes = arguments!!.getParcelableArrayList<PointAttribute>(POINT_ATTRIBUTES_KEY)!!
        val longAttributes = arguments!!.getParcelableArrayList<LongAttribute>(LONG_ATTRIBUTES_KEY)!!
        routePoints.addAll(arguments!!.getParcelableArrayList<RoutePoint>(ROUTE_POINTS_KEY)!!)

        adapter = EdgeAttributesAdapter(context!!.getCategories(), this)

        val pointAttributesHeader = Header(getString(R.string.point_attributes))
        val longAttributesHeader = Header(getString(R.string.long_attributes))

        rvAttributes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@EdgeAttributesDialog.adapter
        }

        if (pointAttributes.isNotEmpty()) {
            adapter.add(pointAttributesHeader)
            adapter.addAll(pointAttributes)
        }

        if (longAttributes.isNotEmpty()) {
            adapter.add(longAttributesHeader)
            adapter.addAll(longAttributes)
        }

    }

    override fun onPointAttributeClick(pointAttribute: PointAttribute) {
        val intent = AttributeInfoActivity.getStartIntent(context!!, pointAttribute, routePoints)
        startActivity(intent)
    }

    override fun onLongAttributeClick(longAttribute: LongAttribute) {
        val intent = AttributeInfoActivity.getStartIntent(context!!, longAttribute, routePoints)
        startActivity(intent)
    }

}