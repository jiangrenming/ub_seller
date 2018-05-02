package com.example.huangjinding.ub_seller.seller.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.HeadView;
import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.LoginActivity;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.base.LoginAction;
import com.example.huangjinding.ub_seller.seller.bean.SellerBaseInfoBean;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
import com.example.huangjinding.ub_seller.seller.util.Tools;
import com.hykj.dialoglib.MyDialog;
import com.hykj.dialoglib.MyDialogOnClick;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.huangjinding.ub_seller.seller.service.BaseService.BASE_IMAGE_URL;

/**
 * Created by huangjinding on 2017/6/2.
 */

public class BaseInfoActivity extends BaseActivity {
    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_seller_name)
    TextView tvSellerName;
    @BindView(R.id.tv_get_count)
    TextView tvGetCount;
    @BindView(R.id.tv_wating)
    TextView tvWatting;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.ll_seller_info)
    LinearLayout llSellerInfo;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.tv_aplay_balance)
    TextView tvAplayBalance;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.tv_bili)
    TextView tvBili;
    @BindView(R.id.ll_my_banlance)
    LinearLayout llMyBanlance;
    @BindView(R.id.ll_change_pwd)
    LinearLayout llChangePwd;
    @BindView(R.id.ll_about_plat)
    LinearLayout llAboutPlat;
//    @BindView(R.id.ll_question)
//    LinearLayout llQuestion;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.btn_cacel_login)
    Button btnCacelLogin;


    private SellerService sellerService;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_info;
    }
    @Override
    protected void initView(){
        super.initView();
        sellerService=new SellerService(this);
        hvHead.setTitle("基础资料");
    }

    @Override
    protected void onResume() {
        super.onResume();

        getData();
    }

    private void getData(){

        sellerService.getSellerBaseInfo(new CommonResultListener<SellerBaseInfoBean>(this) {
            @Override
            public void successHandle(SellerBaseInfoBean result) throws JSONException {
                handlerMessage(result);
            }
        });
    }
    private void handlerMessage(SellerBaseInfoBean result){
        tvSellerName.setText(((result.name).length())>7?(result.name.substring(0,6)+"..."):result.name);
        tvGetCount.setText("可提现  "+result.can_get_money);
        tvWatting.setText("可到账  "+result.wait_get_money+".00");
        tvBalance.setText("审核中  "+result.checking_money+".00");
        tvAddress.setText("店铺地址  : "+result.address);
        tvPay.setText("结算折扣  : "+result.discount);
        tvBili.setText("U币比例  : "+result.ratio);
        Tools.ImageLoaderShow(BaseInfoActivity.this, BASE_IMAGE_URL+result.icon, ivLogo);

    }

    @OnClick({R.id.iv_logo,R.id.iv_code, R.id.tv_aplay_balance, R.id.ll_my_banlance, R.id.ll_change_pwd, R.id.ll_about_plat,R.id.ll_phone, R.id.btn_cacel_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_logo:

                break;
            case R.id.iv_code:
                show();
                break;
            case R.id.tv_aplay_balance:
                startActivity(ApplyActivity.class);
                break;
            case R.id.ll_my_banlance:
                startActivity(MyCardActivity.class);
                break;
            case R.id.ll_change_pwd:
                startActivity(FindPwdActivity.class);
                break;
            case R.id.ll_about_plat:
                startActivity(AboutPlatformActiivty.class);
                break;

            case R.id.ll_phone:
                startActivity(KefuActivity.class);
                break;
            case R.id.btn_cacel_login:
                new MyDialog(this, MyDialog.DIALOG_TWOOPTION, "温馨提示", "您确定要退出登录吗?", new MyDialogOnClick() {
                    @Override
                    public void sureOnClick(View v) {
                        LoginAction.logout(BaseInfoActivity.this);
                        Intent intent = new Intent(BaseInfoActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void cancleOnClick(View v) {
                    }
                }).show();
                break;
        }
    }
    private void show(){
        QrSellerDialog dialog=new QrSellerDialog(this);
        dialog.showDialog();
    }
}
