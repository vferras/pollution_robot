package com.vferras.robotMeasurement.domain

import com.vferras.robotMeasurement.io.InMemoryPersistence
import com.vferras.robotMeasurement.io.Output.Companion.printOutput

class Robot {
    private lateinit var polyline: Polyline

    fun start() {
        InMemoryPersistence.upsertToStorage("shouldStop", "false")

        val totalPoints = polyline.getInterpolatedPoints()
        val measurements = mutableListOf<Measurement>()

        for(index in totalPoints.indices) {
            if(InMemoryPersistence.getFromStorage("shouldStop") == "true") return

            if(isTimeToMeasure(index)) measurements.add(getRandomMeasurement())

            zoneCheck(totalPoints[index], measurements)

            if(isTimeToReport(index)) {
                printOutput(totalPoints[index], index, getPollutionLevel(measurements), "robot")

                measurements.clear()
                InMemoryPersistence.removeFromStorage("shouldReport")

                Thread.sleep(200)
            }
        }
    }

    val stop = { InMemoryPersistence.upsertToStorage("shouldStop", "true") }

    val feed = { polyline: Polyline ->
        this.polyline = polyline

        this
    }

    val report = { InMemoryPersistence.upsertToStorage("shouldReport", "true") }

    private val isTimeToMeasure = { i: Int -> i % 100 == 0 }

    private val isTimeToReport = {
        i: Int -> i % (convertMinutesToSeconds(15)) == 0 || InMemoryPersistence.getFromStorage("shouldReport") == "true"
    }

    private val getRandomMeasurement = { Measurement((0..160).shuffled().first()) }

    private val getPollutionLevel = { measurements: List<Measurement> ->
        when(measurements.map { it.pm25 }.average().toInt()) {
            in 0..50 -> "Good"
            in 51..100 -> "Moderate"
            in 101..150 -> "USG"
            else -> "Unhealthy"
        }
    }

    private val zoneCheck = { position: GeoPosition, measurements: List<Measurement> ->
        checkIfBuckinghamZone(position, measurements)
        checkIfTemplateStationZone(position, measurements)
    }

    private val checkIfBuckinghamZone = { position: GeoPosition, measurements: List<Measurement> ->
        if(haversineDistance(position, GeoPosition(51.501299, -0.141935)) in 99.9..100.1) {
            printOutput(position, 0, getPollutionLevel(measurements), "Buckingham")
        }
    }

    private val checkIfTemplateStationZone = { position: GeoPosition, measurements: List<Measurement> ->
        if(haversineDistance(position, GeoPosition(51.510852, -0.114165)) in 99.9..100.1) {
            printOutput(position, 0, getPollutionLevel(measurements), "TemplateStation")
        }
    }

    private val convertMinutesToSeconds = { minutes: Int -> minutes * 60 }
}
