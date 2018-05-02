package com.example.huangjinding.ub_seller.seller.bean;

import java.io.Serializable;

/**
 * Created by huangjinding on 2017/6/7.
 */

public class BalanceAddBean implements Serializable {
    public String bank_name;
    public String bank_account_name;
    public String bank_account_number;
    public String opening_bank_name;
    public int code;

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_account_name() {
        return bank_account_name;
    }

    public void setBank_account_name(String bank_account_name) {
        this.bank_account_name = bank_account_name;
    }

    public String getBank_account_number() {
        return bank_account_number;
    }

    public void setBank_account_number(String bank_account_number) {
        this.bank_account_number = bank_account_number;
    }

    public String getOpening_bank_name() {
        return opening_bank_name;
    }

    public void setOpening_bank_name(String opening_bank_name) {
        this.opening_bank_name = opening_bank_name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
