package com.mybase.libb.http

import okhttp3.Response
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.parse.TypeParser
import rxhttp.wrapper.utils.convertTo
import java.io.IOException
import java.lang.reflect.Type


@Parser(name = "Response")
open class ResponseParser <T> : TypeParser<T> {

    protected constructor() : super()

    constructor(type: Type) : super(type)

    @Throws(IOException::class)
    override fun onParse(response: Response): T {
        val data: BaseBean<T> = response.convertTo(BaseBean::class, *types)
        var t = data.data //获取data字段
        if (t == null && types[0] === String::class.java) {

            @Suppress("UNCHECKED_CAST")
            t = data.msg as T
        }
        if (data.ret != UrlConfig.QUEST_SUCCESS_CODE || t == null) {
            throw ParseException(data.ret.toString(), data.msg, response)
        }
        return t
    }

}