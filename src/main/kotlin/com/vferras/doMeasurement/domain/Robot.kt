package com.vferras.doMeasurement.domain

import java.time.Instant

class Robot {
    private lateinit var polyline: Polyline

    fun feed(polyline: Polyline): Robot {
        this.polyline = polyline

        return this
    }

    fun start() {
        val totalPoints = polyline.getInterpolatedPoints()

        val measurements = mutableListOf<Measurement>()

        for(index in 1..totalPoints.size) {
            if(isTimeToMeasure(index)) measurements.add(getRandomMeasurement())

            if(isTimeToReport(index)) {
                printOutput(totalPoints[index], index, getPollutionLevel(measurements))

                measurements.clear()
            }
        }
    }

    val isTimeToMeasure = { i: Int -> i % 100 == 0 }

    val isTimeToReport = { i: Int -> i % (15 * 60) == 0 }

    val getRandomMeasurement = { Measurement((0..160).shuffled().first()) }

    val getPollutionLevel = { measurements: List<Measurement> ->
        when(measurements.map { it.pm25 }.average().toInt()) {
            in 0..50 -> "Good"
            in 51..100 -> "Moderate"
            in 101..150 -> "USG"
            else -> "Unhealthy"
        }
    }

    val printOutput = { location: GeoPosition, i: Int, level: String ->
        println("{“timestamp”: ${timestamp(i)}, " +
                "“location”: {“lat”: ${location.lat}, “lng”: ${location.lon}}, " +
                "“level”: “$level”,“source”: “robot”}")
    }

    val timestamp = { i: Int -> Instant.now().plusSeconds(i.toLong()).epochSecond }
}
