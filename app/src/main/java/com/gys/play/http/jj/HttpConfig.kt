package com.gys.play.http.jj

import android.content.Context
import android.text.TextUtils
import com.android.liba.network.compose.NetworkCompose
import com.android.liba.util.GsonUtil
import com.android.liba.util.UIHelper
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Type
open class HttpConfig : BaseHttpMethods<HttpService>() {

    override fun getContext(): Context {
        return MyApplication.getInstance()
    }

    public override fun getBaseUrl(): String {
        return Config.BaseUrl
    }

    override fun getConnectTimeOutSecond(): Int {
        return 30
    }

    override fun getReadTimeOutSecond(): Int {
        return 30
    }

    fun getHttpErrorDefaultStr(): String {
        return MyApplication.getInstance().getString(R.string.http_error)
    }

    fun <T> doHttp(
        path: String,
        headerMap: Map<String, Any>,
        subscriber: SimpleSubscriber<T>, type: Type
    ) {
        doHttp(path, headerMap, object : SimpleSubscriber<BaseData>() {
            override fun onSucceed(result: BaseData) {
                UIHelper.showLog(TAG, "onSucceed $result")
                if (result.ret !== 200) {
                    UIHelper.showLog(TAG, "http success but ret != 200 ?")
                    val msg: String = result.msg
                    onFail(if (TextUtils.isEmpty(msg)) getHttpErrorDefaultStr() else msg)
                    return
                }
                try {
                    subscriber.onSucceed(GsonUtil.instance().fromJson(result.data, type))
                } catch (e: Exception) {
                    UIHelper.showLog(TAG, "http success 数据解析失败")
                    onFail(getHttpErrorDefaultStr())
                }
            }

            override fun onFail(error: String) {
                UIHelper.showLog(TAG, "onFail $error")
                subscriber.onFail(error)
            }
        })
    }

    private fun doHttp(
        path: String,
        headerMap: Map<String, Any>,
        subscriber: SimpleSubscriber<BaseData>?
    ) {
        if (subscriber == null) return
        if (!NetworkCompose.isNetworkAvailable()) {
            subscriber.onFail(MyApplication.getInstance().getString(R.string.network_error))
            return
        }
        //默认用path headers
        val header = AESEncrypt.encode(GsonUtil.instance().toJson(headerMap))
        val objectObservable: Observable<BaseData> =
            httpService.httpPostBaseData(path, header)
                ?: return
        objectObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber)
    }

    fun <T> doHttpOther(
        url: String?,
        paramsMap: Map<String, Any>?,
        subscriber: SimpleSubscriber<T>, type: Type?
    ) {
        doHttpOther(url, paramsMap, object : SimpleSubscriber<String>() {
            override fun onSucceed(result: String) {
                UIHelper.showLog(TAG, "onSucceed $result")

                try {
                    subscriber.onSucceed(GsonUtil.instance().fromJson(result, type))
                } catch (e: Exception) {
                    UIHelper.showLog(TAG, "http success 数据解析失败")
                    onFail(getHttpErrorDefaultStr())
                }
            }

            override fun onFail(error: String) {
                UIHelper.showLog(TAG, "onFail $error")
                subscriber.onFail(error)
            }
        })
    }

    fun doHttpOther(
        url: String?,
        paramsMap: Map<String, Any>?,
        subscriber: SimpleSubscriber<String>?
    ) {
        if (subscriber == null) return
        if (!NetworkCompose.isNetworkAvailable()) {
            subscriber.onFail(MyApplication.getInstance().getString(R.string.network_error))
            return
        }
        val objectObservable = if (paramsMap == null || paramsMap.isEmpty()) {
            httpService.httpPostByUrl(url)
        } else {
            httpService.httpPostByUrl(url, paramsMap)
        }
        if (objectObservable == null) return
        objectObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber)
    }
}