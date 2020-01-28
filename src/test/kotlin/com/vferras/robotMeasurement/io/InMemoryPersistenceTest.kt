package com.vferras.robotMeasurement.io

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test

class InMemoryPersistenceTest {
    @Test
    fun `given a value when storing it in memory then should be returned from memory`() {
        val key = "KeyTest"
        val value = "ValueTest"

        InMemoryPersistence.upsertToStorage(key, value)

        InMemoryPersistence.getFromStorage(key) shouldBe value
    }

    @Test
    fun `given a value when storing it in memory and deleting it then should not be returned from memory`() {
        val key = "KeyTest"
        val value = "ValueTest"

        InMemoryPersistence.upsertToStorage(key, value)

        InMemoryPersistence.getFromStorage(key) shouldBe value

        InMemoryPersistence.removeFromStorage(key)

        InMemoryPersistence.getFromStorage(key) shouldBe ""
    }
}
