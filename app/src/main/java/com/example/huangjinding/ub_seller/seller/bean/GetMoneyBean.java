package com.example.huangjinding.ub_seller.seller.bean;

import java.io.Serializable;

/**
 * Created by huangjinding on 2017/6/6.
 */

public class GetMoneyBean implements Serializable {
    public String today_income;
    public String all_income;
    public String month_income;

    public String getToday_income() {
        return today_income;
    }

    public void setToday_income(String today_income) {
        this.today_income = today_income;
    }

    public String getAll_income() {
        return all_income;
    }

    public void setAll_income(String all_income) {
        this.all_income = all_income;
    }

    public String getMonth_income() {
        return month_income;
    }

    public void setMonth_income(String month_income) {
        this.month_income = month_income;
    }
}
