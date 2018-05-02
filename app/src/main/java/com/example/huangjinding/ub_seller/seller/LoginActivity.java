package com.example.huangjinding.ub_seller.seller;

import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.activity.FindPwdActivity;
import com.example.huangjinding.ub_seller.seller.activity.MainActivity;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.base.LoginAction;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
import com.example.huangjinding.ub_seller.seller.util.MySharedPreference;
import com.example.huangjinding.ub_seller.seller.util.SharedKeyConstant;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huangjinding on 2017/6/2.
 */

public class LoginActivity extends BaseActivity {


    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_quck_register)
    TextView tvQuckRegister;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;

    private String number;
    private String pwd;
    private SellerService sellerService;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        checkToken();
        sellerService = new SellerService(this);
        addLayoutListener(activityMain, btnLogin);
    }

    private void addLayoutListener(final LinearLayout activityMain, final Button btnLogin) {
        activityMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //1、获取main在窗体的可视区域
                activityMain.getWindowVisibleDisplayFrame(rect);
                //2、获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mainInvisibleHeight = activityMain.getRootView().getHeight() - rect.bottom;
                int screenHeight = activityMain.getRootView().getHeight();//屏幕高度
                //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
                if (mainInvisibleHeight > screenHeight / 4) {
                    int[] location = new int[2];
                    btnLogin.getLocationInWindow(location);
                    // 4､获取Scroll的窗体坐标，算出main需要滚动的高度
                    int srollHeight = (location[1] + btnLogin.getHeight()) - rect.bottom;
                    //5､让界面整体上移键盘的高度
                    activityMain.scrollTo(0, srollHeight);
                } else {
                    //3、不可见区域小于屏幕高度1/4时,说明键盘隐藏了，把界面下移，移回到原有高度
                    activityMain.scrollTo(0, 0);
                }
            }
        });
    }

//    private void checkToken() {
//        String token = MySharedPreference.get(SharedKeyConstant.TOKEN, "", this);
//        String mobile = MySharedPreference.get(SharedKeyConstant.SAVE_MOBILE, "", this);
//        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(mobile)) {
//            LoginAction.login(token, LoginActivity.this);
//            LoginAction.saveMobile(mobile, LoginActivity.this);
//            startActivity(MainActivity.class);
//            finish();
//        }
//    }
private void checkToken() {
        String token = MySharedPreference.get(SharedKeyConstant.TOKEN, "", this);
        if (!TextUtils.isEmpty(token) ) {
            LoginAction.login(token, LoginActivity.this);
            startActivity(MainActivity.class);
            finish();
        }
    }
    @OnClick({R.id.iv_close, R.id.btn_login, R.id.tv_forget_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                break;
            case R.id.btn_login:
                toLogin();
                break;
            case R.id.tv_forget_pwd:
                startActivity(FindPwdActivity.class);
                break;
        }
    }

    private void toLogin() {
        number = etNumber.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        if ("".equals(number) && "".equals(pwd)) {
            showToast("帐号和密码不能为空");
        } else if ("".equals(number)) {
            showToast("帐号不能为空");
        } else if ("".equals(pwd)) {
            showToast("密码不能为空");
        } else {
            sellerService.login(number, pwd, new CommonResultListener<String>(this) {
                @Override
                public void successHandle(String result) throws JSONException {
                    LoginAction.login(result, LoginActivity.this);
                    LoginAction.saveMobile(number, LoginActivity.this);
                    startActivity(MainActivity.class);
                    finish();
                }
            });
        }
    }
}
