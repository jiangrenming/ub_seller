package com.example.huangjinding.ub_seller.seller.activity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.HeadView;
import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.base.IntentExtraKeyConst;
import com.example.huangjinding.ub_seller.seller.bean.SellerBaseInfoBean;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
import com.example.huangjinding.ub_seller.seller.util.MySharedPreference;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huangjinding on 2017/7/14.
 */

public class ApplyDetailAcivity extends BaseActivity {


    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_bank_code)
    TextView tvBankCode;
    @BindView(R.id.ll_bank)
    LinearLayout llBank;
    @BindView(R.id.et_balance)
    EditText etBalance;
    @BindView(R.id.tv_could_balance)
    TextView tvCouldBalance;
    @BindView(R.id.tv_get_balance_all)
    TextView tvGetBalanceAll;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.img_type)
    ImageView mPayType;
    private String type;
    private String bankId;
    private SellerService sellerService;
    private double minGetMoney;

    public ApplyDetailAcivity() {
        super();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_get_balance;
    }

    @Override
    protected void initView() {
        super.initView();
        hvHead.setTitle("提现");
        sellerService = new SellerService(this);

        String bankCode = MySharedPreference.get("BANK_CODE", "", ApplyDetailAcivity.this);
        String bankName= MySharedPreference.get("BANK_NAME", "", ApplyDetailAcivity.this);

//        String bankName = getIntentValue(IntentExtraKeyConst.BANKSELECT_NAME);
//        String bankCode = getIntentValue(IntentExtraKeyConst.BANKSELECT_CODE);
        type = MySharedPreference.get("TYPE", "", this);
        if ("1".equals(type)) {
            mPayType.setImageResource(R.mipmap.bank);
        } else if ("2".equals(type)) {
            mPayType.setImageResource(R.mipmap.zhifubao);
        } else if ("3".equals(type)) {
            mPayType.setImageResource(R.mipmap.weixin);
        } else {
            showToast("暂时无法获取Type");
        }
        bankId = getIntentValue(IntentExtraKeyConst.BANKSELECT_ID);
        String applyMoney = getIntentValue(IntentExtraKeyConst.APPLY_MONEY);
        if (applyMoney == null || applyMoney.length() == 0) {
            applyMoney = "0";
        }
        tvCouldBalance.setText("可提现" + applyMoney + "0元");
        etBalance.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tvBankName.setText(bankName);
        tvBankCode.setText(bankCode);
        etBalance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getBalanceChangeHandle();
            }
        });
    }

    private double getMinBalance() {
        sellerService.getSellerBaseInfo(new CommonResultListener<SellerBaseInfoBean>(this) {
            @Override
            public void successHandle(SellerBaseInfoBean result) throws JSONException {
                minGetMoney = result.min_amount;
                String etMoney = etBalance.getText().toString();
                if (etMoney == null || etMoney.length() == 0) {
                    showToast("输入为空");
                    return;
                }
                double money = Double.valueOf(etMoney);
                if (money < minGetMoney) {
                    showToast("低于最低提现额度");
                    return;

                }
                String id = MySharedPreference.get("ID", "", getApplicationContext());
//                sellerService.postBalance(String.valueOf(type), money, getBankId(), new CommonResultListener(ApplyDetailAcivity.this) {
                sellerService.postBalance(String.valueOf(type), money, id, new CommonResultListener(ApplyDetailAcivity.this) {
                    @Override
                    public void successHandle(Object result) throws JSONException {
                        finish();
                    }
                });
            }
        });
        return minGetMoney;
    }

    @OnClick({R.id.tv_get_balance_all, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_balance_all:
                String applyMoneyText = tvCouldBalance.getText().toString();
                String applyMoney = applyMoneyText.substring(3, applyMoneyText.length() - 1);
                etBalance.setText(applyMoney);
                break;
            case R.id.btn_confirm:
                handleGetBalance();//1银行卡2支付宝3微信
                break;
        }
    }

    private void handleGetBalance() {
        getMinBalance();
    }

    private void getBalanceChangeHandle() {
        String etMoney = etBalance.getText().toString();

    }

    private String getBankId() {
        String id;
        if ("1".equals(type)) {
            id = bankId;
        } else {
            id = "";
        }

        return id;
    }
}
