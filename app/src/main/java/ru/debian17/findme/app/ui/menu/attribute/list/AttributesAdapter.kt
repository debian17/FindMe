package ru.debian17.findme.app.ui.menu.attribute.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import ru.debian17.findme.R
import ru.debian17.findme.app.base.BaseAdapter
import ru.debian17.findme.app.base.BaseViewHolder
import ru.debian17.findme.app.base.Header
import ru.debian17.findme.app.ext.hide
import ru.debian17.findme.data.model.attribute.LongAttribute
import ru.debian17.findme.data.model.attribute.PointAttribute
import ru.debian17.findme.data.model.category.Category
import java.lang.RuntimeException

class AttributesAdapter(context: Context,
                        private val categories: List<Category>,
                        private val attributesListener: AttributesListener) : BaseAdapter<Any, BaseViewHolder>() {

    interface AttributesListener {
        fun onPointAttributeClick(pointAttribute: PointAttribute)
        fun onPointAttributeEdit(pointAttribute: PointAttribute)
        fun onPointAttributeDelete(pointAttribute: PointAttribute)

        fun onLongAttributeClick(longAttribute: LongAttribute)
        fun onLongAttributeEdit(longAttribute: LongAttribute)
        fun onLongAttributeDelete(longAttribute: LongAttribute)
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_POINT = 1
        private const val VIEW_TYPE_LONG = 2
    }

    private val categoryTitle = context.getString(R.string.category)
    private val radiusTitle = context.getString(R.string.radius)
    private val longTitle = context.getString(R.string.long_attr)
    private val commentTitle = context.getString(R.string.comment)

    override fun getItemViewType(position: Int): Int {
        return when (val item = items[position]) {
            is Header -> VIEW_TYPE_HEADER
            is PointAttribute -> VIEW_TYPE_POINT
            is LongAttribute -> VIEW_TYPE_LONG
            else -> throw RuntimeException("Unknown item=$item")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = layoutInflater.inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_POINT -> {
                val view = layoutInflater.inflate(R.layout.item_point_attribute, parent, false)
                val holder = PointAttributeViewHolder(view)
                holder.itemView.setOnClickListener {
                    val item = items[holder.adapterPosition]
                    if (item is PointAttribute) {
                        attributesListener.onPointAttributeClick(item)
                    }
                }
                holder.ivMenu.setOnClickListener {
                    val item = items[holder.adapterPosition]
                    if (item is PointAttribute) {
                        val menu = PopupMenu(it.context, it)
                        menu.inflate(R.menu.menu_attribute)
                        menu.setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.edit -> {
                                    attributesListener.onPointAttributeEdit(item)
                                    return@setOnMenuItemClickListener true
                                }
                                R.id.delete -> {
                                    attributesListener.onPointAttributeDelete(item)
                                    return@setOnMenuItemClickListener true
                                }
                                else -> return@setOnMenuItemClickListener false
                            }
                        }
                        menu.show()
                    }
                }
                return holder
            }
            VIEW_TYPE_LONG -> {
                val view = layoutInflater.inflate(R.layout.item_long_attribute, parent, false)
                LongAttributeViewHolder(view)
            }
            else -> throw RuntimeException("Unknown viewType=$viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (val item = items[position]) {
            is Header -> {
                val h = holder as HeaderViewHolder
                h.tvHeader.text = item.title
            }
            is PointAttribute -> {
                val h = holder as PointAttributeViewHolder
                val category = categories.find { it.id == item.categoryId }
                if (category != null) {
                    h.tvCategory.text = "$categoryTitle: ${category.name}"
                } else {
                    h.tvCategory.hide()
                }
                h.tvRadius.text = "$radiusTitle: ${item.radius} м."
                h.tvComment.text = "$commentTitle: ${item.comment}"
            }
            is LongAttribute -> {

            }
        }

    }

    class HeaderViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val tvHeader: TextView = itemView.findViewById(R.id.tvTitle)
    }

    class PointAttributeViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvRadius: TextView = itemView.findViewById(R.id.tvRadius)
        val tvComment: TextView = itemView.findViewById(R.id.tvComment)
        val ivMenu: ImageView = itemView.findViewById(R.id.ivMenu)
    }

    class LongAttributeViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvLong: TextView = itemView.findViewById(R.id.tvLong)
        val tvComment: TextView = itemView.findViewById(R.id.tvComment)
    }

}