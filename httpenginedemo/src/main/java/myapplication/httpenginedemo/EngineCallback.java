package myapplication.httpenginedemo;

/**
 * Created by 花歹 on 2017/5/4.
 * Email:   gatsbywang@126.com
 * Description: 引擎回调
 * Thinking: 为啥不进行封装？比如加个泛型T，让result返回T。为了避免奇葩的后台返回，success: data{"xx","xx"}
 *  error :data:"",成功返回object,失败返回string。
 *
 */

public interface EngineCallback {

    void onError(Exception e);

    void onSuccess(String result);

    public final EngineCallback DEFAULT_CALL_BACK = new EngineCallback() {
        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
