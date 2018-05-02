//package com.example.huangjinding.ub_seller;
//
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.example.huangjinding.ub_seller.seller.activity.FindPwdActivity;
//import com.example.huangjinding.ub_seller.seller.activity.MainActivity;
//import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
//import com.example.huangjinding.ub_seller.seller.base.LoginAction;
//import com.example.huangjinding.ub_seller.seller.service.SellerService;
//import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
//
//import org.json.JSONException;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * Created by huangjinding on 2017/6/2.
// */
//
//public class LoginActivity extends BaseActivity {
//
//
//    @BindView(R.id.iv_close)
//    ImageView ivClose;
//    @BindView(R.id.et_number)
//    EditText etNumber;
//    @BindView(R.id.et_pwd)
//    EditText etPwd;
//    @BindView(R.id.btn_login)
//    Button btnLogin;
//    @BindView(R.id.tv_quck_register)
//    TextView tvQuckRegister;
//    @BindView(R.id.tv_forget_pwd)
//    TextView tvForgetPwd;
//    @BindView(R.id.activity_main)
//    LinearLayout activityMain;
//    @BindView(R.id.ll_other_login)
//    LinearLayout mOtherLogin;
//    private String number;
//    private String pwd;
//    private SellerService sellerService;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        addLayoutListener(activityMain, mOtherLogin);
//    }
//
//    private void addLayoutListener(final LinearLayout activityMain, final LinearLayout mOtherLogin) {
//        activityMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Rect rect = new Rect();
//                //1、获取main在窗体的可视区域
//                activityMain.getWindowVisibleDisplayFrame(rect);
//                //2、获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
//                int mainInvisibleHeight = activityMain.getRootView().getHeight() - rect.bottom;
//                int screenHeight = activityMain.getRootView().getHeight();//屏幕高度
//                //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
//                if (mainInvisibleHeight > screenHeight / 4) {
//                    int[] location = new int[2];
//                    mOtherLogin.getLocationInWindow(location);
//                    // 4､获取Scroll的窗体坐标，算出main需要滚动的高度
//                    int srollHeight = (location[1] + mOtherLogin.getHeight()) - rect.bottom;
//                    //5､让界面整体上移键盘的高度
//                    activityMain.scrollTo(0, srollHeight);
//                } else {
//                    //3、不可见区域小于屏幕高度1/4时,说明键盘隐藏了，把界面下移，移回到原有高度
//                    activityMain.scrollTo(0, 0);
//                }
//            }
//        });
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_login;
//    }
//
//    @Override
//    protected void initView() {
//        super.initView();
//        sellerService = new SellerService(this);
//
//    }
//
//
//    @OnClick({R.id.iv_close, R.id.btn_login, R.id.tv_forget_pwd, R.id.et_pwd, R.id.et_number})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.iv_close:
//                break;
//            case R.id.btn_login:
//                toLogin();
//                break;
//            case R.id.tv_forget_pwd:
//                startActivity(FindPwdActivity.class);
//                break;
//        }
//    }
//
//
//    private void toLogin() {
//        number = etNumber.getText().toString().trim();
//        pwd = etPwd.getText().toString().trim();
//        if ("".equals(number) && "".equals(pwd)) {
//            showToast("帐号和密码不能为空");
//        } else if ("".equals(number)) {
//            showToast("帐号不能为空");
//        } else if ("".equals(pwd)) {
//            showToast("密码不能为空");
//        } else {
//            sellerService.login(number, pwd, new CommonResultListener<String>(this) {
//                @Override
//                public void successHandle(String result) throws JSONException {
//                    LoginAction.login(result, LoginActivity.this);
//                    LoginAction.saveMobile(number, LoginActivity.this);
//                    startActivity(MainActivity.class);
//                    finish();
//                }
//            });
//        }
//    }
////        String message = null;
////        if (number.length() == 0 || pwd.length() == 0) {
////            message = "用户名和密码不能为空";
////        } else if (pwd.length() < 0) {
////            message = "密码格式不正确";
////        } else {
////            boolean isMobile = ValidateUtil.isMobilePhone(number);
////            if (!isMobile) {
////                message = "手机号码格式不正确";
////            } else {
////                try {
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////
////        sellerService.login(number, pwd, new CommonResultListener<String>(this) {
////            @Override
////            public void successHandle(String result) throws JSONException {
////                LoginAction.login(result, LoginActivity.this);
////                LoginAction.saveMobile(number, LoginActivity.this);
////                startActivity(MainActivity.class);
////                finish();
////            }
////        });
////    }
////       }
////        if (message != null) {
////            showToast(message);
////        }
////    }
//
//}
