package com.example.huangjinding.ub_seller.seller.bean;

import java.io.Serializable;

/**
 * Created by huangjinding on 2017/6/6.
 */

public class SellerIncomeDetailBean implements Serializable {
    public String datetime;
    public Double total_price;
    public String time;
    public String member_name;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Double total_price) {
        this.total_price = total_price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }
}
