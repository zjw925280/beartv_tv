package com.gys.play.util

import java.math.RoundingMode
import java.text.DecimalFormat

class ConversionUtil {
    companion object {
        fun IntToWString(number: Int): String {
            if (number > 1000) {
                val df = DecimalFormat("#0.0")
                df.roundingMode = RoundingMode.DOWN
                return df.format(number.toDouble() / 1000)
            }
            return number.toString()
        }

        fun IntToWString(number: Int, ratio: Int): String {
            if (number > 1000) {
                val df = DecimalFormat("#0.0")
                df.roundingMode = RoundingMode.DOWN
                return df.format(number.toDouble() / 1000)
            }
            return number.toString()
        }

        fun LongToWString(number: Long, ratio: Int): String {
            if (number > 1000) {
                val df = DecimalFormat("#0.0")
                df.roundingMode = RoundingMode.DOWN
                return df.format(number.toDouble() / 1000)
            }
            return number.toString()
        }
        fun LongToWString(number: Long): String {
            if (number > 1000) {
                val df = DecimalFormat("#0.0")
                df.roundingMode = RoundingMode.DOWN
                return df.format(number.toDouble() / 1000)
            }
            return number.toString()
        }

        fun DoubleToWString(number: Double): String {
            if (number > 1000) {
                val df = DecimalFormat("#0.0")
                df.roundingMode = RoundingMode.DOWN
                return df.format(number / 1000)
            }
            return number.toString()
        }
    }
}