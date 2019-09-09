package com.baby;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baby.entity.Login;
import com.baby.util.BabyUtil;


public class CustomerActivity extends WebCordovaActivity {
	protected String mAction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Login login = BabyUtil.getLoginResult(this);
		if (login == null) {
			Toast.makeText(this, R.string.tip_not_login, Toast.LENGTH_LONG).show();
			CustomerActivity.this.finish();
		}
		Bundle extras = getIntent().getExtras();
		mAction = extras.getString("action", "index");
		switch(mAction) {
			case "search_options": {
				mUrl = "file:///android_asset/www/customer_search_options.html";
				break;
			}
			default: {
				mUrl = "file:///android_asset/www/customer.html?login_role_id="+ login.role_id;
				break;
			}
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public Object onMessage(String id, Object data) {
		if (id.equals("onPageFinished")) {
			Bundle extras = getIntent().getExtras();
			switch(mAction) {
				case "index": {
					calljs("deviceready", extras.getString("param", "{}"));
					break;
				}
			}
		}
		return super.onMessage(id, data);
	}

}
