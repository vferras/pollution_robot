package com.vferras.doMeasurement.domain

import arrow.core.Either
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.ArrayList

data class Polyline(val encodedPolyline: String) {

    fun getInterpolatedPoints(): List<GeoPosition> {
        val allPoints = mutableListOf<GeoPosition>()

        getPoints().map {
            for((index, geoPosition) in it.withIndex()) {
                if(it.size <= index + 1) break

                allPoints.addAll(locationInterpolation(geoPosition, it[index + 1]))
            }
        }

        return allPoints
    }

    fun getPoints(): Either<Exception, MutableList<GeoPosition>> {
        try {
            val multiplier = 1e-5
            val len = encodedPolyline.length
            val path: MutableList<GeoPosition> = ArrayList(len / 2)
            var index = 0
            var lat = 0
            var lng = 0

            while (index < len) {
                var result = 1
                var shift = 0
                var b: Int

                do {
                    b = encodedPolyline[index++].toInt() - 63 - 1
                    result += b shl shift
                    shift += 5
                } while (b >= 0x1f)

                lat += if (result and 1 != 0) (result shr 1).inv() else result shr 1
                result = 1
                shift = 0

                do {
                    b = encodedPolyline[index++].toInt() - 63 - 1
                    result += b shl shift
                    shift += 5
                } while (b >= 0x1f)

                lng += if (result and 1 != 0) (result shr 1).inv() else result shr 1

                path.add(GeoPosition(
                        BigDecimal(lat * multiplier).setScale(5, RoundingMode.DOWN).toDouble(),
                        BigDecimal(lng * multiplier).setScale(5, RoundingMode.DOWN).toDouble()
                ))
            }

            return Either.right(path)
        } catch (exception: IndexOutOfBoundsException) {
            return Either.left(exception)
        }
    }
}
