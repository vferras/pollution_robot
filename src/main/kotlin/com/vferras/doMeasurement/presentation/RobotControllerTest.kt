package com.vferras.doMeasurement.presentation

import com.vferras.doMeasurement.domain.Polyline
import com.vferras.doMeasurement.domain.Robot
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RobotControllerTest {

    @PostMapping("/robot/v1/start")
    fun startRobot(@RequestBody input : RobotInputDto) =
            Robot()
                    .feed(Polyline(input.value))
                    .start()
}
