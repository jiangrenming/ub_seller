package com.example.huangjinding.ub_seller.seller.service.listener;


import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

/**
 * Created by jill on 2016/11/22.
 */

public interface ResponseListener {

    void success(JSONObject jsonObject) throws JSONException;

    void fail(String errCode, String msg);

    void error(Response response, Exception e);

    boolean showToast();

    boolean showLoading();

}
