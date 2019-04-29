package ru.debian17.findme.app.ui.menu.attribute.list

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.debian17.findme.app.mvp.BaseView
import ru.debian17.findme.data.model.attribute.AttributeContainer
import ru.debian17.findme.data.model.category.Category

interface AttributesView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onDataLoaded(categories: List<Category>, attributeContainer: AttributeContainer)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError(errorMessage: String?)

}