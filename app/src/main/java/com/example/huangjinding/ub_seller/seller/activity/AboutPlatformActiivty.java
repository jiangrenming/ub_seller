package com.example.huangjinding.ub_seller.seller.activity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.huangjinding.ub_seller.HeadView;
import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.bean.AboutPlatformBean;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;

import org.json.JSONException;

import butterknife.BindView;

/**
 * Created by huangjinding on 2017/6/6.
 */

public class AboutPlatformActiivty extends BaseActivity {
    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.wv_detail)
    WebView wvDetail;
    private SellerService sellerService;

    @Override
    protected int getLayoutId() {
        return R.layout.ub_activity_about_platform;
    }
    protected void initView() {
        super.initView();
        hvHead.setTitle("关于平台");
        WebSettings mSettings = wvDetail.getSettings();
        mSettings.setUseWideViewPort(true);
        mSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mSettings.setUseWideViewPort(true);//关键点
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mSettings.setDisplayZoomControls(false);
        mSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        mSettings.setAllowFileAccess(true); // 允许访问文件
        mSettings.setBuiltInZoomControls(false); // 设置显示缩放按钮
        mSettings.setSupportZoom(true); // 支持缩放
        mSettings.setLoadWithOverviewMode(true);
        mSettings.setTextSize(WebSettings.TextSize.NORMAL);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.d("maomao", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            mSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            mSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            mSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            mSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            mSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            mSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        //去掉默认滚动条
        wvDetail.setHorizontalScrollBarEnabled(false);//水平不显示
        wvDetail.setVerticalScrollBarEnabled(false); //垂直不显示
        sellerService=new SellerService(this);
        getSeverData();
    }
    private void getSeverData(){
        sellerService.aboutPlatform(new CommonResultListener<AboutPlatformBean>(this) {
            @Override
            public void successHandle(AboutPlatformBean result) throws JSONException {
//                initWebView(result.introduction);
                String introduction = result.introduction;
                if (introduction == null) {
                    return;
                }
                wvDetail.loadDataWithBaseURL(null, introduction, "text/html", "utf-8", null);
            }
        });
    }
//    private void initWebView(String html) {
//        if (html == null && html.length() == 0) {
//            return;
//        }
//        String style = "";
//        if (!isHtmlContentHasStyle(html)) {
//            style = TextConstant.HTTP_STYLE;
//        }
//        String url = TextConstant.HTTP_BODY_START + html + style + TextConstant.HTTP_BODY_END;
//        wvDetail.setScrollbarFadingEnabled(true);
//        wvDetail.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        wvDetail.loadData(url, "text/html;charset=UTF-8", null);
//    }
//
//    private boolean isHtmlContentHasStyle(String content) {
//        return content.indexOf(" style=\"") != -1;
//    }

}
