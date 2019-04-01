package ru.debian17.findme.app.ui.menu.route.build

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.build_route_bottom_dialog.*
import ru.debian17.findme.R
import ru.debian17.findme.app.ext.hide
import java.lang.IllegalStateException

class BuildRouteDialog : BottomSheetDialogFragment() {

    interface BuildRouteListener {
        fun onFromCurrentLocation()
        fun onFromSelectedDot()
    }

    companion object {
        const val TAG = "BuildRouteDialog"
        private const val IS_CURRENT_LOCATION_ENABLED = "isCurrentLocationEnabled"
        fun newInstance(isCurLocationEnabled: Boolean): BuildRouteDialog {
            return BuildRouteDialog().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_CURRENT_LOCATION_ENABLED, isCurLocationEnabled)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment !is BuildRouteListener) {
            throw IllegalStateException("Parent fragment must implement BuildRouteDialog.BuildRouteListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.build_route_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val isCurLocationEnabled = arguments!!.getBoolean(IS_CURRENT_LOCATION_ENABLED, false)

        if (isCurLocationEnabled) {
            flCurLocation.setOnClickListener {
                (parentFragment as BuildRouteListener).onFromCurrentLocation()
                dismiss()
            }
        } else {
            flCurLocation.hide()
        }

        flSelectedDot.setOnClickListener {
            (parentFragment as BuildRouteListener).onFromSelectedDot()
            dismiss()
        }

    }

}