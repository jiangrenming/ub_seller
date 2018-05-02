package com.example.huangjinding.ub_seller.seller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.bean.ApplyBalanceBean;

import java.util.List;

/**
 * Created by huangjinding on 2017/6/6.
 */

public class BalanceGetAdapter extends BaseAdapter {
    Context context;
    List<ApplyBalanceBean> dataList;

   public BalanceGetAdapter(Context context, List<ApplyBalanceBean> dataList) {
       this.context = context;
       this.dataList = dataList;
   }
    @Override
    public int getCount() {
        if(dataList != null){
            return dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(dataList != null){
            return dataList.size();
        }
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_get_balabce, null);

            holderView.getting = (TextView) convertView.findViewById(R.id.tv_get_balance);
            holderView.timeinfo = (TextView) convertView.findViewById(R.id.tv_time_info);
            holderView.money = (TextView) convertView.findViewById(R.id.tv_money_count);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        final ApplyBalanceBean question = dataList.get(index);

        holderView.getting.setText(question.status);
        holderView.timeinfo.setText(question.applicationTime);
        holderView.money.setText("+"+question.amount+"0元");

        return convertView;
    }
    class HolderView {

        TextView getting;
        TextView timeinfo;
        TextView money;
    }
}
