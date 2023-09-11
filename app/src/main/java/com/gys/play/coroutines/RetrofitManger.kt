package com.gys.play.coroutines

import com.android.liba.network.ApiManager
import com.android.liba.network.protocol.RequestArgs
import com.gys.play.http.AppNetService
import com.gys.play.http.NovelRequestArgs
import rxhttp.toDownloadAwait
import rxhttp.wrapper.entity.Progress
import rxhttp.wrapper.param.RxHttp

object Quest {

    val api by lazy { getApiService() }

    private fun getApiService(): AppNetService {
        val retrofit = ApiManager.getApiManager().retrofit
        return retrofit.create(AppNetService::class.java)
    }


    fun getHead(service: String, build: (RequestArgs.() -> RequestArgs)? = null): String {

        val args = NovelRequestArgs.init(service)

        return build?.invoke(args)?.build() ?: args.build()
    }

    fun getPageHead(
        service: String,
        page: Int,
        limit: Int = 10,
        map: HashMap<String, String>? = null,
        build: (RequestArgs.() -> RequestArgs)? = null
    ): String {

        val args = NovelRequestArgs.init(service)
        args.add("page", "$page")
        args.add("limit", "$limit")

        map?.forEach {
            args.add(it.key, "${it.value}")
        }

        return build?.invoke(args)?.build() ?: args.build()
    }

    fun downLoad(url: String, path: String, progress: (suspend (Progress) -> Unit)? = null) =
        RxHttp.get(url).tag(path)
            .toDownloadAwait(path, progress = progress, append = true)
}






