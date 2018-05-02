package com.example.huangjinding.ub_seller.seller.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.HeadView;
import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.LoginActivity;
import com.example.huangjinding.ub_seller.seller.base.BaseActivity;
import com.example.huangjinding.ub_seller.seller.base.LoginAction;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
import com.example.huangjinding.ub_seller.seller.util.ValidateUtil;
import com.example.huangjinding.ub_seller.seller.view.KeyboardLayout;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.huangjinding.ub_seller.R.id.tv_get_code;


/**
 * Created by huangjinding on 2017/6/2.
 */

public class FindPwdActivity extends BaseActivity {

    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_change_pwd)
    TextView tvChangePwd;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.keyboard_forget_password)
    KeyboardLayout mKeyboardLayout;
    @BindView(R.id.scv_forget_password)
    ScrollView mScrollScrollView;
    private String mobile;
    private SellerService sellerService;
    //    private Dialog dialog;
    private int remainSecond = 60;
    private Timer countDownTimer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_back_pwd;
    }

    @Override
    protected void initView() {
        super.initView();
        hvHead.setTitle("找回密码");
        sellerService = new SellerService(this);
        addLayoutListener();
    }

    /**
     * 监听键盘状态，布局有变化时，靠scrollView去滚动界面
     */
    public void addLayoutListener() {
        mKeyboardLayout.setKeyboardListener(new KeyboardLayout.KeyboardLayoutListener() {
            @Override
            public void onKeyboardStateChanged(boolean isActive, int keyboardHeight) {
                Log.e("onKeyboardStateChanged", "isActive:" + isActive + " keyboardHeight:" + keyboardHeight);
                if (isActive) {
                    scrollToBottom();
                }
            }
        });
    }

    /**
     * 弹出软键盘时将SVContainer滑到底
     */
    private void scrollToBottom() {

        mScrollScrollView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mScrollScrollView.smoothScrollTo(0, mScrollScrollView.getBottom() + getStatusBarHeight(FindPwdActivity.this));
            }
        }, 100);

    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        //获取状态栏的高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return activity.getResources().getDimensionPixelSize(resourceId);
    }

    @OnClick({tv_get_code, R.id.tv_change_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case tv_get_code:
                getCode();
                break;
            case R.id.tv_change_pwd:
                confirmChange();
                break;
        }
    }

//    private void showDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(FindPwdActivity.this);
//        final AlertDialog dialog = builder.create();
//        LayoutInflater inflater = LayoutInflater.from(this);
//        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.change_success_dialog, null);
//        dialog.setView(layout, 0, 0, 0, 0);
//        ImageView skip = (ImageView) layout.findViewById(R.id.iv_cansel);
//        skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        Button confirm = (Button) layout.findViewById(R.id.btn_confirm);
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(LoginActivity.class);
//            }
//        });
//        Window dialogWindow = dialog.getWindow();
//        WindowManager m = getWindowManager();
//        dialogWindow.setGravity(Gravity.CENTER);
//        Display d = m.getDefaultDisplay();
//        WindowManager.LayoutParams p = dialogWindow.getAttributes();
//        p.width = (int) (d.getWidth() * 0.8);
//        dialogWindow.setAttributes(p);
//        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.show();
//    }

    private void getCode() {
        mobile = etNumber.getText().toString().trim();
        if ("".equals(mobile)) {
            showToast("请输入手机号码");
        } else {
            if (mobile.length() != 11) {
                showToast("请输入11位数的手机号码");
            } else {
                boolean isMobile = ValidateUtil.isMobilePhone(mobile);
                if (!isMobile) {
                    showToast("手机号码格式不正确");
                } else {
                    sellerService.getAuthCode(mobile, new CommonResultListener<String>(this) {
                        @Override
                        public void successHandle(String result) throws JSONException {
                            timeSchedule();
                        }
                    });
                }
            }
        }
    }

    private void confirmChange() {
        mobile = etNumber.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if ("".equals(mobile) && "".equals(code) && "".equals(pwd)) {
            showToast("请填写完整信息");
        } else if ("".equals(mobile)) {
            showToast("请输入手机号码");
        } else if ("".equals(code)) {
            showToast("请输入验证码");
        } else if ("".equals(pwd)) {
            showToast("请输入新的密码");
        } else {
            sellerService.postChange(mobile, pwd, code, new CommonResultListener<String>(this) {
                @Override
                public void successHandle(String result) throws JSONException {
//                    showToast("重新登录");
                    LoginAction.logout(FindPwdActivity.this);
                    startActivity(new Intent(FindPwdActivity.this, LoginActivity.class));
                    finish();
                }
            });
        }
    }

    private void timeSchedule() {
        countDownTimer = new Timer();
        countDownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendMessage(new Message());
            }
        }, 0, 1000);
        tvGetCode.setEnabled(false);
    }

    Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            tvGetCode.setText(remainSecond + "S");
            remainSecond--;
            if (remainSecond <= 0) {
                tvGetCode.setEnabled(true);
                tvGetCode.setText("获取验证码");
                countDownTimer.cancel();// 取消
                remainSecond = 60;
            }

        }

    };
}
