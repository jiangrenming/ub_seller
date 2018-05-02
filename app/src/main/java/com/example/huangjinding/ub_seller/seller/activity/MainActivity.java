package com.example.huangjinding.ub_seller.seller.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huangjinding.ub_seller.HeadView;
import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.adapter.AplayGetAdapter;
import com.example.huangjinding.ub_seller.seller.base.IntentExtraKeyConst;
import com.example.huangjinding.ub_seller.seller.bean.GetMoneyBean;
import com.example.huangjinding.ub_seller.seller.bean.SellerBaseInfoBean;
import com.example.huangjinding.ub_seller.seller.bean.SellerIncomeDetailBean;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
import com.example.huangjinding.ub_seller.seller.util.Tools;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.huangjinding.ub_seller.seller.service.BaseService.BASE_IMAGE_URL;

public class MainActivity extends TabBaseActivity {

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
    @BindView(R.id.tv_today_balance)
    TextView tvTodayBalance;
    @BindView(R.id.tv_today_get)
    TextView tvTodayGet;

    @BindView(R.id.ll_today)
    LinearLayout llToday;
    @BindView(R.id.tv_aleady_get)
    TextView tvAleadyGet;
    @BindView(R.id.ll_already_get)
    LinearLayout llAlreadyGet;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.listview)
    ListView listview;

    private String totalMoney;
    private boolean status = false;
    private String date;
    private String monthInfo = null;
    private SellerService sellerService;
    private List<SellerIncomeDetailBean> dataList;
    private SellerBaseInfoBean sellerBaseInfoBean;
    private AplayGetAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        hvHead.setTitle("首页");
        hvHead.setBackImageVisible(View.GONE);
        sellerService = new SellerService(this);
        dataList = new ArrayList<>();
        adapter = new AplayGetAdapter(MainActivity.this, dataList);
        listview.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        Date dateNow = new Date();
        Calendar c = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        monthInfo = dateFormat.format(dateNow);
        date = c.get(Calendar.DATE) + "";

        sellerService.getSellerBaseInfo(new CommonResultListener<SellerBaseInfoBean>(this) {
            @Override
            public void successHandle(SellerBaseInfoBean result) throws JSONException {
                handlerMessage(result);
            }
        });

        sellerService.getMoney(monthInfo, new CommonResultListener<GetMoneyBean>(this) {
            @Override
            public void successHandle(GetMoneyBean result) throws JSONException {
                tvTodayBalance.setText("￥ " + result.getToday_income() + "元");
                totalMoney = "￥ " + result.getAll_income() + "元";
                tvAleadyGet.setText(totalMoney);

            }
        });

        //今日收益明细
        sellerService.getDetail("today", "", new CommonResultListener<List<SellerIncomeDetailBean>>(this) {
            @Override
            public void successHandle(List<SellerIncomeDetailBean> result) throws JSONException {
                dataList.clear();
                dataList.addAll(result);
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void handlerMessage(SellerBaseInfoBean result) {
        tvSellerName.setText(((result.name).length()) > 7 ? (result.name.substring(0, 6) + "...") : result.name);
        tvGetCount.setText("可提现  " + result.can_get_money );
        tvWatting.setText("可到账  " + result.wait_get_money + ".00");
        tvBalance.setText("审核中  " + result.checking_money + ".00");
        Tools.ImageLoaderShow(MainActivity.this, BASE_IMAGE_URL + result.icon, ivLogo);

    }

    @OnClick({R.id.iv_logo, R.id.iv_code, R.id.tv_more, R.id.tv_today_get, R.id.tv_aplay_balance})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_logo:
                startActivity(BaseInfoActivity.class);
                break;
            case R.id.iv_code:
                show();
                break;
            case R.id.tv_today_get:
                // selectTab();
                break;
            case R.id.tv_aplay_balance:
                startActivity(ApplyActivity.class);
                break;
            case R.id.tv_more:
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(IntentExtraKeyConst.TOTAL_MONEY, totalMoney);
                startActivity(intent);
                break;
        }
    }

    private void show() {
        QrSellerDialog dialog = new QrSellerDialog(this);
        dialog.showDialog();
    }

    private void selectTab() {
        if (!status) {
            tvTodayGet.setTextColor(Tools.getColorByResId(this, R.color.text_red));
            listview.setVisibility(View.VISIBLE);
            status = true;
        } else {
            tvTodayGet.setTextColor(Tools.getColorByResId(this, R.color.text_gray));
            listview.setVisibility(View.GONE);
            status = false;
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
    //
//    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {


        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
