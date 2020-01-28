package com.vferras.robotMeasurement.io

class InMemoryPersistence private constructor () {

    companion object {
        private val storage = mutableMapOf<String, String>()

        val upsertToStorage = { key: String, value: String -> storage[key] = value }

        val getFromStorage = { key: String -> storage[key] ?: "" }

        val removeFromStorage = { key: String -> storage.remove(key) }
    }
}
