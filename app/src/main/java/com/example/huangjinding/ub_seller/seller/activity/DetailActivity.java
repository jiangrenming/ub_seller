package com.example.huangjinding.ub_seller.seller.activity;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.huangjinding.ub_seller.HeadView;
import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.adapter.AplayGetAdapter;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.base.IntentExtraKeyConst;
import com.example.huangjinding.ub_seller.seller.bean.SellerIncomeDetailBean;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huangjinding on 2017/6/2.
 */

public class DetailActivity extends BaseActivity {
    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.ll_select)
    LinearLayout llSelect;
    @BindView(R.id.tv_already_get)
    TextView tvAlreadyGet;
    @BindView(R.id.listview)
    ListView listview;

    private AplayGetAdapter adapter;
    private TimePickerView tpv;
    private String str;
    private List<SellerIncomeDetailBean> list;
    private SellerService sellerService;
    private TimePickerView timePickerView;
    private boolean isSelectTime = false;
    private String selectTime;

    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        hvHead.setTitle("收益明细");
        sellerService = new SellerService(this);
        list = new ArrayList<>();
        adapter = new AplayGetAdapter(DetailActivity.this, list);
        listview.setAdapter(adapter);
        Date currentDate = new Date(System.currentTimeMillis());
        tvYear.setText((currentDate.getYear() + 1900) + "年");
        tvMonth.setText((currentDate.getMonth() + 1) + "月");
        getServerData();
        getInitMoney();
    }

    private void getInitMoney() {
        String total = getIntentValue(IntentExtraKeyConst.TOTAL_MONEY);
        String money = total.substring(1, total.length());
        tvAlreadyGet.setText(money);
    }

    private void getServerData() {
        sellerService.getDetail("month", getSelectTime(), new CommonResultListener<List<SellerIncomeDetailBean>>(this) {
            @Override
            public void successHandle(List<SellerIncomeDetailBean> result) throws JSONException {
                list.clear();
                if (!isSelectTime) {
                    list.addAll(result);
                } else {
                    List<SellerIncomeDetailBean> mList = new ArrayList<SellerIncomeDetailBean>();
                    for (int i = 0; i < result.size(); i++) {
                        String nowSelectTime = result.get(i).getDatetime().substring(0, 10);
                        Log.e("MONTH", nowSelectTime);
                        if (selectTime.equals(nowSelectTime)) {
                            SellerIncomeDetailBean mBean = result.get(i);
                            mBean.setDatetime(mBean.getDatetime());
                            mBean.setMember_name(mBean.getMember_name());
                            mBean.setTotal_price(mBean.getTotal_price());
                            mList.add(mBean);
                        }else {
                            result.remove(i);
                        }
                    }
                    AplayGetAdapter mAdapter1 =new AplayGetAdapter(DetailActivity.this,mList);
                    listview.setAdapter(mAdapter1);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private String getSelectTime() {
        String yearStr = tvYear.getText().toString();
        yearStr = yearStr.substring(0, yearStr.length() - 1);
        String monthStr = tvMonth.getText().toString();
        monthStr = monthStr.substring(0, monthStr.length() - 1);
        //   showToast(yearStr + "-" + monthStr);
        if (monthStr.length() < 2) {
            monthStr = "0" + monthStr;
        }
        return yearStr + "-" + monthStr;
    }

    @OnClick(R.id.ll_select)
    public void onViewClicked() {
        if (timePickerView == null) {
            createDate();
        }
        if (!timePickerView.isShowing()) {
            timePickerView.show();
        }
    }

    private void createDate() {
        timePickerView = new TimePickerView(DetailActivity.this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setCyclic(true);
        timePickerView.setTime(new java.sql.Date(System.currentTimeMillis()));

        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String year = format.format(date).substring(0, 4);
                String month = format.format(date).substring(5, 7);
                String today = format.format(date).substring(8, 10);
                tvYear.setText(year + "年");
                tvMonth.setText(month + "月");
                selectTime = year + "-" + month + "-" + today;
                isSelectTime = true;
//                String lastTime=year+ "-" + month;
//               showToast(lastTime);
                getServerData();
                //  showToast(time);
            }

        });
    }

}