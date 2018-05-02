package com.example.huangjinding.ub_seller.seller.bean;

import java.io.Serializable;

/**
 * Created by huangjinding on 2017/6/8.
 */

public class BankInfoBean implements Serializable {
    public int type;//类型：(1银行卡2支付宝3微信)
    public String account_name;
    public String tail_number;
    public String bank_name;
    public String opening_bank_name;
    public String id;//银行信息id（支付宝；微信时此字段为空）

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getTail_number() {
        return tail_number;
    }

    public void setTail_number(String tail_number) {
        this.tail_number = tail_number;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getOpening_bank_name() {
        return opening_bank_name;
    }

    public void setOpening_bank_name(String opening_bank_name) {
        this.opening_bank_name = opening_bank_name;
    }

    //获取银行或者第三方支付名称
    public String getBankOrThirdPayName(){
        if(type == 1){
            return bank_name;
        }
        if(type == 2){
            return "支付宝";
        }

        return "微信";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
