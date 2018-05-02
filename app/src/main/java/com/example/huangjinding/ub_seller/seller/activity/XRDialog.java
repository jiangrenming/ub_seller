package com.example.huangjinding.ub_seller.seller.activity;

import android.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.bean.SellerBaseInfoBean;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
import com.example.huangjinding.ub_seller.seller.util.DialogUtil;
import com.example.huangjinding.ub_seller.seller.util.Tools;

import org.json.JSONException;

import butterknife.BindView;

import static com.example.huangjinding.ub_seller.seller.service.BaseService.BASE_IMAGE_URL;

/**
 * Created by huangjinding on 2017/6/8.
 */

public class XRDialog {
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_seller_name)
    TextView tvSellerName;
    @BindView(R.id.tv_seller_address)
    TextView tvSellerAddress;
    private MainActivity activity;
    private SellerService sellerService;
    private SellerBaseInfoBean sellerBaseInfoBean;
    private AlertDialog dialog;

    public XRDialog(SellerBaseInfoBean sellerBaseInfoBean,
                    SellerService sellerService) {
        this.sellerBaseInfoBean = sellerBaseInfoBean;
        this.sellerService = sellerService;
    }

    public void showDialog() {
        View view = View.inflate(activity.getApplicationContext(), R.layout.dialog_code, null);
        dialog = DialogUtil.createDialogBottom(activity, view);

        sellerService.getSellerBaseInfo(new CommonResultListener<SellerBaseInfoBean>(activity) {
            @Override
            public void successHandle(SellerBaseInfoBean result) throws JSONException {
                handlerMessage(result);
            }
        });

    }

    private void handlerMessage(SellerBaseInfoBean result) {
        tvSellerName.setText(((result.name).length()) > 5 ? (result.name.substring(0, 4) + "...") : result.name);
        tvSellerAddress.setText(((result.address).length()) > 5 ? (result.address.substring(0, 4) + "...") : result.address);
        Tools.ImageLoaderShow(activity, BASE_IMAGE_URL + result.icon, ivLogo);

    }

}
