package com.example.huangjinding.ub_seller.seller.activity;

import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.huangjinding.ub_seller.HeadView;
import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.adapter.BankAdapter;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.bean.BankBean;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by huangjinding on 2017/6/7.
 */

public class BankActivity extends BaseActivity {
    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.listview)
    ListView listview;


    private SellerService sellerService;
    private List<BankBean> dataList;
    private BaseAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank;
    }
    @Override
    protected void initView(){
        super.initView();
        hvHead.setTitle("银行卡");

        sellerService=new SellerService(this);
        dataList=new ArrayList<>();
        adapter=new BankAdapter(BankActivity.this,dataList);
        listview.setAdapter(adapter);
        getData();
    }
    private void getData(){
        sellerService.getBankList(new CommonResultListener<List<BankBean>>(this) {
            @Override
            public void successHandle(List<BankBean> result) throws JSONException {
                dataList.addAll(result);
                adapter.notifyDataSetChanged();

            }
        });
    }


}
