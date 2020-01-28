package com.vferras.robotMeasurement.domain

import io.kotlintest.matchers.string.shouldMatch
import org.junit.jupiter.api.Test
import java.io.File
import java.io.PrintStream

class RobotTest {

    @Test
    fun `given a valid polyline when starting the robot then the expected output is written`() {

        val file = File("src/test/resources/test_output.txt")

        if(file.exists()) file.delete()

        file.createNewFile()

        val output = PrintStream(file)

        System.setOut(output)

        Robot()
                .feed(Polyline("mpjyHx`i@VjAVKnAh@BHHX@LZR@Bj@Ml@WWc@]w@bAyAfBmCb@o@pLeQfCsDVa@@OD" +
                        "QR}AJ{A?{BGuAD_@FKb@MTUX]Le@^kBVcAVo@Ta@|EaFh@m@FWaA{DCo@q@mCm@cC{A_GWeA}@sGSeAcA_EOSMa@}A" +
                        "_GsAwFkAiEoAaFaBoEGo@]_AIWW{AQyAUyBQqAI_BFkEd@aHZcDlAyJLaBPqDDeD?mBEiA}@F]yKWqGSkICmCIeZIuZ" +
                        "i@_Sw@{WgAoXS{DOcAWq@KQGIFQDGn@Y`@MJEFIHyAVQVOJGHgFRJBBCCSKBcAKoACyA?m@^yVJmLJ{FGGWq@e@eBIe" +
                        "@Ei@?q@Bk@Hs@Le@Rk@gCuIkJcZsDwLd@g@Oe@o@mB{BgHQYq@qBQYOMSMGBUBGCYc@E_@H]DWJST?JFFHBDNBJ?LE" +
                        "D?LBv@WfAc@@EDGNK|@e@hAa@`Bk@b@OEk@Go@IeACoA@a@PyB`@yDDc@e@K{Bi@oA_@w@]m@_@]QkBoAwC{BmAeA" +
                        "o@s@uAoB_AaBmAwCa@mAo@iCgAwFg@iDq@}G[uEU_GBuP@cICmA?eI?qCB{FBkCI}BOyCMiAGcAC{AN{YFqD^}FR}C" +
                        "Nu@JcAHu@b@_E`@}DVsB^mBTsAQKkCmAg@[YQOIOvAi@[m@e@s@g@GKCKAEJIn@g@GYGIc@ScBoAf@{A`@uAlBfAG`@"))
                .start()

        val expected = this::class.java.classLoader.getResource("expected_test_output.txt").readText()
                .replace("}", "\\}")
                .replace("{", "\\{")
                .replace("[", "\\[")
                .replace("]", "\\]")

        val actual = this::class.java.classLoader.getResource("test_output.txt").readText()

        actual shouldMatch expected
    }
}
