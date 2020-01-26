package com.vferras.doMeasurement.domain

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import kotlin.math.roundToInt

@SpringBootTest
class GeoLocationFunctionsTest {

	@Test
	fun `given two valid locations when calculating the bearing it is returned`() {
		val position1 = GeoPosition(41.37652, 2.15131)
		val position2 = GeoPosition(41.37745, 2.15273)

		calculateBearing(position1, position2) shouldBe 48.885038660570046
	}

	@Test
	fun `given a valid location and bearing when given a distance then a new location is returned`() {
		val position1 = GeoPosition(41.37652, 2.15131)
		val bearing = 48.885038660570046

		val destination = GeoPosition(41.37652591201638, 2.1513190267372875)

		calculateDestinationLocation(position1, bearing, 1.0) shouldBe destination
	}

	@Test
	fun `given valid two positions when using haversine function then distance between two points is returned`() {
		val position1 = GeoPosition(41.37652, 2.15131)
		val position2 = GeoPosition(41.37745, 2.15273)

		val distance = haversineDistance(position1, position2)

		distance shouldBe 157.29999999999998
	}

	@Test
	fun `given two valid locations when asking for location interpolation then a list of points 1m separated returned`() {
		val position1 = GeoPosition(41.37652, 2.15131)
		val position2 = GeoPosition(41.37745, 2.15273)

		val distance = haversineDistance(position1, position2).roundToInt()

		val pointsInBetween = locationInterpolation(position1, position2)

		distance shouldBe pointsInBetween.size
	}

	@Test
	fun `given a degree when converting it then a radian is returned`() {
		degToRad(2.5) shouldBe 0.04363323129985824
	}

	@Test
	fun `given a radian when converting it then a degree is returned`() {
		radToDeg(0.04363323129985824) shouldBe 2.5
	}

	@Test
	fun `given a valid meter value when converting it a km value is returned`() {
		1000.0.toKms() shouldBe 1.0
	}

	@Test
	fun `given a valid km value when converting it a meter value is returned`() {
		1.0.toMs() shouldBe 1000.0
	}
}
