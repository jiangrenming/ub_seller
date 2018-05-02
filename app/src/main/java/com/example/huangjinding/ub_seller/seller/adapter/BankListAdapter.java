package com.example.huangjinding.ub_seller.seller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.bean.BankInfoBean;

import java.util.List;

/**
 * Created by huangjinding on 2017/6/6.
 */

public class BankListAdapter extends BaseAdapter {
    Context context;
    List<BankInfoBean> dataList;
    private OnAdapterItemDeleteOrEdit onAdapterItemDeleteOrEdit;
    public BankListAdapter(Context context, List<BankInfoBean> dataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bank_info, null);

            holderView.tvBankName = (TextView) convertView.findViewById(R.id.tv_bank_name);
            holderView.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
            holderView.tvBankCode = (TextView) convertView.findViewById(R.id.tv_bank_code);
            holderView.ivBankImage = (ImageView) convertView.findViewById(R.id.iv_bank_image);
            holderView.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        final BankInfoBean question = dataList.get(index);
        if("1".equals(question.type+"")){
            holderView.tvBankName.setText(question.bank_name);
            //      holderView.ivBankImage.setImageBitmap(); 银行卡
            holderView.ivBankImage.setImageResource(R.mipmap.bank);
        }else if("2".equals(question.type+"")){
            holderView.tvBankName.setText("支付宝");
            //     holderView.ivBankImage.setImageBitmap(); 支付宝
            holderView.ivBankImage.setImageResource(R.mipmap.zhi);
        }else {
            holderView.tvBankName.setText("微信");
            //    holderView.ivBankImage.setImageBitmap(); 微信
            holderView.ivBankImage.setImageResource(R.mipmap.wechat);
        }
        holderView.tvUserName.setText(question.account_name);
        String bankCode = question.tail_number;
        holderView.tvBankCode.setText(bankCode);

        holderView.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAdapterItemDeleteOrEdit!=null){
                    onAdapterItemDeleteOrEdit.delete(index);
                }
            }
        });
        return convertView;
    }

    class HolderView {

        ImageView ivBankImage;

        TextView tvBankName;

        TextView tvUserName;

        TextView tvBankCode;

        ImageView ivDelete;

    }
    public interface OnAdapterItemDeleteOrEdit{
        void delete(int position);
    }
    public void setOnAdapterItemDeleteOrEdit(OnAdapterItemDeleteOrEdit onAdapterItemDeleteOrEdit){
        this.onAdapterItemDeleteOrEdit=onAdapterItemDeleteOrEdit;
    }
}
