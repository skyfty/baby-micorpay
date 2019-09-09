package com.baby;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.baby.util.BabyUtil;
import com.baby.util.UpgradeManger;

public class MainActivity extends WebCordovaActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
		mUrl = "file:///android_asset/www/index.html?" + "login_role_id="+BabyUtil.getLoginId(this);
        super.onCreate(savedInstanceState);

		if (BabyUtil.getLoginResult(mContext) == null) {
			startActivity( new Intent(mContext, LoginActivity.class));
			finish();
		}
        new UpgradeManger(this).excute(false);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			calljs("on_exit");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
