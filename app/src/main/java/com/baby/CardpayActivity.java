package com.baby;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.baby.entity.Login;
import com.baby.util.BabyUtil;
import com.wizarpos.paymentrouter.aidl.IWizarPayment;

import org.json.JSONException;
import org.json.JSONObject;

public class CardpayActivity extends WebCordovaActivity {

	private IWizarPayment mWizarPayment;
	final ServiceConnection mConnPayment = new PaymentConnection();

	class PaymentConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName compName, IBinder binder) {
			calljs("on_paystate", "{'RespCode':'SC', 'RespDesc':'支付连接已成功'}");
			mWizarPayment = IWizarPayment.Stub.asInterface(binder);
			createAsyncTask().execute();
		}

		@Override
		public void onServiceDisconnected(ComponentName compName) {
			mWizarPayment = null;
			calljs("on_paystate", "{'RespCode':'SD', 'RespDesc':'支付连接已断开'}");
		}
	}

    @Override
	public void onCreate(Bundle savedInstanceState) {
		Login login = BabyUtil.getLoginResult(this);
		if (login == null) {
			Toast.makeText(this, R.string.tip_not_login, Toast.LENGTH_LONG).show();
			CardpayActivity.this.finish();
		}
		Bundle extras = getIntent().getExtras();

		String uid = BabyUtil.getLoginId(this);
		double total_fee = 0.0;//extras.getDouble("total_fee", 0.0);
		String market_id = "16853";//extras.getString("market_id");
		mUrl = "file:///android_asset/www/cardpay.html?market_id=" + market_id + "&total_fee=" +total_fee+"&login_role_id="+uid;
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindPaymentRouter();
	}

	private void bindPaymentRouter() {
		if (mWizarPayment == null) {
			Intent intent = new Intent(IWizarPayment.class.getName());
			bindService(intent, mConnPayment, BIND_AUTO_CREATE);
		}
	}
	private void unbindPaymentRouter() {
		if (mWizarPayment != null) {
			unbindService(mConnPayment);
			mWizarPayment = null;
		}
	}


	private AsyncTask<Integer, Void, String> createAsyncTask() {
		return new AsyncTask<Integer, Void, String>() {
			protected void onPreExecute() {
				calljs("on_paystate", "{'RespCode':'SS', 'RespDesc':'开始支付'}");
			}
			protected String doInBackground(Integer...btnIds) {
				String result;
				try {
					JSONObject json = new JSONObject(mWizarPayment.login(setParamlogin()));
					if (json.optString("RespCode", "").equals("00")) {
						json = new JSONObject(mWizarPayment.payCash(setParamPayCash()));
						if (json.optString("RespCode", "").equals("00")) {

							Log.d("doInBackground", "Response: " + json);
						}
					}
					result = json.toString();

				} catch (Exception e) {
					result = "{'RespCode':'SE', 'RespDesc':'"+e.getMessage()+"'}";
				}
				return result;
			}
			protected void onPostExecute(String result) {
				calljs("on_paystate", result);
			}

			private JSONObject setParamCommon()throws JSONException {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("AppID", "50006");
				jsonObject.put("AppName", "test账户");
				return jsonObject;
			}

			private String setParamlogin() throws JSONException {
				JSONObject jsonObject = setParamCommon();
				jsonObject.put("OptCode", "01");
				jsonObject.put("OptPass", "0000");
				return jsonObject.toString();
			}

			private String setParamPayCash() throws JSONException {
				JSONObject jsonObject = setParamCommon();
				jsonObject.put("TrxID", "12498423");
				jsonObject.put("TransType", 1);
				jsonObject.put("TransAmount", "000000000001");
				jsonObject.put("TransIndexCode", "14526855");
				jsonObject.put("ReqTransDate", "140421");
				jsonObject.put("ReqTransTime", "100445");
				return jsonObject.toString();
			}
		};
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			calljs("on_cancel_pay");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case GlobalParam.ACTION_REQUEST_CODE_CARDPAY: {
					bindPaymentRouter();
					calljs("on_paystate", "{'RespCode':'SS', 'RespDesc':'连接设备'}");
					break;
				}
			}
		}
	};

	@Override
	public Object onMessage(String id, Object data) {
		if (id.equals(GlobalParam.ACTION_SCAN_CARDPAY)) {
			mHandler.sendEmptyMessage(GlobalParam.ACTION_REQUEST_CODE_CARDPAY);
		}
		return super.onMessage(id, data);
	}

}
