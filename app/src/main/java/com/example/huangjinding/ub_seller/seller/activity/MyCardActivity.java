package com.example.huangjinding.ub_seller.seller.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.adapter.BankListAdapter;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.base.GlobalInfo;
import com.example.huangjinding.ub_seller.seller.base.IntentExtraKeyConst;
import com.example.huangjinding.ub_seller.seller.bean.BankInfoBean;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huangjinding on 2017/6/2.
 */

public class MyCardActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.listview)
    ListView listview;

    private SellerService sellerService;
    private BankListAdapter adapter;
    List<BankInfoBean> dataList;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_balanca;
    }

    @Override
    protected void initView(){
        super.initView();
        sellerService=new SellerService(this);
        dataList=new ArrayList<>();
        adapter=new BankListAdapter(MyCardActivity.this,dataList);
        adapter.setOnAdapterItemDeleteOrEdit(new MyListener());
        listview.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        getBankInfo();
    }

    @Override
    protected void initData(){
        super.initData();

    }
    private void getBankInfo(){
        String userToken = GlobalInfo.userToken;
        if (TextUtils.isEmpty(userToken)) {
            userToken = "\"\"";
        }
       sellerService.getBankList(userToken, new CommonResultListener<List<BankInfoBean>>(this) {
           @Override
           public void successHandle(List<BankInfoBean> result) throws JSONException {
               dataList.clear();
               dataList.addAll(result);
               adapter.notifyDataSetChanged();
               addItemSelectedHandleIfNeed();
           }
       });
    }
    private void addItemSelectedHandleIfNeed(){
        String canSelect=getIntentValue(IntentExtraKeyConst.CAN_SELECT);
        if("1".equals(canSelect)){
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BankInfoBean bean=dataList.get(position);
                    Intent intent=new Intent();
                    Gson gson=new Gson();
                    intent.putExtra(IntentExtraKeyConst.JSON_DATA,gson.toJson(bean));
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add:
                startActivity(AddMyCardActivity.class);
                break;
        }
    }
    private class MyListener implements BankListAdapter.OnAdapterItemDeleteOrEdit{
        @Override
        public void delete(int position){
            deleteBank(position);
        }
    }

    private void deleteBank(final int position){
        BankInfoBean bean=dataList.get(position);
        if(bean!=null){
            String id=bean.id;
            String token= GlobalInfo.userToken;
            if(bean.type==1){
                sellerService.getDeliteBankList(id, token, new CommonResultListener(this) {
                    @Override
                    public void successHandle(Object result) throws JSONException {
                        showToast("删除成功");
                        dataList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }else if(bean.type==2){
                 sellerService.deleteAlipay(token, new CommonResultListener(this) {
                     @Override
                     public void successHandle(Object result) throws JSONException {
                         showToast("删除成功");
                         dataList.remove(position);
                         adapter.notifyDataSetChanged();
                     }
                 });
            }else {
                sellerService.deleteWechat(token, new CommonResultListener(this) {
                    @Override
                    public void successHandle(Object result) throws JSONException {
                        showToast("删除成功");
                        dataList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

        }
    }

}
