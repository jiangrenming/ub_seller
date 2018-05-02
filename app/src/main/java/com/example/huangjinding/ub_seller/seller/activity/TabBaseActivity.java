package com.example.huangjinding.ub_seller.seller.activity;

import android.view.KeyEvent;

import com.example.huangjinding.ub_seller.seller.base.BaseActivity;


public abstract class TabBaseActivity extends BaseActivity {
    protected long exitTime = 0L;

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(!intercept()){
//            return super.onKeyDown(keyCode, event);
//        }
//
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                showToast("再按一次退出");
//                exitTime = System.currentTimeMillis();
//            } else {
//                System.exit(0);
//            }
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

    public boolean intercept(){
        return true;
    }
}
