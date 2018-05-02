package com.example.huangjinding.ub_seller.seller.activity;

import android.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.bean.SellerBaseInfoBean;
import com.example.huangjinding.ub_seller.seller.bean.SellerQrBean;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
import com.example.huangjinding.ub_seller.seller.util.DialogUtil;
import com.example.huangjinding.ub_seller.seller.util.Tools;

import org.json.JSONException;

import static com.example.huangjinding.ub_seller.seller.service.BaseService.BASE_IMAGE_URL;

/**
 * Created by huangjinding on 2017/7/14.
 */

public class QrSellerDialog {
    private AlertDialog dialog;
    private SellerService sellerService;
    private BaseActivity activity;
    private View mDialogView;
    private String qrIcon;

    ImageView iv_qr;
    TextView tv_seller_name;
    TextView tv_seller_address;
    public QrSellerDialog(BaseActivity activity){
        this.activity=activity;
    }

   public void showDialog(){
       mDialogView = View.inflate(activity.getApplicationContext(), R.layout.dialog_code, null);
       dialog = DialogUtil.createDialogBottom(activity, mDialogView);
       iv_qr = (ImageView) mDialogView.findViewById(R.id.iv_logo);
       tv_seller_name = (TextView) mDialogView.findViewById(R.id.tv_seller_name);
       tv_seller_address = (TextView) mDialogView.findViewById(R.id.tv_seller_address);
       initData();
   }
   private void initData(){
       sellerService=new SellerService(activity);
       sellerService.getSellerBaseInfo(new CommonResultListener<SellerBaseInfoBean>(activity) {
           @Override
           public void successHandle(SellerBaseInfoBean result) throws JSONException {
               tv_seller_name.setText(result.name);
               tv_seller_address.setText(result.address);
               getQrImage(result.shop_id);
           }
       });
   }
   private void getQrImage(String id){
      sellerService.getQrCodeSeller(id, new CommonResultListener<SellerQrBean>(activity) {
          @Override
          public void successHandle(SellerQrBean result) throws JSONException {
              qrIcon = result.qrCode;
              Tools.ImageLoaderShow(activity, BASE_IMAGE_URL + qrIcon, iv_qr);
          }
      });
   }







}
