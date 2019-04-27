package ru.debian17.findme.app.mvp

import org.osmdroid.util.GeoPoint
import ru.debian17.findme.app.androidx.MvpAndroidxActivity

abstract class BaseActivity : MvpAndroidxActivity() {

    protected val defaultPoint = GeoPoint(47.23660, 39.71257)
    protected val defaultZoom = 17.0

}