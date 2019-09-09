package com.baby;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baby.entity.Login;
import com.baby.entity.ModCat;
import com.baby.util.BabyUtil;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaInterfaceImpl;

public class ProductActivity extends WebCordovaActivity {
	protected String mAction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Login login = BabyUtil.getLoginResult(this);
		if (login == null) {
			Toast.makeText(this, R.string.tip_not_login, Toast.LENGTH_LONG).show();
			ProductActivity.this.finish();
		}
        Bundle extras = getIntent().getExtras();
		String uid = BabyUtil.getLoginId(this);

		mAction = extras.getString("action", "index");
		switch(mAction) {
			case "search_options": {
				mUrl = "file:///android_asset/www/product_search_options.html?" + "login_role_id="+uid;
				break;
			}
			default: {
				mUrl = "file:///android_asset/www/product.html?login_role_id="+uid;
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
				case "search_options": {
					break;
				}
				default: {
					calljs("deviceready", extras.getString("param", ""));
					break;
				}
			}
		}
		return super.onMessage(id, data);
	}
}
