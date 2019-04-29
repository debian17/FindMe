package ru.debian17.findme.app.dal

import io.reactivex.Completable
import io.reactivex.Single
import ru.debian17.findme.data.model.attribute.AttributeContainer
import ru.debian17.findme.data.model.edge.EdgeInfo

interface AttributesDataSource {

    fun getAttributesOfEdge(edgeId: Int): Single<EdgeInfo>

    fun addPointAttribute(categoryId: Int,
                          radius: Double,
                          comment: String,
                          latitude: Double,
                          longitude: Double): Completable

    fun getAttributes(): Single<AttributeContainer>

}