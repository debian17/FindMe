package ru.debian17.findme.app.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : BaseViewHolder> : RecyclerView.Adapter<VH>() {

    protected val items = ArrayList<T>()

    override fun getItemCount(): Int {
        return items.size
    }

    fun add(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun addAll(items: List<T>) {
        val position = this.items.size
        this.items.addAll(items)
        notifyItemRangeInserted(position, items.size)
    }

    fun replaceAll(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

}