package com.example.huangjinding.ub_seller.seller.util;

/**
 * Created by huangjinding on 2017/6/2.
 */

public interface PullRefreshable {
    void refreshEnd(boolean hasNext, boolean loadSuccess);
}
