package ru.debian17.findme.app.ui.menu.attribute.add

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.choose_attribute_type_dialog.*
import ru.debian17.findme.R
import java.lang.RuntimeException

class ChooseAttributeTypeDialog : BottomSheetDialogFragment() {

    interface ChooseAttributeTypeListener {
        fun onPointAttribute()
        fun onLongAttribute()
    }

    companion object {
        const val TAG = "ChooseAttributeTypeDialogTag"
        fun newInstance(): ChooseAttributeTypeDialog {
            return ChooseAttributeTypeDialog()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment !is ChooseAttributeTypeListener) {
            throw RuntimeException("Parent fragment must implement ChooseAttributeTypeListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.choose_attribute_type_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        flPointAttribute.setOnClickListener {
            (parentFragment as ChooseAttributeTypeListener).onPointAttribute()
            dismiss()
        }

        flLongAttribute.setOnClickListener {
            (parentFragment as ChooseAttributeTypeListener).onLongAttribute()
            dismiss()
        }

    }

}