package com.android.liba.util

import java.util.regex.Pattern

/**
 * @deprecated Kotlin处理中文和Unicode互转工具类
 */
object UnicodeUtils {

    /**
     * 将字符串转成Unicode编码，包括但不限于中文
     *
     * @param src 原始字符串，包括但不限于中文
     * @return Unicode编码字符串
     */
    fun encode(src: String): String {
        val builder = StringBuilder()
        for (element in src) {
            // 如果你的Kotlin版本低于1.5，这里 element.code 会报错 找不到方法,请替换成:
            // Kotlin < 1.5
            // var s = Integer.toHexString(element.toInt())
            // Kotlin >= 1.5
            var s = Integer.toHexString(element.code)

            if (s.length == 2) {// 英文转16进制后只有两位，补全4位
                s = "00$s"
            }
            builder.append("\\u$s")
        }
        return builder.toString()
    }

    /**
     * Unicode编码转'中文'String
     * @param text Unicode编码
     * @return 中文可读信息
     */
    fun decode(text: String): String {
        val reUnicode = Pattern.compile("\\\\u([0-9a-zA-Z]{4})")
        val m = reUnicode.matcher(text)
        val sb = StringBuffer(text.length)
        while (m.find()) {
            val group = m.group(1) ?: continue
            m.appendReplacement(
                sb,
                group.toInt(16).toChar().toString()
            )
        }
        m.appendTail(sb)
        return sb.toString()
    }
}