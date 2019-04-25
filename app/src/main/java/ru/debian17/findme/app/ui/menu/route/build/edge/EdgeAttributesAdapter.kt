package ru.debian17.findme.app.ui.menu.route.build.edge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.debian17.findme.R
import ru.debian17.findme.app.base.BaseAdapter
import ru.debian17.findme.app.base.BaseViewHolder
import ru.debian17.findme.app.base.Header
import ru.debian17.findme.data.model.attribute.LongAttribute
import ru.debian17.findme.data.model.attribute.PointAttribute
import ru.debian17.findme.data.model.category.Category
import java.lang.RuntimeException

class EdgeAttributesAdapter(
    private val categories: List<Category>,
    private val edgeAttributesListener: EdgeAttributesListener
) : BaseAdapter<Any, BaseViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_POINT = 1
        private const val VIEW_TYPE_LONG = 2
    }

    interface EdgeAttributesListener {
        fun onPointAttributeClick(pointAttribute: PointAttribute)
        fun onLongAttributeClick(longAttribute: LongAttribute)
    }


    override fun getItemViewType(position: Int): Int {
        return when (val item = items[position]) {
            is Header -> VIEW_TYPE_HEADER
            is PointAttribute -> VIEW_TYPE_POINT
            is LongAttribute -> VIEW_TYPE_LONG
            else -> throw RuntimeException("Unknown item=$item")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_POINT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edge_attribute, parent, false)
                val holder = PointViewHolder(view)
                holder.itemView.setOnClickListener {
                    val position = holder.adapterPosition
                    val item = items[position]
                    if (item is PointAttribute) {
                        edgeAttributesListener.onPointAttributeClick(item)
                    }
                }
                holder
            }
            VIEW_TYPE_LONG -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edge_attribute, parent, false)
                val holder = LongViewHolder(view)
                holder.itemView.setOnClickListener {
                    val position = holder.adapterPosition
                    val item = items[position]
                    if (item is LongAttribute) {
                        edgeAttributesListener.onLongAttributeClick(item)
                    }
                }
                holder
            }
            else -> throw RuntimeException("Unknown viewType=$viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = items[position]

        if (item is Header && holder is HeaderViewHolder) {
            holder.tvTitle.text = item.title
        } else if (item is PointAttribute && holder is PointViewHolder) {
            val category = categories.find { item.categoryId == it.id }
            if (category != null) {
                holder.tvCategory.text = category.name
            }
        } else if (item is LongAttribute && holder is LongViewHolder) {
            val category = categories.find { item.categoryId == it.id }
            if (category != null) {
                holder.tvCategory.text = category.name
            }
        }
    }

    class HeaderViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    }

    class PointViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
    }

    class LongViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
    }

}