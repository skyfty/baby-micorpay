package com.baby;

import android.os.Bundle;

public class LoginActivity extends WebCordovaActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mUrl = "file:///android_asset/www/login.html";
		super.onCreate(savedInstanceState);
	}
}
