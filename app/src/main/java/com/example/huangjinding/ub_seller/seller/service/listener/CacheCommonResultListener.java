package com.example.huangjinding.ub_seller.seller.service.listener;


import com.example.huangjinding.ub_seller.seller.base.Viewable;
import com.example.huangjinding.ub_seller.seller.service.cache.ACache;

public abstract class CacheCommonResultListener<T> extends CommonResultListener<T> {

    public String cacheKey;
    public int cacheTime;

    public CacheCommonResultListener(Viewable context, int cacheTime){
        super(context);
        this.cacheTime = cacheTime;
    }

//    public void successHandle(CategoryListBean result) {
//        ACache mCache = ACache.get(context.getTargetActivity());
//        mCache.put(cacheKey, result);
//    }

    public T getCache(){
        ACache mAcache = ACache.get(context.getTargetActivity());
        return (T)mAcache.getAsObject(cacheKey);
    }

}
