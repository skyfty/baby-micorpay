package com.baby.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.StringRes;
import android.util.Base64;

import com.baby.entity.Login;

import org.apache.cordova.CordovaInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;


public class BabyUtil {
	public static boolean isApplicationActive = false;

	public static final String LOGIN_SHARED = "login_shared";
	public static final String LOGIN_RESULT = "login";

	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getApplicationContext().getPackageName();

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
	
	public static void appOnStop(Context context){
		if (!isAppOnForeground(context) && BabyUtil.isApplicationActive) {
			//app 进入后台
			BabyUtil.isApplicationActive = false;

		}
	}

	private static Boolean mIsNetWorkAvailable = false;
	public static void setNetWorkState(boolean state){
		mIsNetWorkAvailable = state;
	}

	public static boolean verifyNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null) {
			if (activeNetInfo.isConnected()) {
				setNetWorkState(true);
				return true;
			} else {
				setNetWorkState(false);
				return false;
			}
		} else {
			setNetWorkState(false);
			return false;
		}
	}
	
	public static int getAppVersion(Context context) {
		int versionCode = 0;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = pi.versionCode;

		} catch (Exception e) {
		}
		return versionCode;
	}

	public static void saveLoginResult(Context context, Login login){
		int mode = Context.MODE_WORLD_WRITEABLE;
		if(Build.VERSION.SDK_INT >= 11){
			mode = Context.MODE_MULTI_PROCESS;
		}
		SharedPreferences preferences = context.getSharedPreferences(LOGIN_SHARED, mode);
		SharedPreferences.Editor editor = preferences.edit();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(login);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String server = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
		editor.putString(LOGIN_RESULT, server);
		editor.commit();
	}

	public static Login getLoginResult(Context context){
		int mode = Context.MODE_WORLD_WRITEABLE;
		if(Build.VERSION.SDK_INT >= 11){
			mode = Context.MODE_MULTI_PROCESS;
		}
		SharedPreferences preferences = context.getSharedPreferences(LOGIN_SHARED, mode);
		String str = preferences.getString(LOGIN_RESULT, "");
		Login login = null;
		if(str.equals("")){
			return null;
		}
		// 对Base64格式的字符串进行解码
		byte[] base64Bytes = Base64.decode(str.getBytes(), Base64.DEFAULT);
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bais);
			Object obj= ois.readObject();
			login =  (Login) obj;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return login;
	}

	public static String getLoginId(Context context){
		Login login = getLoginResult(context);
		return login != null?login.role_id:null;
	}

	public static String getString(Context context, @StringRes int id) {
		return context.getResources().getString(id);
	}

	public static String getString(CordovaInterface cordova, @StringRes int id) {
		return getString(cordova.getActivity(), id);
	}
}
