package com.example.huangjinding.ub_seller.seller.service;

import android.text.TextUtils;
import android.util.Log;

import com.example.huangjinding.ub_seller.seller.base.GlobalInfo;
import com.example.huangjinding.ub_seller.seller.base.Viewable;
import com.example.huangjinding.ub_seller.seller.bean.AboutPlatformBean;
import com.example.huangjinding.ub_seller.seller.bean.ApplyBalanceInfoBean;
import com.example.huangjinding.ub_seller.seller.bean.BankBean;
import com.example.huangjinding.ub_seller.seller.bean.BankInfoBean;
import com.example.huangjinding.ub_seller.seller.bean.GetMoneyBean;
import com.example.huangjinding.ub_seller.seller.bean.HotLineBean;
import com.example.huangjinding.ub_seller.seller.bean.QuestionBean;
import com.example.huangjinding.ub_seller.seller.bean.SellerBaseInfoBean;
import com.example.huangjinding.ub_seller.seller.bean.SellerIncomeDetailBean;
import com.example.huangjinding.ub_seller.seller.bean.SellerQrBean;
import com.example.huangjinding.ub_seller.seller.service.listener.BaseResponseListener;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResponseListener;
import com.example.huangjinding.ub_seller.seller.service.listener.ResultListener;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.huangjinding.ub_seller.seller.base.GlobalInfo.userToken;

/**
 * Created by huangjinding on 2017/6/6.
 */

public class SellerService extends BaseService {

    public SellerService(Viewable context) {
        super(context);
    }

    //商家登录
    public void login(String mobile, String password, final ResultListener<String> resultListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("login_account", mobile);
        params.put("shop_password", password);
        postJson(UrlConstant.LOGIN, params, new BaseResponseListener(context, resultListener) {
            @Override
            public void success(JSONObject jsonObject) throws JsonSyntaxException, JSONException {
                JSONObject dataJson = jsonObject.getJSONObject("data");
                String token = dataJson.getString("token");
                resultListener.successHandle(token);
            }

        });
    }

    //获取验证码
    public void getAuthCode(String mobile, ResultListener<String> resultListener) {
        get(UrlConstant.MOBILECODE + "/" + mobile, null, new BaseResponseListener(context, resultListener) {
            @Override
            public void success(JSONObject jsonObject) throws JsonSyntaxException, JSONException {
                JSONObject data = jsonObject.getJSONObject("data");
                Log.i("data",data.toString());
                String msg = jsonObject.getString("msg");
                if (!data.toString().equals("{}") && !TextUtils.isEmpty(data.toString())) {
                    String authCode = data.getString("code");
                    resultListener.successHandle(authCode);
                } else {
                    context.showToast(msg);
                }

            }
        });
    }

    //找回密码
    public void postChange(String mobile, String pwd, String code, ResultListener<String> resultListener) {
        Map<String, Object> params = new HashMap<>();
        int codeNew = Integer.parseInt(code);
        params.put("telephone", mobile);
        params.put("shop_password", pwd);
        params.put("code", codeNew);

        postJson(UrlConstant.PWD_RESET, params, new BaseResponseListener(context, resultListener) {
            @Override
            public void success(JSONObject jsonObject) throws JSONException {
                String code = jsonObject.getString("code");
                if ("200".equals(code)) {
                    context.showToast("修改成功");
                    resultListener.successHandle("");
                } else {
                    context.showToast("修改失败");
                }
            }
        });
    }

    //获取商家基础资料
    public void getSellerBaseInfo(ResultListener<SellerBaseInfoBean> resultListener) {
        Map<String, String> params = new HashMap<>();
        String token = userToken;
        if (TextUtils.isEmpty(token)) {
            token = "\"\"";
        }
        params.put("token", token);
        get(UrlConstant.SELLER_INFO, params, new CommonResponseListener<SellerBaseInfoBean>(context,
                resultListener, new TypeToken<SellerBaseInfoBean>() {
        }));
    }

    //商家二维码
    public void getQrCodeSeller(String unionshopId, ResultListener<SellerQrBean> resultListener) {
        get(UrlConstant.SELLER_QR + "/" + unionshopId, null, new CommonResponseListener<SellerQrBean>(context,
                resultListener, new TypeToken<SellerQrBean>() {
        }));

    }

    //商家收益总额
    public void getMoney(String month, ResultListener<GetMoneyBean> resultListener) {
        Map<String, String> params = new HashMap<>();
        String token = userToken;
        if (TextUtils.isEmpty(token)) {
            token = "\"\"";
        }
        params.put("token", token);
        params.put("month", month);
        get(UrlConstant.TOTAL, params, new CommonResponseListener<GetMoneyBean>(context,
                resultListener, new TypeToken<GetMoneyBean>() {
        }));

    }

    //商家收益明细
    public void getDetail(String date, String month, ResultListener<List<SellerIncomeDetailBean>> resultListener) {
        Map<String, String> params = new HashMap<>();
        String token = userToken;
        if (TextUtils.isEmpty(token)) {
            token = "\"\"";
        }
        params.put("token", token);
        params.put("month", month);
        get(UrlConstant.DETAIL + date, params, new CommonResponseListener<List<SellerIncomeDetailBean>>(context,
                resultListener, new TypeToken<List<SellerIncomeDetailBean>>() {
        }));
    }

    //联系客服获取问题
    public void getQuestion(ResultListener<List<QuestionBean>> resultListener) {
        get(UrlConstant.CONTACT_CUSTOMER, null, new CommonResponseListener<List<QuestionBean>>(context,
                resultListener, new TypeToken<List<QuestionBean>>() {
        }));
    }

    //客服热线
    public void linePlatform(ResultListener<HotLineBean> resultListener) {
        get(UrlConstant.SERVICE_HOTLINE, null, new CommonResponseListener<HotLineBean>(context,
                resultListener, new TypeToken<HotLineBean>() {
        }));
    }

    //关于平台
    public void aboutPlatform(ResultListener<AboutPlatformBean> resultListener) {
        get(UrlConstant.ABOUT_PLATFORM, null, new CommonResponseListener<AboutPlatformBean>(context,
                resultListener, new TypeToken<AboutPlatformBean>() {
        }));
    }

    //商家某月提现明细和总额
    public void getBalanceList(String month, ResultListener<ApplyBalanceInfoBean> resultListener) {
        Map<String, String> params = new HashMap<>();
        String token = GlobalInfo.userToken;
        if (TextUtils.isEmpty(token)) {
            token = "\"\"";
        }
        params.put("token", token);
        params.put("month", month);
        get(UrlConstant.MONTH_ALL, params, new CommonResponseListener<ApplyBalanceInfoBean>(context,
                resultListener, new TypeToken<ApplyBalanceInfoBean>() {
        }));
    }

    //银行名称列表（供下拉框用）
    public void getBankList(ResultListener<List<BankBean>> resultListener) {
        get(UrlConstant.BANK_LIST, null, new CommonResponseListener<List<BankBean>>(context,
                resultListener, new TypeToken<List<BankBean>>() {
        }));
    }

    //商家添加银行账号
    public void postAddBankInfo(String telephone,String userToken, String bankName, String bankAccountName, String bankAccountNummber, String openBankName, String code, ResultListener resultListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("telephone", telephone);
        params.put("token", userToken);
        params.put("bank_name", bankName);
        params.put("bank_account_name", bankAccountName);
        params.put("bank_account_number", bankAccountNummber);
        params.put("opening_bank_name", openBankName);
        params.put("code", code);

        postJson(UrlConstant.BANK_ADD, params, new BaseResponseListener(context, resultListener) {
            @Override
            public void success(JSONObject jsonObject) throws JSONException {
                String code = jsonObject.getString("code");
                if ("200".equals(code)) {
                    context.showToast("添加成功");
                } else {
                    context.showToast("添加失败");
                }
            }
        });
    }

    //商家添加微信
    public void postAddWechatInfo(String userToken, String wechatName, String wechatNumber, String code, ResultListener resultListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("wechat_account_number", wechatNumber);
        params.put("wechat_account_name", wechatName);
        params.put("code", code);

        postJson(UrlConstant.WECHAT_ADD, params, new BaseResponseListener(context, resultListener) {
            @Override
            public void success(JSONObject jsonObject) throws JSONException {
                String code = jsonObject.getString("code");
                if ("200".equals(code)) {
                    context.showToast("添加成功");
                    resultListener.successHandle("");
                } else {
                    context.showToast("添加失败");
                }
            }
        });
    }

    //商家删除微信账号
    public void deleteWechat(String token, ResultListener resultListener) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        get(UrlConstant.WECHAT_DELETE, params, new CommonResponseListener(context,
                resultListener, new TypeToken<Object>() {
        }));
    }

    //商家删除支付宝账号
    public void deleteAlipay(String token, ResultListener resultListener) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        get(UrlConstant.ALIPAY_DELETE, params, new CommonResponseListener(context,
                resultListener, new TypeToken<Object>() {
        }));
    }

    //商家添加支付宝
    public void postAddAlipayInfo(String telephone,String userToken, String alipayName, String alipayNumber, String code, ResultListener resultListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("telephone", telephone);
        params.put("token", userToken);
        params.put("alipay_account_number", alipayNumber);
        params.put("alipay_account_name", alipayName);
        params.put("code", code);
        postJson(UrlConstant.ALIPAY_ADD, params, new BaseResponseListener(context, resultListener) {
            @Override
            public void success(JSONObject jsonObject) throws JSONException {
                String code = jsonObject.getString("code");
                if ("200".equals(code)) {
                    context.showToast("添加成功");
                    resultListener.successHandle("");
                } else {
                    context.showToast("添加失败");
                }
            }
        });
    }

    //商家银行列表
    public void getBankList(String Token, ResultListener<List<BankInfoBean>> resultListener) {
        Map<String, String> params = new HashMap<>();
        params.put("token", Token);
        get(UrlConstant.SELLER_BANK_LIST, params, new CommonResponseListener<List<BankInfoBean>>(context,
                resultListener, new TypeToken<List<BankInfoBean>>() {
        }));
    }

    //商家删除银行账号
    public void getDeliteBankList(String id, String Token, ResultListener resultListener) {
//         int bank_id=Integer.parseInt(id);
        // Integer bank_id=Integer.valueOf(id);
        //  BigInteger bank_id=new BigInteger(id);
        Map<String, String> params = new HashMap<>();
        params.put("token", Token);
        get(UrlConstant.DELETE_BANK + "/" + id, params, new CommonResponseListener(context,
                resultListener, new TypeToken<Object>() {
        }));
    }

    //商家申请提现
    public void postBalance(String type, double amount, String id, ResultListener resultListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("amount", amount);
        params.put("id", id);
        postJson(UrlConstant.APPLY, params, new BaseResponseListener(context, resultListener) {
            @Override
            public void success(JSONObject jsonObject) throws JSONException {
                String code = jsonObject.getString("code");
                if ("200".equals(code)) {
                    context.showToast("申请成功");
                } else {
                    context.showToast("申请失败");
                }
                resultListener.successHandle("");
            }
        });

    }
}