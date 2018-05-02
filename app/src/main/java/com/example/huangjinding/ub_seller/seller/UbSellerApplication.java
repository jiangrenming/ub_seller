package com.example.huangjinding.ub_seller.seller;

import android.app.Application;
import android.content.Context;

import com.example.huangjinding.ub_seller.seller.service.CrashHandler;

/**
 * Created by Administrator on 2018/3/7/007.
 */

public class UbSellerApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;
        CrashHandler.getInstance().init(this);//初始化全局异常管理
    }

    public static Context getContext(){
        return mContext;
    }


}
