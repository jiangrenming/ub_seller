package com.example.huangjinding.ub_seller.seller.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.huangjinding.ub_seller.HeadView;
import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.adapter.QuestionListAdapter;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.bean.HotLineBean;
import com.example.huangjinding.ub_seller.seller.bean.QuestionBean;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
import com.hykj.dialoglib.MyDialog;
import com.hykj.dialoglib.MyDialogOnClick;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangjinding on 2017/6/6.
 */

public class KefuActivity extends BaseActivity {

    @BindView(R.id.hv_title)
    HeadView hvTitle;
    @BindView(R.id.lv_question_list)
    ListView lvQuestionList;
    @BindView(R.id.btn_city_more)
    Button btnCityMore;
    private String mobile;
    private SellerService sellerService;
    private QuestionListAdapter questionListAdapter;
    private List<QuestionBean> dataList;

    public KefuActivity() {
    }
    @Override
    protected int getLayoutId() {

        return R.layout.ub_activity_lianxikefu;
    }

    protected void initView() {
        super.initView();
        hvTitle.setTitle("在线客服");
        sellerService=new SellerService(this);
        dataList=new ArrayList<>();
        questionListAdapter=new QuestionListAdapter(KefuActivity.this,dataList);
        lvQuestionList.setAdapter(questionListAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        getSeverData();
    }

    private void getSeverData() {
        sellerService.getQuestion(new CommonResultListener<List<QuestionBean>>(this) {
            @Override
            public void successHandle(List<QuestionBean> result) throws JSONException {
                dataList.addAll(result);
                questionListAdapter.notifyDataSetChanged();
            }
        });


        sellerService.linePlatform(new CommonResultListener<HotLineBean>(this) {
            @Override
            public void successHandle(HotLineBean result) throws JSONException {
                mobile=result.mobile;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_city_more)
    public void onViewClicked() {
        call();
    }
    private void call(){
        new MyDialog(this, MyDialog.DIALOG_TWOOPTION, "温馨提示", "确定拨打" + mobile+"吗?", new MyDialogOnClick() {
            @Override
            public void sureOnClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mobile));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void cancleOnClick(View v) {

            }
        }).show();
    }

}
