package com.example.huangjinding.ub_seller.seller.service.listener;

import org.json.JSONException;

import okhttp3.Response;

/**
 * Created by jill on 2016/11/22.
 */

public interface ResultListener<T> {

    void successHandle(T result) throws JSONException;

    void failHandle(String code, String result)throws JSONException;

    void errorHandle(Response response, Exception e)throws JSONException;

}
