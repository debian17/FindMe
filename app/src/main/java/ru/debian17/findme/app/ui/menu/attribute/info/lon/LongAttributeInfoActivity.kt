package ru.debian17.findme.app.ui.menu.attribute.info.lon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import ru.debian17.findme.R
import ru.debian17.findme.app.mvp.BaseActivity
import ru.debian17.findme.data.model.attribute.LongAttribute
import ru.debian17.findme.data.model.route.RoutePoint

class LongAttributeInfoActivity : BaseActivity() {

    companion object {
        private const val LONG_ATTRIBUTE_KEY = "pointAttributeKey"
        private const val ROUTE_POINTS_KEY = "routePointsKey"
        fun getStartIntent(
            context: Context,
            longAttribute: LongAttribute,
            routePoints: ArrayList<RoutePoint>
        ): Intent {
            return Intent(context, LongAttributeInfoActivity::class.java).apply {
                putExtra(LONG_ATTRIBUTE_KEY, longAttribute)
                putExtra(ROUTE_POINTS_KEY, routePoints)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_long_attribute_info)
    }
}
