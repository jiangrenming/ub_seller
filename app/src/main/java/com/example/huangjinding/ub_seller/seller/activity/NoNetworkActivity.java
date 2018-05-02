package com.example.huangjinding.ub_seller.seller.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.UbSellerApplication;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.base.GlobalInfo;
import com.example.huangjinding.ub_seller.seller.service.AppManager;
import com.example.huangjinding.ub_seller.seller.util.Tools;

import butterknife.OnClick;



public class NoNetworkActivity extends BaseActivity {

    @OnClick({R.id.tv_reload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_reload:
                boolean networkAvailable = Tools.isNetworkAvailable(this);
                if(networkAvailable){
                    if(GlobalInfo.viewable == null)
                        return;

                    finish();
                    GlobalInfo.viewable.refresh();
                }else{
                    showToast("请检查您的网络");
                }

                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_no_network;
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean networkAvailable = Tools.isNetworkAvailable(this);
        if(networkAvailable){
            if(GlobalInfo.viewable == null)
                return;

            finish();
            GlobalInfo.viewable.refresh();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

//    ActivityManager manager = (ActivityManager)getApplicationContext().getSystemService(ACTIVITY_SERVICE); //获取应用程序管理器

    private void exit() {

            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            AppManager.getAppManager().finishAllActivity();

//        manager.killBackgroundProcesses(getPackageName()); //强制结束当前应用程序
            // 利用handler延迟发送更改状态信息
//            mHandler.sendEmptyMessageDelayed(0, 2000);

//            finish();
            System.exit(0);
        }

}
