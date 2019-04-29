package ru.debian17.findme.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import org.osmdroid.util.GeoPoint
import ru.debian17.findme.app.dal.AttributesDataSource
import ru.debian17.findme.data.model.LocationParams
import ru.debian17.findme.data.model.attribute.AttributeContainer
import ru.debian17.findme.data.model.attribute.LongAttributeParams
import ru.debian17.findme.data.model.attribute.PointAttributeParams
import ru.debian17.findme.data.model.edge.EdgeInfo
import ru.debian17.findme.data.model.route.RoutePoint
import ru.debian17.findme.data.network.WebAPIService

class AttributesRepository(private val webAPIService: WebAPIService) : AttributesDataSource {

    override fun getAttributesOfEdge(edgeId: Int): Single<EdgeInfo> {
        return webAPIService.getAttributesOfEdge(edgeId)
    }

    override fun addPointAttribute(categoryId: Int, radius: Double, comment: String, latitude: Double, longitude: Double): Completable {
        val location = LocationParams(latitude, longitude)
        val pointAttributeParams = PointAttributeParams(categoryId, radius, comment, location)
        return webAPIService.addPointAttribute(pointAttributeParams)
    }

    override fun getAttributes(): Single<AttributeContainer> {
        return webAPIService.getAttributes()
    }

    override fun deleteAttribute(id: Int): Completable {
        return webAPIService.deleteAttribute(id)
    }

    override fun addLongAttribute(categoryId: Int, comment: String, points: List<GeoPoint>): Completable {
        val longAttributeParams = LongAttributeParams(categoryId, comment, points.map {
            LocationParams(it.latitude, it.longitude)
        })
        return webAPIService.addLongAttribute(longAttributeParams)
    }

}