package com.vferras.doMeasurement.domain

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import kotlin.math.roundToInt

@SpringBootTest
class PolylineTest {

	@Test
	fun `given a valid polyline when decoding it then a list of valid points should be returned`() {
		val validPolyline = "gjp{FudcLyD{GaD{G"

		Polyline(validPolyline).getPoints().map {
			it.size shouldBe 3
			it shouldBe listOf(
                    GeoPosition(41.37652, 2.15131),
                    GeoPosition(41.37745, 2.15273),
                    GeoPosition(41.37826, 2.15415)
			)
		}
	}

	@Test
	fun `given an invalid polyline when decoding it then an exception should be thrown`() {
		val invalidPolyline = "aaa"

		Polyline(invalidPolyline).getPoints().isLeft() shouldBe true
	}

	@Test
	fun `given a polyline when asking for the interpolated points then should equal distance`() {
		val validPolyline = "gjp{FudcLyD{GaD{G"
		val position1 = GeoPosition(41.37652, 2.15131)
		val position2 = GeoPosition(41.37826, 2.15415)

		val distance = haversineDistance(position1, position2)

		Polyline(validPolyline).getInterpolatedPoints().size shouldBe distance.roundToInt()
	}
}
