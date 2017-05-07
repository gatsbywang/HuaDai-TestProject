package myapplication.httpenginedemo;

import android.content.Context;

import java.util.Map;

/**
 * Created by 花歹 on 2017/5/4.
 * Email:   gatsbywang@126.com
 * Description: http引擎规范
 * Thought: get和post请求，三个参数：url,map,callback
 */

public interface IHttpEngine {

    //get请求
    void get(Context context, String url, Map<String, Object> header, Map<String, Object> params, EngineCallback callback);

    //post请求
    void post(Context context, String url, Map<String, Object> header, Map<String, Object> params, EngineCallback callback);
    //上传

    //下载

    //https
}
