package com.baby;

import android.os.Bundle;

import com.baby.util.BabyUtil;

public class SettingActivity extends WebCordovaActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
		mUrl = "file:///android_asset/www/setting.html?" + "login_role_id="+BabyUtil.getLoginId(this);
        super.onCreate(savedInstanceState);
	}
}
