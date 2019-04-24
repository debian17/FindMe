package ru.debian17.findme.data.repository

import io.reactivex.Single
import ru.debian17.findme.app.dal.AttributesDataSource
import ru.debian17.findme.data.model.edge.EdgeInfo
import ru.debian17.findme.data.network.WebAPIService

class AttributesRepository(private val webAPIService: WebAPIService) : AttributesDataSource {

    override fun getAttributesOfEdge(edgeId: Int): Single<EdgeInfo> {
        return webAPIService.getAttributesOfEdge(edgeId)
    }

}