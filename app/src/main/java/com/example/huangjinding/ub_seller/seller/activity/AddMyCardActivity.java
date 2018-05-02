package com.example.huangjinding.ub_seller.seller.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.HeadView;
import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.base.GlobalInfo;
import com.example.huangjinding.ub_seller.seller.base.IntentExtraKeyConst;
import com.example.huangjinding.ub_seller.seller.bean.BalanceAddBean;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
import com.example.huangjinding.ub_seller.seller.util.NoFastClickUtils;
import com.example.huangjinding.ub_seller.seller.util.Tools;
import com.example.huangjinding.ub_seller.seller.util.ValidateUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.huangjinding.ub_seller.R.id.tv_code;
import static com.example.huangjinding.ub_seller.seller.util.ValidateUtil.checkBankCard;

/**
 * Created by huangjinding on 2017/6/7.
 */

public class AddMyCardActivity extends BaseActivity {


    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.spinner_select)
    Spinner spinnerSelect;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.ll_bank_select)
    LinearLayout llBankSelect;
    @BindView(R.id.et_bank_code)
    EditText etBankCode;
    @BindView(R.id.et_bank_username)
    EditText etBankUsername;
    @BindView(R.id.et_bank_open_name)
    EditText etBankOpenName;
    @BindView(R.id.ll_bank)
    LinearLayout llBank;
    @BindView(R.id.et_style_id)
    EditText etStyleId;
    @BindView(R.id.et_style_name)
    EditText etStyleName;
    @BindView(R.id.ll_other_style)
    LinearLayout llOtherStyle;
    //    @BindView(R.id.tv_phone)
//    TextView tvPhone;
    @BindView(R.id.et_telephone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(tv_code)
    TextView tvCode;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private String bank_name;
    private String bank_account_name;
    private String bank_account_unmber;
    private String opening_bank_name;
    private SellerService sellerService;
    private String selectType;
    private List<String> data;
    private int sType;
    private String mobile;
    private int remainSecond = 60;
    private Timer countDownTimer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_get_two;
    }

    @Override
    protected void initView() {
        super.initView();
        hvHead.setTitle("我的提现账户");
        sellerService = new SellerService(this);
//        mobile = etPhone.getText().toString();
//        tvPhone.setText(mobile);
        getSpinnerData();
        selectType();
    }

    private void selectType() {
        String bankName = getIntentValue(IntentExtraKeyConst.BANK_NAME);
        tvBankName.setText(bankName);
        tvBankName.setTextColor(Tools.getColorByResId(AddMyCardActivity.this, R.color.text_black));
    }

    private void getSpinnerData() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_select);
        data = new ArrayList<>();
        data.add("银行卡");
//        data.add("微信");
        data.add("支付宝");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectType = data.get(position);
//                 etCode.setText(selectType);
                if ("银行卡".equals(selectType)) {
                    llOtherStyle.setVisibility(View.GONE);
                    llBank.setVisibility(View.VISIBLE);
                    sType = 1;
                } else {
                    llBank.setVisibility(View.GONE);
                    llOtherStyle.setVisibility(View.VISIBLE);
                    sType = 2;
//                    if ("微信".equals(selectType)) {
//                        sType = 2;
//                    } else {
//                        sType = 3;
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void timeSchedule() {
        countDownTimer = new Timer();
        countDownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendMessage(new Message());
            }
        }, 0, 1000);
        tvCode.setEnabled(false);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            tvCode.setText(remainSecond + "S");
            remainSecond--;
            if (remainSecond <= 0) {
                tvCode.setEnabled(true);
                tvCode.setText("获取验证码");
                countDownTimer.cancel();// 取消
                remainSecond = 60;
            }
        }
    };

    private void getAuthCode() {
//        if (bank_name.length() == 0 && "".equals(bank_name)
//                && bank_account_name.length() == 0 && "".equals(bank_account_name)
//                && bank_account_unmber.length() == 0 && "".equals(bank_account_unmber)
//                && opening_bank_name.length() == 0 && "".equals(opening_bank_name)
//                ) {
//            showToast("请输入完整信息");
//        } else if (bank_name.length() == 0 || "".equals(bank_name)) {
//            showToast("请选择银行名称");
//        } else if (bank_account_unmber.length() == 0 || "".equals(bank_account_unmber)) {
//            showToast("请输入的银行卡号");
//        } else if (bank_account_name.length() == 0 || "".equals(bank_account_name)) {
//            showToast("请输入开户名");
//        } else if ("".equals(opening_bank_name) || opening_bank_name.length() == 0) {
//            showToast("请输入开户行");
//        } else
        if ("".equals(etPhone.getText().toString()) || etPhone.getText().toString().length() == 0) {
            showToast("请输入手机号码");
        } else {
            mobile = etPhone.getText().toString();
        Log.i("mobile",mobile);
            sellerService.getAuthCode(mobile, new CommonResultListener<String>(this) {
                @Override
                public void successHandle(String result) throws JSONException {
                    //showToast(result);
                    timeSchedule();
                }
            });
        }
    }

    private void postHandle() {
        String userToken = GlobalInfo.userToken;
        if (TextUtils.isEmpty(userToken)) {
            userToken = "\"\"";
        }
        String bank_name = tvBankName.getText().toString().trim();
        String bank_account_name = etBankUsername.getText().toString().trim();
        String bank_account_unmber = etBankCode.getText().toString().trim();
        String opening_bank_name = etBankOpenName.getText().toString().trim();
        String otherId = etStyleId.getText().toString().trim();
        String otherName = etStyleName.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        boolean isIDCard = checkBankCard(bank_account_unmber);
        if (NoFastClickUtils.isFastClick()) {
            showToast("休息一会吧。。。");
        } else {
            //添加银行卡
            if (sType == 1) {
                if (bank_name.length() == 0 && "".equals(bank_name)
                        && bank_account_name.length() == 0 && "".equals(bank_account_name)
                        && bank_account_unmber.length() == 0 && "".equals(bank_account_unmber)
                        && opening_bank_name.length() == 0 && "".equals(opening_bank_name)
                        && otherName.length() == 0 && "".equals(otherName)
                        && code.length() == 0 && "".equals(code)) {
                    showToast("请输入完整信息");
                } else if (bank_name.length() == 0 || "".equals(bank_name)) {
                    showToast("请选择银行名称");
                }
                else if (bank_account_name.length() == 0 || "".equals(bank_account_name)) {
                    showToast("请输入开户名");
                } else if ( opening_bank_name.length() == 0|| "".equals(opening_bank_name)) {
                    showToast("请输入开户行");
                } else if ("".equals(etPhone.getText().toString()) || etPhone.getText().toString().length() == 0) {
                    showToast("请输入手机号码");
                } else if (code.length() == 0 || "".equals(code)) {
                    showToast("请输入短信验证码");
                }else {
                if (!isIDCard) {
                    showToast("请输入正确的银行卡号");
                }else {
                    mobile = etPhone.getText().toString();
                    sellerService.postAddBankInfo(mobile, userToken, bank_name, bank_account_name,
                            bank_account_unmber, opening_bank_name, code, new CommonResultListener<List<BalanceAddBean>>(this) {
                                @Override
                                public void successHandle(List<BalanceAddBean> result) throws JSONException {
                                    showToast("添加成功");
                                    finish();
                                }
                            });
                }
                }
            } else {

                if ("".equals(etPhone.getText().toString()) || etPhone.getText().toString().length() == 0) {
                    showToast("请输入手机号码");
                } else if (code.length() == 0 || "".equals(code)) {
                    showToast("请输入短信验证码");
                }else if(otherId.length() == 0){
                    showToast("请输入账户ID");
                }else if(otherName.length() == 0) {
                    showToast("请输入真实姓名");
                }else{
                    mobile = etPhone.getText().toString();
                sellerService.postAddAlipayInfo(mobile,userToken, otherName, otherId, code, new CommonResultListener(this) {
                    @Override
                    public void successHandle(Object result) throws JSONException {
                        showToast("添加成功");
//                        startActivity(MyCardActivity.class);
                        finish();
                    }
                });
                }
            }

//
        }

    }

    @OnClick({R.id.ll_bank_select, tv_code, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_bank_select:
                startActivity(BankActivity.class);
                break;
            case tv_code:
                getAuthCode();
                break;
            case R.id.btn_confirm:
                postHandle();
                break;
        }
    }
}
