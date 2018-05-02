package com.example.huangjinding.ub_seller.seller.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.huangjinding.ub_seller.seller.util.ToastUtils;
import com.lidroid.xutils.ViewUtils;

import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by huangjinding on 2017/6/1.
 */

public abstract class BaseActivity extends AppCompatActivity implements Viewable {
    private LoadingAction loadingAction;

    public BaseActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 沉浸状态栏
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarCompat.init(this, getBgColor()); // 填充
        loadingAction = new LoadingAction(this);

        initSoft();
        ViewUtils.inject(this);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    protected  void initSoft(){};

    protected abstract int getLayoutId();
    /**
     * 设置浸润背景色
     * @return
     */
    protected int getBgColor(){
        return Color.parseColor("#CCCCCC");
    }

    /**
     * 初始化视图
     */
    protected void initView(){}

    /**
     * 初始化数据
     */
    protected void initData(){}

    /**
     * 初始化监听
     */
    protected void initListener(){}

    /**
     * 显示提示
     * @param message
     */
    public void showToast(String message){
        ToastUtils.showCenter(this, message);
    }

    public void startActivity(Class clazz){
        Intent intent = new Intent(this, clazz);
        this.startActivity(intent);
    }

    public void startActivity(Class clazz, Map<String, String> map){
        Intent intent = new Intent(this, clazz);
        if(map != null) {
            for (String key : map.keySet()) {
                intent.putExtra(key, map.get(key));
            }
        }
        this.startActivity(intent);
    }

    /**
     * 等待提示框
     */
    public void addLoading() {
        loadingAction.add();
    }
    /**
     * 等待框消失
     */
    public void removeLoading() {
        loadingAction.remove();
    }

    public String getIntentValue(String key){
        return getIntent().getStringExtra(key);
    }

    public void refresh(){
        initView();
        initListener();
        initData();
    }

    public BaseActivity getTargetActivity(){
        return this;
    }

}
