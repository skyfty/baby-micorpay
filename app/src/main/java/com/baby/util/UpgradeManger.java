/**
 * probject:lvxin
 * @version 5.0
 * 
 * @author 3979434@qq.com
 */
package com.baby.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Toast;

import com.baby.GlobalParam;
import com.baby.R;
import com.baby.entity.ModCat;
import com.baby.entity.Version;
import com.baby.widget.CustomDialog;
import com.baby.widget.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *
 *
 */
public class UpgradeManger implements  CustomDialog.OnOperationListener, FileDownloader.FileDownloadCallBack {
	CustomProgressDialog progressDialog;
	FileDownloader downloader;
	CustomDialog customDialog;
	Context context;
	File file;
	final DecimalFormat format = new DecimalFormat("0.00");
	private Version mVersion;

	public UpgradeManger(Context ctx) {
		context = ctx;
		customDialog = new CustomDialog(ctx);
		customDialog.setButtonsText(context.getString(R.string.cancel), context.getString(R.string.upgrade));
		customDialog.setTitle(context.getString(R.string.tip_find_newversion));
		customDialog.setOperationListener(this);
		downloader = FileDownloader.getInstance();
		downloader.setOnDownloadCallBack(this);
		progressDialog = new CustomProgressDialog(ctx);
	}


	/**
	 * 处理消息
	 */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case GlobalParam.SHOW_UPGRADE_DIALOG:
					showUpgradeDialog();
					break;
				case GlobalParam.NO_NEW_VERSION:
					Toast.makeText(context, context.getResources().getString(R.string.no_version), Toast.LENGTH_LONG).show();
					break;
				case GlobalParam.MSG_NETWORK_ERROR:
					Toast.makeText(context, R.string.network_error, Toast.LENGTH_LONG).show();
					return;

				case GlobalParam.MSG_TICE_OUT_EXCEPTION:
					String message = (String) msg.obj;
					if (message == null || message.equals("")) {
						message = context.getResources().getString(R.string.timeout);
					}
					Toast.makeText(context, message, Toast.LENGTH_LONG).show();
					break;
			}
		}

	};

	private void showUpgradeDialog() {
		String versionString = context.getString(R.string.label_setting_newversion, mVersion.name);
		SpannableStringBuilder text = new SpannableStringBuilder(versionString + "\n\n" + mVersion.description);
		text.setSpan(new ForegroundColorSpan(Color.parseColor("#3C568B")), 4, mVersion.name.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		text.setSpan(new StyleSpan(Typeface.BOLD), 4, mVersion.name.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		customDialog.setMessage(text);
		customDialog.show();
	}

	public void excute(boolean show) {
		if (show) {
			progressDialog.setMessage(context.getString(R.string.tip_loading, context.getString(R.string.common_detection)));
			progressDialog.show();
		}
		final int curVersion = BabyUtil.getAppVersion(context);
		NetUtil.checkUpgrade(curVersion).enqueue(new Callback() {
			 @Override
			 public void onFailure(Call call, IOException e) {
				 progressDialog.dismiss();
				mHandler.sendEmptyMessage(GlobalParam.MSG_NETWORK_ERROR);
			 }

			 @Override
			 public void onResponse(Call call, Response response) throws IOException {
				 try {
					 JSONObject json = new JSONObject(response.body().string());
					 Version mVersion;
					 if (json != null && json.optInt("code", -1) > 0 && json.has("body") && (mVersion = new Version(json.getJSONObject("body"))) == null) {
						 if (curVersion < mVersion.version) {
							 mHandler.sendEmptyMessage(GlobalParam.SHOW_UPGRADE_DIALOG);
						 }
					 }
				 } catch (Exception e) {

				 }finally {
					 progressDialog.dismiss();
				 }
			 }
		 });
	}


	@Override
	public void onLeftClick() {
		customDialog.dismiss();
	}

	@Override
	public void onRightClick() {
		customDialog.dismiss();
		progressDialog.show();
		progressDialog.setMessage(context.getString(R.string.tip_begin_download));
		file = new File(GlobalParam.DOWNLOAD_DIR + "/baby" + String.valueOf(mVersion.version) + ".apk");
		downloader.download(mVersion.url, file);
	}

	@Override
	public void progress(String threadKey, long fileSize, long downloadSize) {
		String progress = format.format((double) downloadSize / fileSize * 100) + "%";
		progressDialog.setMessage(context.getString(R.string.label_file_download, progress));
		if (downloadSize == fileSize) {
			progressDialog.dismiss();
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setClassName("com.android.packageinstaller", "com.android.packageinstaller.PackageInstallerActivity");
			context.startActivity(intent);
		}
	}

	@Override
	public void statusChange(String threadKey, int status) {
		progressDialog.hide();
		mHandler.sendEmptyMessage(GlobalParam.MSG_NETWORK_ERROR);
	}
}
