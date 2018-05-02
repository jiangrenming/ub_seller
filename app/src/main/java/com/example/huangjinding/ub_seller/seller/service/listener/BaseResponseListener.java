package com.example.huangjinding.ub_seller.seller.service.listener;



import com.example.huangjinding.ub_seller.seller.base.Viewable;

import org.json.JSONException;

import okhttp3.Response;

/**
 * Created by jill on 2016/11/22.
 */

public abstract class BaseResponseListener implements ResponseListener {

    protected Viewable context;
    public ResultListener resultListener;

    public BaseResponseListener(Viewable context,ResultListener resultListener){
        this.context = context;
        this.resultListener = resultListener;
    }

    @Override
    public void fail(String errCode, String msg) {
        try {
            resultListener.failHandle(errCode, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void error(Response response, Exception e) {
        try {
            resultListener.errorHandle(response, e);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    public boolean showToast(){
        if(resultListener instanceof ShowableResultListener){
            return ((ShowableResultListener)resultListener).isShowToast();
        }

        return true;
    }

    public boolean showLoading(){
        if(resultListener instanceof ShowableResultListener){
            return ((ShowableResultListener)resultListener).isShowLoading();
        }
        return true;
    }

//    public boolean readCache(){
//        return resultListener instanceof CacheCommonResultListener;
//    }
}
