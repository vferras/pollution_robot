package com.vferras.robotMeasurement.domain

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.atan2
import kotlin.math.asin
import kotlin.math.sqrt
import kotlin.math.roundToInt

const val EARTH_RADIUS = 6372.8
const val MULTIPLIER = 1000

fun Double.toMs(): Double { return this * MULTIPLIER }

fun Double.toKms(): Double { return this / MULTIPLIER }

fun calculateBearing(startPoint: GeoPosition, endPoint: GeoPosition): Double {
    val lat1 = degToRad(startPoint.lat)
    val lat2 = degToRad(endPoint.lat)
    val deltaLon = degToRad(endPoint.lon - startPoint.lon)
    val y = sin(deltaLon) * cos(lat2)
    val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(deltaLon)
    val bearing = atan2(y, x)

    return (radToDeg(bearing) + 360) % 360
}

fun calculateDestinationLocation(point: GeoPosition, bearing: Double, distance: Double): GeoPosition {
    val bearingRad = degToRad(bearing)
    val kms = distance.toKms() / EARTH_RADIUS
    val lat1 = degToRad(point.lat)
    val lon1 = degToRad(point.lon)
    val lat2 = asin(sin(lat1) * cos(kms) + cos(lat1) * sin(kms) * cos(bearingRad))
    val lon2 = lon1 + atan2(sin(bearingRad) * sin(kms) * cos(lat1), cos(kms) - sin(lat1) * sin(lat2))
    val lon2Normalized = (lon2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI // normalize to -180 - + 180 degrees

    return GeoPosition(radToDeg(lat2), radToDeg(lon2Normalized))
}

fun haversineDistance(initialPosition: GeoPosition, finalPosition: GeoPosition): Double {
    val dLat = Math.toRadians(finalPosition.lat - initialPosition.lat)
    val dLon = Math.toRadians(finalPosition.lon - initialPosition.lon)
    val originLat = Math.toRadians(initialPosition.lat)
    val destinationLat = Math.toRadians(finalPosition.lat)

    val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(originLat) * cos(destinationLat)

    val c = 2 * asin(sqrt(a))

    return BigDecimal(EARTH_RADIUS * c).setScale(4, RoundingMode.DOWN).toDouble().toMs()
}

fun locationInterpolation(initialPosition: GeoPosition, finalPosition: GeoPosition): List<GeoPosition> {
    val distanceBetweenPoints = 1.0
    var startLocation = initialPosition.copy()

    val positions = mutableListOf<GeoPosition>()

    for (i in 0 until haversineDistance(initialPosition, finalPosition).roundToInt()) {
        val bearing: Double = calculateBearing(startLocation, finalPosition)
        val intermediaryLocation = calculateDestinationLocation(startLocation, bearing, distanceBetweenPoints)

        positions.add(intermediaryLocation)
        startLocation = intermediaryLocation
    }

    return positions
}

val degToRad = { deg: Double -> deg * Math.PI / 180 }
val radToDeg = { rad: Double -> rad * 180 / Math.PI }
