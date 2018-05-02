package com.example.huangjinding.ub_seller.seller.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.huangjinding.ub_seller.HeadView;
import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.adapter.BalanceGetAdapter;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.base.IntentExtraKeyConst;
import com.example.huangjinding.ub_seller.seller.bean.ApplyBalanceBean;
import com.example.huangjinding.ub_seller.seller.bean.ApplyBalanceInfoBean;
import com.example.huangjinding.ub_seller.seller.bean.BankInfoBean;
import com.example.huangjinding.ub_seller.seller.bean.SellerBaseInfoBean;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
import com.example.huangjinding.ub_seller.seller.util.MySharedPreference;
import com.example.huangjinding.ub_seller.seller.util.SharedKeyConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.huangjinding.ub_seller.seller.base.GlobalInfo.userToken;

/**
 * Created by huangjinding on 2017/6/2.
 */

public class ApplyActivity extends BaseActivity {

    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.ll_bank_select)
    LinearLayout llAlipay;
    @BindView(R.id.tv_could_balance)
    TextView tvCouldBalance;
    @BindView(R.id.tv_confirm_balance)
    TextView tvConfirmBalance;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.iv_select_time)
    ImageView ivSelectTime;
    @BindView(R.id.iv_bank_image)
    ImageView ivBankImage;
    @BindView(R.id.tv_aplay_alert)
    TextView tvAlplayAlert;

    @BindView(R.id.ll_select_time)
    LinearLayout llSelectTime;
    @BindView(R.id.tv_aleady_balance)
    TextView tvAleadyBalance;
    @BindView(R.id.lv_listview)
    ListView lvListview;
    private static final int SELECT_BANK = 1;
    private TimePickerView timePickerView;
    private BalanceGetAdapter adapter;
    private TimePickerView tpv;
    private String str;
    private List<ApplyBalanceBean> list;
    private SellerService sellerService;
    private String time;
    private BankInfoBean bean;
    private int type;
    private SellerBaseInfoBean sellerBaseInfoBean;
    private String bankCode;
    private String bankOrThirdName;
    private String tailNumber;
    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_aply_balance;
    }

    @Override
    protected void initView() {
        super.initView();
        hvHead.setTitle("申请提现");
        sellerService = new SellerService(this);
        list = new ArrayList<>();
        adapter = new BalanceGetAdapter(ApplyActivity.this, list);
        lvListview.setAdapter(adapter);
        Date currentDate = new Date(System.currentTimeMillis());
        tvYear.setText((currentDate.getYear() + 1900) + "年");
        tvMonth.setText((currentDate.getMonth() + 1) + "月");
        getBankStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getServerData();
    }

    private void getServerData() {
        sellerService.getBalanceList(getSelectTime(), new CommonResultListener<ApplyBalanceInfoBean>(this) {
            @Override
            public void successHandle(ApplyBalanceInfoBean result) throws JSONException {
                list.clear();
                list.addAll(result.attrList);
                tvAleadyBalance.setText(result.total + "0元");
                adapter.notifyDataSetChanged();
            }
        });

        sellerService.getSellerBaseInfo(new CommonResultListener<SellerBaseInfoBean>(this) {
            @Override
            public void successHandle(SellerBaseInfoBean result) throws JSONException {
                sellerBaseInfoBean = result;
                tvCouldBalance.setText("可提现 " + result.can_get_money + "元");
            }
        });
    }

    private void getBankStatus() {
        sellerService.getBankList(userToken, new CommonResultListener<List<BankInfoBean>>(this) {
            @Override
            public void successHandle(List<BankInfoBean> result) throws JSONException {
                bean = getLastSelectBank(result);
                if (bean == null)
                    return;
                type = bean.getType();
                if (type == 1) {
                    ivBankImage.setImageResource(R.mipmap.bank);
                } else if (type == 2) {
                    ivBankImage.setImageResource(R.mipmap.zhifubao);
                } else if (type == 3) {
                    ivBankImage.setImageResource(R.mipmap.weixin);
                } else {
                    showToast("暂时无法获取Type");
                }
                bankCode = bean.tail_number;
                bankOrThirdName = bean.getBankOrThirdPayName();
                tvAlplayAlert.setText(bankOrThirdName + "(" + bankCode + ")");
                tailNumber = bean.getTail_number();
                id = bean.getId();
                MySharedPreference.save("TYPE", String.valueOf(type), ApplyActivity.this);
                MySharedPreference.save("ID", id, ApplyActivity.this);
                MySharedPreference.save("BANK_CODE", bankCode, ApplyActivity.this);
                MySharedPreference.save("BANK_NAME", bankOrThirdName, ApplyActivity.this);
            }
        });
    }

    private BankInfoBean getLastSelectBank(List<BankInfoBean> bankList) {
        String lastSelect = MySharedPreference.get(SharedKeyConstant.LAST_APPLY_BANK, "", this);
        if (bankList == null || bankList.isEmpty()) {
            return null;
        }

        if (lastSelect == null || lastSelect.length() == 0) {
            return bankList.get(0);
        }

        Gson gson = new Gson();
        Type typeToken = new TypeToken<BankInfoBean>() {
        }.getType();
        BankInfoBean infoBean = gson.fromJson(lastSelect, typeToken);
        if (infoBean == null)
            return bankList.get(0);

        for (BankInfoBean bean : bankList) {
            if (bean.type == infoBean.type) {
                if (infoBean.id == null || infoBean.id.length() == 0) {
                    return bean;
                } else if (infoBean.id.equals(bean.id)) {
                    return bean;
                }
            }
        }

        return bankList.get(0);
    }


    private String getSelectTime() {
        String yearStr = tvYear.getText().toString();
        yearStr = yearStr.substring(0, yearStr.length() - 1);
        String monthStr = tvMonth.getText().toString();
        monthStr = monthStr.substring(0, monthStr.length() - 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }
        //    showToast(yearStr + "-" + monthStr);
        return yearStr + "-" + monthStr;
    }

    @OnClick({R.id.hv_head, R.id.ll_bank_select, R.id.tv_could_balance, R.id.tv_confirm_balance, R.id.ll_select_time, R.id.tv_aleady_balance})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.hv_head:
                break;
            case R.id.ll_bank_select:
                Intent intent = new Intent(this, MyCardActivity.class);
                intent.putExtra(IntentExtraKeyConst.CAN_SELECT, "1");
                startActivityForResult(intent, SELECT_BANK);
                break;
            case R.id.tv_could_balance:
                break;
            case R.id.tv_confirm_balance:
//                bean = new BankInfoBean();
                if (bean == null) {
                    showToast("请添加银行卡信息");
                } else {
                    Intent intent1 = new Intent(this, ApplyDetailAcivity.class);
                    intent1.putExtra(IntentExtraKeyConst.BANKSELECT_NAME, bankOrThirdName);
                    intent1.putExtra(IntentExtraKeyConst.BANKSELECT_CODE, tailNumber);
                    intent1.putExtra(IntentExtraKeyConst.BANKSELECT_TYPE, type);
                    intent1.putExtra(IntentExtraKeyConst.BANKSELECT_ID, bean.id);//银行信息id（支付宝；微信时此字段为空）
                    if (sellerBaseInfoBean != null) {
                        intent1.putExtra(IntentExtraKeyConst.APPLY_MONEY, sellerBaseInfoBean.can_get_money);
                    }
                    startActivity(intent1);
                }

                break;
            case R.id.ll_select_time:
                if (timePickerView == null) {
                    createDate();
                }
                if (!timePickerView.isShowing()) {
                    timePickerView.show();
                }
                break;
            case R.id.tv_aleady_balance:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_BANK:
                    bankSelectChange(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void bankSelectChange(Intent data) {
        String result = data.getStringExtra(IntentExtraKeyConst.JSON_DATA);
        Gson gson = new Gson();
        Type typeToken = new TypeToken<BankInfoBean>() {
        }.getType();
        bean = gson.fromJson(result, typeToken);
        int type = bean.getType();
        if (type == 1) {
            ivBankImage.setImageResource(R.mipmap.bank);
        } else if (type == 2) {
            ivBankImage.setImageResource(R.mipmap.zhifubao);
        } else if (type == 3) {
            ivBankImage.setImageResource(R.mipmap.weixin);
        } else {
            showToast("暂时无法获取Type");
        }
        bankCode = bean.tail_number;
        tvAlplayAlert.setText(bean.getBankOrThirdPayName() + "  (" + bankCode + ")");
        MySharedPreference.save("TYPE", String.valueOf(type), this);
        id = bean.getId();
        MySharedPreference.save("ID", id, ApplyActivity.this);
        MySharedPreference.save(SharedKeyConstant.LAST_APPLY_BANK, result, this);
    }

    private void createDate() {
        timePickerView = new TimePickerView(ApplyActivity.this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setCyclic(true);
        timePickerView.setTime(new java.sql.Date(System.currentTimeMillis()));

        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String year = format.format(date).substring(0, 4);
                String month = format.format(date).substring(5, 7);
                tvYear.setText(year + "年");
                tvMonth.setText(month + "月");
                getServerData();
                //  showToast(time);
            }

        });
    }
}
