package com.gys.play.http.jj;


import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * 使用rx的HttpService示例类
 */
public interface HttpService {

    @POST("{path}")
    Observable<String> httpPost(@Path(value = "path", encoded = true) String path, @Header("data") String data);

    @POST("{path}")
    Observable<BaseData> httpPostBaseData(@Path(value = "path", encoded = true) String path, @Header("data") String data);

    @POST
    Observable<String> httpPostByUrl(@Url String url);

    @FormUrlEncoded
    @POST
    Observable<String> httpPostByUrl(@Url String url, @FieldMap Map<String, Object> params);

//    @POST
//    Observable<String> httpPostByUrl(@Url String url, @QueryMap Map<String, Object> params);


    //@Path     将path拼接到baseUrl中  @POST("{path}")   @Path(value = "path", encoded = true) String path
    //@Url      使用URL，忽略baseUrl配置
    //@Header   将参数配置在header中
    //@Query    一般是把key-value拼接到url的后面，?key=value&key1=value1
    //@Field    配合@FormUrlEncoded使用，其实就是表单提交的方式

}
