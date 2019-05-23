package ru.debian17.findme.app.ui.menu.attribute.list


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_attributes.*

import ru.debian17.findme.R
import ru.debian17.findme.app.App
import ru.debian17.findme.app.base.Header
import ru.debian17.findme.app.ext.hide
import ru.debian17.findme.app.ext.longSnackBar
import ru.debian17.findme.app.ext.show
import ru.debian17.findme.app.mvp.BaseFragment
import ru.debian17.findme.app.ui.menu.attribute.edit.point.EditPointAttributeActivity
import ru.debian17.findme.app.ui.menu.attribute.info.lon.LongAttributeInfoActivity
import ru.debian17.findme.app.ui.menu.attribute.info.point.PointAttributeInfoActivity
import ru.debian17.findme.data.model.attribute.AttributeContainer
import ru.debian17.findme.data.model.attribute.LongAttributeInfo
import ru.debian17.findme.data.model.attribute.PointAttribute
import ru.debian17.findme.data.model.category.Category

class AttributesFragment : BaseFragment(), AttributesView, AttributesAdapter.AttributesListener {

    companion object {
        private const val EDIT_ATTR_CODE = 123
        const val TAG = "AttributesFragmentTag"
        fun newInstance(): AttributesFragment {
            return AttributesFragment()
        }
    }

    private lateinit var adapter: AttributesAdapter
    private lateinit var categories: List<Category>

    @InjectPresenter
    lateinit var presenter: AttributesPresenter

    @ProvidePresenter
    fun providePresenter(): AttributesPresenter {
        val dataSourceComponent = (activity!!.applicationContext as App).getDataSourceComponent()
        return AttributesPresenter(
                dataSourceComponent.provideCategoriesRepository(),
                dataSourceComponent.provideAttributesRepository()
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_attributes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAttributes.apply {
            layoutManager = LinearLayoutManager(context!!)
        }

    }

    override fun onDataLoaded(categories: List<Category>, attributeContainer: AttributeContainer) {
        this.categories = categories
        adapter = AttributesAdapter(context!!, categories, this)
        rvAttributes.adapter = adapter

        if (attributeContainer.pointAttributes.isEmpty() && attributeContainer.longAttributes.isEmpty()) {
            tvEmpty.show()
        } else {

            if (attributeContainer.pointAttributes.isNotEmpty()) {
                val pointHeader = Header(getString(R.string.point_attributes))
                adapter.add(pointHeader)
                adapter.addAll(attributeContainer.pointAttributes)
            }

            if (attributeContainer.longAttributes.isNotEmpty()) {
                val longHeader = Header(getString(R.string.long_attributes))
                adapter.add(longHeader)
                adapter.addAll(attributeContainer.longAttributes)
            }

        }

    }

    override fun onPointAttributeClick(pointAttribute: PointAttribute) {
        val category = categories.find { it.id == pointAttribute.categoryId }
        if (category != null) {
            val intent = PointAttributeInfoActivity.getStartIntent(context!!, pointAttribute, category, null)
            startActivity(intent)
        }
    }

    override fun onPointAttributeEdit(pointAttribute: PointAttribute) {
        startActivityForResult(
                EditPointAttributeActivity.getStartIntent(context!!, pointAttribute),
                EDIT_ATTR_CODE
        )
    }

    override fun onPointAttributeDelete(pointAttribute: PointAttribute) {
        presenter.deleteAttribute(pointAttribute.id)
    }

    override fun onLongAttributeClick(longAttribute: LongAttributeInfo) {
        val category = categories.find { it.id == longAttribute.categoryId }
        if (category != null) {
            val intent = LongAttributeInfoActivity.getStartIntent(context!!, longAttribute, category, null)
            startActivity(intent)
        }
    }

    override fun onLongAttributeEdit(longAttribute: LongAttributeInfo) {

    }

    override fun onLongAttributeDelete(longAttribute: LongAttributeInfo) {
        presenter.deleteAttribute(longAttribute.id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EDIT_ATTR_CODE && resultCode == Activity.RESULT_OK) {
            presenter.getAttributes()
        }
    }

    override fun onError(errorMessage: String?) {
        view?.longSnackBar(errorMessage)
    }

    override fun showLoading() {
        pbLoading.show()
        rvAttributes.hide()
    }

    override fun showMain() {
        rvAttributes.show()
        pbLoading.hide()
    }

    override fun showError(errorMessage: String?) {

    }

}
