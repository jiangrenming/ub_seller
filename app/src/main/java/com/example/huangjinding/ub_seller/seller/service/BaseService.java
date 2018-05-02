package com.example.huangjinding.ub_seller.seller.service;

import android.app.Activity;
import android.text.TextUtils;

import com.example.huangjinding.ub_seller.seller.LoginActivity;
import com.example.huangjinding.ub_seller.seller.activity.NoNetworkActivity;
import com.example.huangjinding.ub_seller.seller.base.GlobalInfo;
import com.example.huangjinding.ub_seller.seller.base.Viewable;
import com.example.huangjinding.ub_seller.seller.service.cache.CacheHelper;
import com.example.huangjinding.ub_seller.seller.service.listener.BaseResponseListener;
import com.example.huangjinding.ub_seller.seller.service.listener.CacheCommonResultListener;
import com.example.huangjinding.ub_seller.seller.service.listener.ResponseListener;
import com.example.huangjinding.ub_seller.seller.service.listener.ResultListener;
import com.example.huangjinding.ub_seller.seller.util.MyActivityManager;
import com.example.huangjinding.ub_seller.seller.util.PullRefreshable;
import com.example.huangjinding.ub_seller.seller.util.Tools;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.OkGo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.huangjinding.ub_seller.seller.base.GlobalInfo.userToken;

/**
 * 服务接口的基类
 */

public abstract class BaseService {
//    public static String BASE_URL = "http://192.168.31.102:8080/youbao/app/";
//    public static String BASE_IMAGE_URL="http://192.168.31.102:8080/youbao/";
//    public static String BASE_URL = "http://192.168.31.102:8089/youbao/app/";
//    public static String BASE_IMAGE_URL="http://192.168.31.102:8089/youbao/";
//        public static String BASE_URL = "http://118.178.182.6/youbao/app/";  //测试
//        public static String BASE_IMAGE_URL = "http://118.178.182.6/youbao/";
//
    public static String BASE_URL = "http://116.62.162.19/youbao/app/";  //正式
    public static String BASE_IMAGE_URL = "http://116.62.162.19/youbao/";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_DATA = "data";
    private static final String FIELD_NEXT = "next";
    private static final String FIELD_ERROR_CODE = "errCode";
    private static final String FIELD_MSG = "msg";
    private static final String FIELD_TOKEN = "token";

    private static final String RESULT_SUCCESS = "SUCCESS";
    protected Viewable context;

    public BaseService(Viewable context) {
        this.context = context;
    }

    //get，将参数放到url后面，支持的URL路径：http://www.xx.com/app?id=1&name=2
    protected void get(String url, Map<String, String> params, ResponseListener responseListener) {
        if (responseListener instanceof BaseResponseListener) {
            ResultListener resultListener = ((BaseResponseListener) responseListener).resultListener;
            if (resultListener instanceof CacheCommonResultListener) {
                ((CacheCommonResultListener) resultListener).cacheKey = CacheHelper.url2Key(url, params);
                Object cache = ((CacheCommonResultListener) resultListener).getCache();
                if (cache != null) {
                    try {
                        resultListener.successHandle(cache);
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (responseListener.showLoading())
            context.addLoading();

        params = assembleParam(params);
        OkGo.get(BASE_URL + url).params(params).tag(url).execute(new DefaultJsonObjectCallback(responseListener));
    }

    protected void get(String url, Map<String, String> params, ResponseListener responseListener, boolean useCache) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        params = assembleParam(params);
        if (responseListener.showLoading()) {
            context.addLoading();
        }

        OkGo.get(BASE_URL + url).params(params).tag(url).execute(new DefaultJsonObjectCallback(responseListener));
    }

    //post时，将参数放到url后面，支持的URL路径：http://www.xx.com/app?id=1&name=2
    protected void post(String url, Map<String, String> params, ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (responseListener.showLoading())
            context.addLoading();

        params = assembleParam(params);
        OkGo.post(BASE_URL + url).params(params).tag(url).execute(new DefaultJsonObjectCallback(responseListener));
    }

    //post时，将参数放到body，body的内容为json格式，支持的URL路径：http://www.xx.com/app
    protected void postJson(String url, Map<String, Object> params, ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (responseListener.showLoading())
            context.addLoading();

        JSONObject jsonObject = new JSONObject(params);
        Map<String, String> baseParam = assembleParam(null);
        String suffix = "&";
        if (url.indexOf("?") == -1) {
            suffix = "?";
        }
        String urlParam = suffix + map2UrlParamString(baseParam);
        OkGo.post(BASE_URL + url + urlParam).upJson(jsonObject).tag(url).execute(new DefaultJsonObjectCallback(responseListener));
    }

    private String map2UrlParamString(Map<String, String> paramMap) {
        String urlParam = "";
        for (String key : paramMap.keySet()) {
            urlParam += key + "=" + paramMap.get(key) + "&";
        }
        if (urlParam.endsWith("&")) {
            urlParam = urlParam.substring(0, urlParam.length() - 1);
        }

        return urlParam;
    }

    protected void postArray(String url, Map<String, String> map, Map<String, String[]> params, String key, List<File> files, ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (responseListener.showLoading())
            context.addLoading();

        map = assembleParam(map);
        params = assembleArrayParam(params);
        OkGo.post(BASE_URL + url)
                .params(map)
                .addFileParams(key, files)
                .tag(url)
                .isMultipart(true)
                .execute(new DefaultJsonObjectCallback(responseListener));
    }

    protected void postComment(String orderCode, List<String> goodsCodes, List<String> skuDesc, List<String> levels, List<String> contentTxt, List<File> files1, List<File> files2, List<File> files3, List<File> files4, List<File> files5, ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (responseListener.showLoading())
            context.addLoading();

        Map<String, String> params = new HashMap<>();
        params.put("orderCodes", orderCode);
        //    params.put("v", GlobalInfo.appVersionName);
        params.put(FIELD_TOKEN, userToken);

        String url = UrlConstant.EVALUAT;
        OkGo.post(BASE_URL + url)
                .params(params)
                .addUrlParams("goodsCodes", goodsCodes)
                .addUrlParams("skuDesc", skuDesc)
                .addUrlParams("levels", levels)
                .addUrlParams("contentTxt", contentTxt)
                .addFileParams("pic1", files1)
                .addFileParams("pic2", files2)
                .addFileParams("pic3", files3)
                .addFileParams("pic4", files4)
                .addFileParams("pic5", files5)
                .tag(url)
                .isMultipart(true)
                .execute(new DefaultJsonObjectCallback(responseListener));
    }

    protected void postFile(String url, Map<String, String> params, String key, List<File> files, ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (responseListener.showLoading())
            context.addLoading();

        params = assembleParam(params);
        OkGo.post(BASE_URL + url)
                .params(params)
                .addFileParams(key, files)
                .tag(url)
                .isMultipart(true)
                .execute(new DefaultJsonObjectCallback(responseListener));
    }

    protected void postOneFile(String url, Map<String, String> params, String key, File file, ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (responseListener.showLoading())
            context.addLoading();

        params = assembleParam(params);
        OkGo.post(BASE_URL + url)
                .params(params)
                .params(key, file)
                .tag(url)
                .isMultipart(true)
                .execute(new DefaultJsonObjectCallback(responseListener));
    }

    protected Map<String, String[]> assembleArrayParam(Map<String, String[]> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }


    protected Map<String, String> assembleParam(Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }

        String userToken = GlobalInfo.userToken;
        if (TextUtils.isEmpty(userToken)) {
            userToken = "\"\"";
        }
        params.put(FIELD_TOKEN, userToken);
        //   params.put("v", GlobalInfo.appVersionName);
        return params;
    }


    protected Map<String, Object> assembleObjectParam(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(FIELD_TOKEN, userToken);
        //   params.put("v", GlobalInfo.appVersionName);

        return params;
    }

    private boolean beforeHttp() {
        boolean networkAvailable = Tools.isNetworkAvailable(context.getTargetActivity());
        Activity currentActivity = MyActivityManager.getInstance().getCurrentActivity();
        boolean isCurNoNetwork = false;
        if (currentActivity != null && currentActivity instanceof NoNetworkActivity) {
            isCurNoNetwork = true;
        }

        if (!networkAvailable && !isCurNoNetwork) {
            context.startActivity(NoNetworkActivity.class);
            GlobalInfo.viewable = context.getTargetActivity();
        }

        return networkAvailable;
    }

    protected class DefaultJsonObjectCallback extends JSONObjectCallback {

        private ResponseListener responseListener;

        public DefaultJsonObjectCallback(ResponseListener responseListener) {
            this.responseListener = responseListener;
        }

        @Override
        public void onSuccess(JSONObject jsonObject, Call call, Response response) {
            try {
                int code = jsonObject.getInt(FIELD_CODE);
                if (responseListener.showLoading())
                    context.removeLoading();
                if (200 == code) {
                    responseListener.success(jsonObject);

                    if (isPullRefresh()) {
                        String string = jsonObject.getString("data");
                        if ("null".equals(string)) {
                            return;
                        }
                        JSONObject jsonData = jsonObject.getJSONObject(FIELD_DATA);
                        if (jsonData == null) {
                            return;
                        }
                        String next = jsonData.getString(FIELD_NEXT);
                        boolean hasNext = "1".equals(next);
                        refreshHandle(hasNext, true);
                    }
                } else {
                    if (isPullRefresh()) {
                        refreshHandle(true, false);
                    }
                    String msg = jsonObject.getString(FIELD_MSG);
                    if (responseListener.showToast() && ("Token无效".equals(msg) || "请先登录".equals(msg) || "请重新登录".equals(msg))) {
                        if (GlobalInfo.needGoLogin) {
                            return;
                        }

                        OkGo.getInstance().cancelAll();
                        GlobalInfo.needGoLogin = true;
                        context.showToast(msg);

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                context.startActivity(LoginActivity.class);
                            }
                        };
                        timer.schedule(task, 200); // 1秒跳转
                        return;
                    }
                    responseListener.fail(jsonObject.getString(FIELD_CODE), msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if (responseListener.showToast())
                    context.showToast("服务器数据返回出现异常");
            } catch (JsonSyntaxException e) {
                if (responseListener.showToast())
                    context.showToast("服务器数据返回出现问题");
            }
        }

        @Override
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
            responseListener.error(response, e);
            if (isPullRefresh()) {
                refreshHandle(true, false);
            }
        }
    }

    private boolean isPullRefresh() {
        return context instanceof PullRefreshable;
    }

    private void refreshHandle(boolean hasNext, boolean loadSuccess) {
        ((PullRefreshable) context).refreshEnd(hasNext, loadSuccess);
    }


}
