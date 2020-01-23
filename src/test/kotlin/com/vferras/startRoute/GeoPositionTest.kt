package com.vferras.startRoute

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GeoPositionTest {

	@Test
	fun givenAValidPolylineWhenDecodingItThenAListOfValidPointsShouldBeReturned() {
		val validPolyline = "gjp{FudcLyD{GaD{G"

		val decodedPolyline = GeoPosition.decodePolyline(validPolyline)

		decodedPolyline.size shouldBe 3
		decodedPolyline shouldBe listOf(
				GeoPosition(41.37652, 2.15131),
				GeoPosition(41.37745, 2.15273),
				GeoPosition(41.37826, 2.15415)
		)
	}

	@Test
	fun givenAnInvalidPolylineWhenDecodingItThenAnExceptionShouldBeThrown() {
		val validPolyline = "aaa"

		shouldThrow<StringIndexOutOfBoundsException> {
			GeoPosition.decodePolyline(validPolyline)
		}
	}

	@Test
	fun x() {
		val position1 = GeoPosition(41.37652, 2.15131)
		val position2 = GeoPosition(41.37745, 2.15273)

		val distance = position1.haversine(position2)

		distance shouldBe 0.1573
	}
}
