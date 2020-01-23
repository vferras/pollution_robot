package com.vferras.startRoute

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

data class GeoPosition(val lat: Double, val lon: Double) {

    fun haversine(finalPosition: GeoPosition): Double {
        val dLat = Math.toRadians(finalPosition.lat - this.lat)
        val dLon = Math.toRadians(finalPosition.lon - this.lon)
        val originLat = Math.toRadians(this.lat)
        val destinationLat = Math.toRadians(finalPosition.lat)

        val a = Math.pow(Math.sin(dLat / 2), 2.toDouble()) + Math.pow(Math.sin(dLon / 2), 2.toDouble()) * Math.cos(originLat) * Math.cos(destinationLat)
        val c = 2 * Math.asin(Math.sqrt(a))

        return BigDecimal(earthRadiusKm * c).setScale(4, RoundingMode.DOWN).toDouble()
    }

    companion object {
        const val earthRadiusKm: Double = 6372.8
        private const val multiplier: Double = 1e-5

        val decodePolyline = { encodedPath: String ->
            val len = encodedPath.length
            val path: MutableList<GeoPosition> = ArrayList(len / 2)
            var index = 0
            var lat = 0
            var lng = 0

            while (index < len) {
                var result = 1
                var shift = 0
                var b: Int

                do {
                    b = encodedPath[index++].toInt() - 63 - 1
                    result += b shl shift
                    shift += 5
                } while (b >= 0x1f)

                lat += if (result and 1 != 0) (result shr 1).inv() else result shr 1
                result = 1
                shift = 0

                do {
                    b = encodedPath[index++].toInt() - 63 - 1
                    result += b shl shift
                    shift += 5
                } while (b >= 0x1f)

                lng += if (result and 1 != 0) (result shr 1).inv() else result shr 1

                path.add(GeoPosition(
                        BigDecimal(lat * multiplier).setScale(5, RoundingMode.DOWN).toDouble(),
                        BigDecimal(lng * multiplier).setScale(5, RoundingMode.DOWN).toDouble()
                ))
            }

            path
        }
    }
}
