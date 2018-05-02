package com.example.huangjinding.ub_seller.seller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.activity.AddMyCardActivity;
import com.example.huangjinding.ub_seller.seller.base.IntentExtraKeyConst;
import com.example.huangjinding.ub_seller.seller.bean.BankBean;

import java.util.List;

/**
 * Created by huangjinding on 2017/6/7.
 */

public class BankAdapter extends BaseAdapter {
    Context context;
    List<BankBean> dataList;

    public BankAdapter(Context context, List<BankBean> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int index, View convertView, ViewGroup parent) {
        HolderView holderView;
        if (convertView == null) {
            holderView = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_balance, null);
            holderView.getting = (TextView) convertView.findViewById(R.id.tv_bank_name);
            holderView.rl_balance = (RelativeLayout) convertView.findViewById(R.id.rl_bank);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        final BankBean question = dataList.get(index);

        holderView.getting.setText(question.bank_name);

        holderView.rl_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddMyCardActivity.class);
                intent.putExtra(IntentExtraKeyConst.BANK_NAME, question.bank_name);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class HolderView {
        RelativeLayout rl_balance;
        TextView getting;

    }
}
