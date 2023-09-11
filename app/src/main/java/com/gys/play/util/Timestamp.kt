package com.gys.play.util

import java.text.ParsePosition
import java.text.SimpleDateFormat


class Timestamp {
    companion object {


        fun transToString(time: Long): String {
            return SimpleDateFormat("yyyy-MM-dd").format(time)
        }

        fun transToStringm(time: Long): String {
            return SimpleDateFormat("yyyy-MM-dd HH:mm").format(time)
        }

        fun transToStringMDHM(time: Long): String {
            return SimpleDateFormat("MM-dd HH:mm").format(time)
        }



        fun transToTimeStamp(date: String): Long {
            return SimpleDateFormat("yyyy-MM-dd").parse(date, ParsePosition(0)).time
        }
    }
}
