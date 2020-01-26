package com.vferras.doMeasurement.presentation

import com.fasterxml.jackson.annotation.JsonProperty

data class RobotInputDto(@JsonProperty(value = "input") val value: String)
