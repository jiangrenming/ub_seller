package com.example.huangjinding.ub_seller.seller.bean;

import java.io.Serializable;

/**
 * Created by huangjinding on 2017/6/7.
 */

public class GetBalanceListBean implements Serializable {
    public Double amount;
    public String application_time;
    public String status;
    public Double total_money;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getApplication_time() {
        return application_time;
    }

    public void setApplication_time(String application_time) {
        this.application_time = application_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotal_money() {
        return total_money;
    }

    public void setTotal_money(Double total_money) {
        this.total_money = total_money;
    }
}
