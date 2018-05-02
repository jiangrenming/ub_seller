package com.example.huangjinding.ub_seller.seller.service;

import android.util.Log;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.StringConvert;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by jill on 2016/11/22.
 */

public abstract class JSONObjectCallback extends AbsCallback<JSONObject> {

    @Override
    public JSONObject convertSuccess(Response response) throws Exception {
        String s = StringConvert.create().convertSuccess(response);
        Log.i("li", s + "---------");
        response.close();
        return new JSONObject(s);
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
    }
}
