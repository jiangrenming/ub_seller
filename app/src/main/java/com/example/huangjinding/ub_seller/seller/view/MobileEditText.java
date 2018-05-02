package com.example.huangjinding.ub_seller.seller.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huangjinding.ub_seller.R;
import com.example.huangjinding.ub_seller.seller.base.Viewable;
import com.example.huangjinding.ub_seller.seller.listener.DataChangeListener;
import com.example.huangjinding.ub_seller.seller.service.SellerService;
import com.example.huangjinding.ub_seller.seller.service.listener.CommonResultListener;
import com.example.huangjinding.ub_seller.seller.util.Tools;
import com.example.huangjinding.ub_seller.seller.util.ValidateUtil;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;


public class MobileEditText extends LinearLayout implements Validatable {

	private EditText et_mobile;
	private TextView tv_get_code;
	private Context context;

	private int remainSecond = 60;

	private Timer countDownTimer;

	private DataChangeListener dataChangeListener;
	private OnMoblieOnclikLisener onMoblieOnclikLisener;

	private SellerService userService;

	public MobileEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_edit_mobile, this);

		tv_get_code = (TextView) view.findViewById(R.id.tv_get_code);
		et_mobile = (EditText) view.findViewById(R.id.et_mobile);
		et_mobile.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void afterTextChanged(Editable s) {
				inputChangeHandle();
			}
		});

		tv_get_code.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getVerifyCode();
			}
		});

		userService = new SellerService((Viewable) context);
	}

	public String getText() {
		return et_mobile.getText().toString().trim();
	}

	public void setDataChangeListener(DataChangeListener dataChangeListener) {
		this.dataChangeListener = dataChangeListener;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			tv_get_code.setText(remainSecond + "S");
			remainSecond--;
			if (remainSecond <= 0) {
				tv_get_code.setEnabled(true);
				tv_get_code.setText("获取验证码");
				tv_get_code.setTextColor(Tools.getColorValueByResId(context, R.color.text_red));
				countDownTimer.cancel();// 取消
				remainSecond = 60;
			}

		}

		;
	};

	private void getVerifyCode() {
		String rs = validate();
		if (rs != null) {
			((Viewable) context).showToast(rs);
			return;
		}
		getAuthCode();
	}

	public void setOnclickLisener(OnMoblieOnclikLisener onMoblieOnclikLisener) {
		this.onMoblieOnclikLisener = onMoblieOnclikLisener;
	}


	public interface OnMoblieOnclikLisener {
		void onClick();
	}

	private void inputChangeHandle() {
		int length = getText().length();
		int textColor = R.color.text_gray;
		boolean enable = false;
		if (length == 11) {
			if ("获取验证码".equals(tv_get_code.getText())) {
				textColor = R.color.text_red;
			}
			enable = true;
		}
		tv_get_code.setTextColor(Tools.getColorValueByResId(context, textColor));
		tv_get_code.setEnabled(enable);

		if (dataChangeListener != null) {
			dataChangeListener.handle(getText());
		}
	}

	private void getAuthCode() {
		userService.getAuthCode(getText(), new CommonResultListener<String>((Viewable) context) {
			@Override
			public void successHandle(String result) throws JSONException {
				((Viewable) context).showToast("发送成功");
				timeSchedule();

			}
		});
	}

	private void timeSchedule() {
		countDownTimer = new Timer();
		countDownTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendMessage(new Message());
			}
		}, 0, 1000);
		tv_get_code.setEnabled(false);
		tv_get_code.setTextColor(Tools.getColorValueByResId(context, R.color.text_gray2));
		if (onMoblieOnclikLisener != null) {
			onMoblieOnclikLisener.onClick();
		}

	}



	public String validate(){
		String number = getText();
		if(number.length() == 0)
			return "手机号码不能空";

		if(!ValidateUtil.isMobilePhone(number)){
			return "手机号码格式不正确";
		}

		return null;
	}

}
