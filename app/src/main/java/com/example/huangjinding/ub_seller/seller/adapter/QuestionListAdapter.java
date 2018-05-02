package com.example.huangjinding.ub_seller.seller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.bean.QuestionBean;

import java.util.List;

/**
 * Created by huangjinding on 2017/5/16.
 */

public class QuestionListAdapter extends BaseAdapter {
    Context context;
    List<QuestionBean> dataList;

    public QuestionListAdapter(Context context, List<QuestionBean> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
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

    @SuppressWarnings({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int index, View convertView, ViewGroup parent) {
        HolderView holderView;
        if (convertView == null) {
            holderView = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.ub_item_online_quetion, null);
            holderView.questionItem = (RelativeLayout) convertView.findViewById(R.id.rl_question);
            holderView.id = (TextView) convertView.findViewById(R.id.tv_question_id);
            holderView.question = (TextView) convertView.findViewById(R.id.tv_question_name);
            holderView.answer = (TextView) convertView.findViewById(R.id.tv_question);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        final QuestionBean question = dataList.get(index);
        holderView.id.setText(String.valueOf(question.getId()));
        holderView.question.setText(question.getQuestion());
        holderView.answer.setText(question.getAnswer());
        // Tools.ImageLoaderShow(context, seller1.getIcon1(), holderView.ll_consume_list);
//        holderView.questionItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, KefuActivity.class);
//                intent.putExtra(IntentExtraKeyConst.QUESTION_ID, question.getId());
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }

    class HolderView {
        RelativeLayout questionItem;
        TextView id;
        TextView question;
        TextView answer;
    }
}
