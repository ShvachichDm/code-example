package io.polydev.public_transport.utils

import android.location.Location
import android.util.Log
import com.here.android.mpa.common.GeoCoordinate
import java.lang.Math.abs
import kotlin.math.sign

object MapManager {



    fun getClosestPointOnSegment(start: GeoCoordinate, end: GeoCoordinate, point: GeoCoordinate, log: Boolean): GeoCoordinate{
        val xDelta = end.latitude - start.latitude
        val yDelta = end.longitude - start.longitude
        if(xDelta == 0.0 && yDelta == 0.0) return point
        val u =
            ((point.latitude - start.latitude) * xDelta + (point.longitude - start.longitude) * yDelta) / (xDelta * xDelta + yDelta * yDelta)
        val closestPoint: GeoCoordinate
        closestPoint = when {
            u < 0 -> {
//                if(log) Log.d(AppConstants.LOG_TAG_1,"u = $u u < 0")
                start
            }
            u > 1 -> {
//                if(log)  Log.d(AppConstants.LOG_TAG_1,"u = $u u > 0")
                end
            }
            else -> {
                val closestPoint = GeoCoordinate(
                    start.latitude + u * xDelta,
                    start.longitude + u * yDelta
                )
//                if(log) Log.d(AppConstants.LOG_TAG_1,"u > 0 u < 1")
                return closestPoint
            }
        }
        return closestPoint
    }

    private fun getClosestVertexOnLine(
        current: GeoCoordinate,
        locations: Collection<GeoCoordinate>
    ): GeoCoordinate {
        var minDistance = Double.MAX_VALUE
        var nearestVertex = current
        locations.forEach {
            val distance =   current.distanceTo(it)
            if (distance < minDistance) {
                minDistance = distance
                nearestVertex = it
            }
        }

        return nearestVertex
    }

    fun getSegmentBetweenTwoPoints(start: GeoCoordinate,end: GeoCoordinate,line: ArrayList<GeoCoordinate>): ArrayList<GeoCoordinate> {
        val closestStart = getClosestVertexOnLine(start,line)
        val closestEnd = getClosestVertexOnLine(end,line)
        val segment = ArrayList<GeoCoordinate>()
        var firstPointFound = false
        for(index in line.indices) {
            val point = line[index]
            if(firstPointFound) {
                segment.add(point)
                if(point.latitude == closestEnd.latitude && point.longitude == closestEnd.longitude) {
                    if(index + 1 < line.size) {
                        segment.add(line[index + 1])
                    }
                    break
                }
            } else {
                if(point.latitude == closestStart.latitude && point.longitude == closestStart.longitude) {
                    if(index -1 >= 0) {
                        segment.add(line[index-1])
                    }
                    segment.add(point)
                    firstPointFound = true
                }
            }
        }
        return segment
    }

    fun interpolate(
        fraction: Float,
        start: GeoCoordinate,
        end: GeoCoordinate
    ): GeoCoordinate {
        val lat = (end.latitude - start.latitude) * fraction + start.latitude
        var lngDelta = end.longitude - start.longitude
        if (abs(lngDelta) > 180) {
            lngDelta -= sign(lngDelta) * 360
        }
        val lng = lngDelta * fraction + start.longitude
        return GeoCoordinate(lat, lng)
    }

}

