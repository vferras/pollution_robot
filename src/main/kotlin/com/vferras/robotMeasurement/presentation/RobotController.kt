package com.vferras.robotMeasurement.presentation

import com.vferras.robotMeasurement.domain.Polyline
import com.vferras.robotMeasurement.domain.Robot
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RobotController {

    @PostMapping("/robot/v1/start")
    fun start(@RequestBody input : RobotInputDto) =
            Robot()
                    .feed(Polyline(input.value))
                    .start()

    @GetMapping("/robot/v1/stop")
    fun stop() =
            Robot()
                    .stop()

    @PostMapping("/robot/v1/report")
    fun report() =
            Robot()
                    .report()
}
