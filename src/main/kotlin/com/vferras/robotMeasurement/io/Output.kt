package com.vferras.robotMeasurement.io

import com.vferras.robotMeasurement.domain.GeoPosition
import java.time.Instant

class Output private constructor() {

    companion object {
        val printOutput = { location: GeoPosition, i: Int, level: String, source: String ->
            println("{“timestamp”: ${timestamp(i)}, " +
                    "“location”: {“lat”: ${location.lat}, “lng”: ${location.lon}}, " +
                    "“level”: “$level”, “source”: “$source”}")
        }

        private val timestamp = { i: Int -> Instant.now().plusSeconds(i.toLong()).epochSecond }
    }
}
