package com.baby;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.baby.entity.Login;
import com.baby.util.BabyUtil;
import com.unionpay.cloudpos.POSTerminal;

public class PrepayActivity extends WebCordovaActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Login login = BabyUtil.getLoginResult(this);
		if (login == null) {
			Toast.makeText(this, R.string.tip_not_login, Toast.LENGTH_LONG).show();
			PrepayActivity.this.finish();
		}
		POSTerminal terminal = POSTerminal.getInstance(this);
//		String terminal_no = terminal.getTerminalSpec().getSerialNumber();
		String terminal_no = "WP14521000000010";
		String uid = BabyUtil.getLoginId(this);
		Bundle extras = getIntent().getExtras();
		String market_id = extras.getString("market_id");
		String pay_channel = "";
		String pay_channel_mode = "O";
		switch(extras.getInt("pay_channel")) {
			case R.id.wxpay: {
				pay_channel = "W";break;
			}
			case R.id.pwxpay: {
				pay_channel = "W";pay_channel_mode = "P";break;
			}
			case R.id.alipay: {
				pay_channel = "A";break;
			}
			case R.id.palipay: {
				pay_channel = "A";pay_channel_mode = "P";break;
			}
			case R.id.baidupay: {
				pay_channel = "B";break;
			}
			case R.id.pbaidupay: {
				pay_channel = "B";pay_channel_mode = "P";break;
			}
			case R.id.tongyipay: {
				pay_channel = "U";break;
			}
			case R.id.ptongyipay: {
				pay_channel = "U";pay_channel_mode = "P";break;
			}
		}
		double total_fee = extras.getDouble("total_fee", 0.0);
		mUrl = "file:///android_asset/www/prepay.html?terminal_no="+terminal_no+"&market_id=" + market_id+ "&pay_channel=" +pay_channel+ "&pay_channel_mode=" +pay_channel_mode+ "&total_fee=" +total_fee+"&login_role_id="+uid;
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			calljs("on_cancel_pay");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
