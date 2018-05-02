package com.example.huangjinding.ub_seller.seller.bean;

import java.io.Serializable;

/**
 * Created by huangjinding on 2017/6/6.
 */

public class HotLineBean implements Serializable {
    public Number id;
    public String mobile;

    public Number getId() {
        return id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



}
