package com.example.huangjinding.ub_seller.seller.base;

import android.content.Context;

import com.example.huangjinding.ub_seller.seller.util.MySharedPreference;
import com.example.huangjinding.ub_seller.seller.util.SharedKeyConstant;

/**
 * Created by huangjinding on 2017/6/6.
 */

public class LoginAction {
    public static void login(String token, Context context) {
        MySharedPreference.save(SharedKeyConstant.TOKEN, token, context);
        GlobalInfo.userToken = token;

    }

    public static void logout(Context context) {
        MySharedPreference.remove(SharedKeyConstant.TOKEN, context);
        GlobalInfo.userToken = "";
        GlobalInfo.shopCartCount = 0;
        GlobalInfo.saveMobile = "";
    }

    public static void saveMobile(String mobile, Context context) {
        MySharedPreference.save(SharedKeyConstant.SAVE_MOBILE, mobile, context);
        GlobalInfo.saveMobile = mobile;
    }

}

