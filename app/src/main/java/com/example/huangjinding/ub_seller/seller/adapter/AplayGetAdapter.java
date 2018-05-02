package com.example.huangjinding.ub_seller.seller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.bean.SellerIncomeDetailBean;

import java.util.List;

/**
 * Created by huangjinding on 2017/6/6.
 */

public class AplayGetAdapter extends BaseAdapter {
    Context context;
    List<SellerIncomeDetailBean> dataList;

   public AplayGetAdapter(Context context, List<SellerIncomeDetailBean> dataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_get_balabce, null);
            holderView.getting = (TextView) convertView.findViewById(R.id.tv_get_balance);
            holderView.timeinfo = (TextView) convertView.findViewById(R.id.tv_time_info);
            holderView.money = (TextView) convertView.findViewById(R.id.tv_money_count);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        final SellerIncomeDetailBean question = dataList.get(index);

        holderView.getting.setText(question.member_name);
        holderView.timeinfo.setText(question.datetime);
        holderView.money.setText("+"+question.total_price+"元");

//        holderView.getItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,KefuActivity.class);
//                intent.putExtra(IntentExtraKeyConst.QUESTION_ID, question.getId());
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }
    class HolderView {

        TextView getting;
        TextView timeinfo;
        TextView money;
    }
}
