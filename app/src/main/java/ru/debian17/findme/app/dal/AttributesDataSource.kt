package ru.debian17.findme.app.dal

import io.reactivex.Single
import ru.debian17.findme.data.model.edge.EdgeInfo

interface AttributesDataSource {

    fun getAttributesOfEdge(edgeId: Int): Single<EdgeInfo>

}