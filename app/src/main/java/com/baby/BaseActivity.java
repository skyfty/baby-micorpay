package com.baby;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.baby.util.BabyUtil;

import java.util.List;

/**
 * 所有页面的父类
 * 
 * @author dongli
 *
 */
@SuppressLint("NewApi")
public class BaseActivity extends AppCompatActivity implements OnClickListener {

	public static boolean isActive = false;
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		registerFinishCurrentPageWorkMonitor();
	}

	/**
	 * 注册通知事件
	 */
	public void registerFinishCurrentPageWorkMonitor() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalParam.ACTION_DESTROY_CURRENT_ACTIVITY);
		registerReceiver(mReceiver, filter);
	}

	/*
	 * 处理页面公用通知
	 */
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}
			if (intent.getAction().equals(GlobalParam.ACTION_DESTROY_CURRENT_ACTIVITY)) {
				String name = ((Activity) mContext).getLocalClassName();
			}
		}
	};


	/** --- for title bar --- */
	@Override
	protected void onStop() {
		super.onStop();
		BabyUtil.appOnStop(mContext);
		isActive = false;// 记录当前已经进入后台
	}

	@Override
	protected void onResume() {
		super.onResume();
		isActive = true;// 记录当前已经进入前台
	}

	public boolean isAppOnForeground() {
		ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
			InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		isActive = false;
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}

}
