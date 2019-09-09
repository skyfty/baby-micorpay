package com.baby;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.baby.entity.Login;
import com.baby.entity.ModCat;
import com.baby.entity.ModCatMap;
import com.baby.util.BabyUtil;
import com.baby.util.NetUtil;
import com.baby.widget.CustomProgressDialog;
import com.baby.widget.MessageDialog;
import com.baby.widget.PayselDialog;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BabyPlugin extends CordovaPlugin {

    private int mMapIdx = 1000;

    class CordovaCallback {
        public int requestCode;
        public CallbackContext context;

        public CordovaCallback(int r, CallbackContext c) {
            requestCode = r;
            context = c;
        }
    }
    private HashMap<Integer, CordovaCallback> mMapCallback = new HashMap<Integer, CordovaCallback>();
    private ModCatMap mCatMap = null;

    protected void pluginInitialize() {
        NetUtil.getAllCinfo().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    if (json == null || json.optInt("code", -1) < 0 || !json.has("body")) {
                        String msg = json != null && json.has("error") ? json.optString("error"):BabyUtil.getString(cordova,R.string.category_error);
                        throw new Exception(msg);
                    }
                    mCatMap = new ModCatMap(json.getJSONArray("body"));

                } catch (Exception e) {
                }
            }
        });
    }

    private void finishActivity(int result) {
        Intent intent = new Intent();
        cordova.getActivity().setResult(result, intent);
        cordova.getActivity().finish();
    }

	@Override  
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {

            if (GlobalParam.ACTION_FINISH_ACTIVITY.equals(action)) {
                finishActivity(args.getString(0).equals("ok")?Activity.RESULT_OK:Activity.RESULT_CANCELED);
                return false;
            }
            if (GlobalParam.ACTION_START_ACTIVITY.equals(action)) {
            	startActivity(args);
                return true;
            }

            if (GlobalParam.ACTION_START_WEB_ACTIVITY.equals(action)) {
                startWebActivity(args);
                return true;
            }
            if (GlobalParam.ACTION_START_ASSET_ACTIVITY.equals(action)) {
                startAssetActivity(args);
                return true;
            }

            if (GlobalParam.ACTION_SHOW_TOASH.equals(action)) {
                Message message = new Message();
                message.what = GlobalParam.MSG_TOASH_MESSAGE;
                message.obj = args.getString(0);
                mBaseHandler.sendMessage(message);
                return true;
            }

            if (GlobalParam.ACTION_LOGOUT.equals(action)) {
                BabyUtil.saveLoginResult(cordova.getActivity(), null);
                cordova.getActivity().startActivity(new Intent().setClass(cordova.getActivity(), LoginActivity.class));
                cordova.getActivity().finish();
                return true;
            }
            if (GlobalParam.ACTION_SETTING.equals(action)) {
                BabyUtil.saveLoginResult(cordova.getActivity(), null);
                cordova.getActivity().startActivity(new Intent().setClass(cordova.getActivity(), SettingActivity.class));
                return true;
            }

            if (GlobalParam.ACTION_SHOW_MESSAGE_DIALOG.equals(action)) {
                showMessageDialog(args, callbackContext);
                return true;
            }

            if (GlobalParam.ACTION_SHOW_PROCESS_DIALOG.equals(action)) {
                showProgressDialog(args.getString(0));
                return true;
            }

            if (GlobalParam.ACTION_HIDE_PROCESS_DIALOG.equals(action)) {
                hideProgressDialog();
                return true;
            }

            if (GlobalParam.ACTION_PRINT_MARKET.equals(action)) {
                printMarket(args, callbackContext);
                return true;
            }

            if (!BabyUtil.verifyNetwork(cordova.getActivity())) {
                sendErrorMessage(GlobalParam.MSG_NETWORK_ERROR, callbackContext);
                return false;
            }

            if (GlobalParam.ACTION_LOGIN.equals(action)) {
                login(args, callbackContext);
                return true;
            }
            if (GlobalParam.ACTION_GET_LOGIN_INFO.equals(action)) {
                getLoginInfo(callbackContext);
                return true;
            }

            if (GlobalParam.ACTION_SELECT_CUSTOMER.equals(action)) {
                showSelectCustomer(args, callbackContext);
                return true;
            }

            if (GlobalParam.ACTION_COMPILE_SELECT_CUSTOMER.equals(action)) {
                completeSelectCustomer(args, callbackContext);
                return true;
            }

            if (GlobalParam.ACTION_SEARCH_CUSTOMER_OPTIONS.equals(action)) {
                searchCustomerOptions(args, callbackContext);
                return true;
            }

            if (GlobalParam.ACTION_COMPILE_SEARCH_OPTIONS.equals(action)) {
                completeSearchOptions(args, callbackContext);
                return true;
            }

            if (GlobalParam.ACTION_SELECT_PRODUCT.equals(action)) {
                showSelectProduct(args, callbackContext);
                return true;
            }

            if (GlobalParam.ACTION_COMPILE_SELECT_PRODUCT.equals(action)) {
                completeSelectProduct(args, callbackContext);
                return true;
            }

            if (GlobalParam.ACTION_SEARCH_PRODUCT_OPTIONS.equals(action)) {
                searchProductOptions(args, callbackContext);
                return true;
            }

            if (GlobalParam.ACTION_SEARCH_MARKET_OPTIONS.equals(action)) {
                searchMarketOptions(args, callbackContext);
                return true;
            }

            if (GlobalParam.ACTION_NEW_MARKET.equals(action)) {
                startNewMarket(args, callbackContext);
                return false;
            }

            if (GlobalParam.ACTION_COMPILE_MARKET.equals(action)) {
                completeMarket(args, callbackContext);
                return false;
            }
            if (GlobalParam.ACTION_VIEW_MARKET.equals(action)) {
                viewMarket(args, callbackContext);
                return false;
            }

            if (GlobalParam.ACTION_EDIT_MARKET.equals(action)) {
                editMarket(args, callbackContext);
                return true;
            }
            if (GlobalParam.ACTION_MARKET_EDIT_PRODUCT.equals(action)) {
                marketEditProduct(args, callbackContext);
                return true;
            }
            if (GlobalParam.ACTION_MARKET_ADD_PRODUCT.equals(action)) {
                marketAddProduct(args, callbackContext);
                return true;
            }

            if (GlobalParam.ACTION_LIST_MARKET.equals(action)) {
                listMarket(args, callbackContext);
                return true;
            }

            if (GlobalParam.ACTION_PAY_MARKET.equals(action)) {
                payMarket(args, callbackContext);
                return true;
            }
            if (GlobalParam.ACTION_PAY_SELECT.equals(action)) {
                paySelect(args, callbackContext);
                return true;
            }
            if (GlobalParam.ACTION_SCAN_QRCODE.equals(action)) {
                scanQrcode(args, callbackContext);
                return true;
            }
            if (GlobalParam.ACTION_SCAN_CARDPAY.equals(action)) {
                scanCardpay(args, callbackContext);
                return false;
            }
            callbackContext.error("Invalid action");  
        } catch(Exception e) {  
            callbackContext.error(e.getMessage());  
        }  
        return false;  
    }

	protected CustomProgressDialog mProgressDialog;

	/*
	 * 处理整个运用公用消息
	 */
    public Handler mBaseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case GlobalParam.SHOW_PROGRESS_DIALOG:{
                String dialogMsg = (String) msg.obj;
                showProgressDialog(dialogMsg);
                break;
            }
            case GlobalParam.HIDE_PROGRESS_DIALOG:{
                hideProgressDialog();
                String hintMsg = (String) msg.obj;
                if (hintMsg != null && !hintMsg.equals("")) {
                    Toast.makeText(cordova.getActivity(), hintMsg, Toast.LENGTH_LONG).show();
                }
            }
            break;
            case GlobalParam.MSG_NETWORK_ERROR:{
                Toast.makeText(cordova.getActivity(),  R.string.network_error, Toast.LENGTH_LONG).show();
                hideProgressDialog();
                break;
            }
            case GlobalParam.MSG_TIMEOUT_ERROR:{
                hideProgressDialog();
                String timeOutMsg = (String) msg.obj;
                Toast.makeText(cordova.getActivity(), timeOutMsg, Toast.LENGTH_LONG).show();
                break;
            }
            case GlobalParam.MSG_LOAD_ERROR:{
                hideProgressDialog();
                String error_Detail = (String) msg.obj;
                if (error_Detail != null && !error_Detail.equals("")) {
                    Toast.makeText(cordova.getActivity(), error_Detail, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(cordova.getActivity(), R.string.load_error, Toast.LENGTH_LONG).show();
                }
            }
            break;

            case GlobalParam.MSG_TICE_OUT_EXCEPTION:{
                hideProgressDialog();
                String message = (String) msg.obj;
                if (message == null || message.equals("")) {
                    message = BabyUtil.getString(cordova,R.string.timeout);
                }
                Toast.makeText(cordova.getActivity(), message, Toast.LENGTH_LONG).show();
            }
            break;
            case GlobalParam.MSG_TOASH_MESSAGE: {
                String hintMsg = (String) msg.obj;
                if (hintMsg != null && !hintMsg.equals("")) {
                    Toast.makeText(cordova.getActivity(), hintMsg, Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
        }

    };


    public void sendErrorMessage(String msg, final CallbackContext callbackContext) {
        Message message = new Message();
        message.what = GlobalParam.MSG_LOAD_ERROR;
        message.obj = msg;
        mBaseHandler.sendMessage(message);
        if (callbackContext != null) {
            callbackContext.error(msg);
        }
    }

    public void sendErrorMessage(int msg, final CallbackContext callbackContext) {
        mBaseHandler.sendEmptyMessage(msg);
        if (callbackContext != null) {
            callbackContext.error(msg);
        }
    }

	public void showProgressDialog(String msg) {
		mProgressDialog = new CustomProgressDialog(cordova.getActivity());
		mProgressDialog.setMessage(msg);
		mProgressDialog.show();
	}

	public void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	private void showMessageDialog(JSONArray args, final CallbackContext callbackContext) {
		MessageDialog dialog = new MessageDialog(cordova.getActivity(),new MessageDialog.OnFinishClick() {
			@Override
			public void onFinishListener(String content) {
				callbackContext.success(content);
			}
		});
		dialog.show();
	}

	private void startActivityForResult(Intent intent, int requestCode, final CallbackContext callbackContext) {
        mMapIdx++;
        mMapCallback.put(mMapIdx, new CordovaCallback(requestCode, callbackContext));
        cordova.startActivityForResult(this, intent, mMapIdx);
    }

    private void showSelectCustomer(final JSONArray args, final CallbackContext callbackContext) throws ClassNotFoundException, JSONException {
        Intent intent = new Intent().setClass(cordova.getActivity(), CustomerActivity.class);
        intent.putExtra("param", args.optString(0, ""));
        startActivityForResult(intent, GlobalParam.ACTION_REQUEST_CODE_SELECT_CUSTOMER, callbackContext);
    }

    private void completeSelectCustomer(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Activity current = cordova.getActivity();
        Intent intent = new Intent();
        intent.putExtra("customer_id", args.getString(0));
        current.setResult(Activity.RESULT_OK, intent);
        current.finish();
        callbackContext.success();
    }

    private void searchCustomerOptions(final JSONArray args, final CallbackContext callbackContext) {
        Intent intent = new Intent().setClass(cordova.getActivity(), CustomerActivity.class);
        intent.putExtra("action", "search_options");
        startActivityForResult(intent, GlobalParam.ACTION_REQUEST_CODE_SEARCH_OPTIONS, callbackContext);
    }

    private void completeSearchOptions(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Activity current = cordova.getActivity();
        Intent intent = new Intent();
        intent.putExtra("params", args.getString(0));
        current.setResult(Activity.RESULT_OK, intent);
        current.finish();
        callbackContext.success();
    }

    private void showSelectProduct(final JSONArray args, final CallbackContext callbackContext) throws ClassNotFoundException, JSONException {
        Intent intent = new Intent().setClass(cordova.getActivity(), ProductActivity.class);
        intent.putExtra("param", args.optString(0, ""));
        startActivityForResult(intent, GlobalParam.ACTION_REQUEST_CODE_SELECT_PRODUCT, callbackContext);
    }

    private void completeSelectProduct(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Activity current = cordova.getActivity();
        Intent intent = new Intent();
        intent.putExtra("product_id", args.getString(0));
        current.setResult(Activity.RESULT_OK, intent);
        current.finish();
        callbackContext.success();
    }


    private void searchMarketOptions(final JSONArray args, final CallbackContext callbackContext) {
        Intent intent = new Intent().setClass(cordova.getActivity(), MarketActivity.class);
        intent.putExtra("action", "search_options");
        startActivityForResult(intent, GlobalParam.ACTION_REQUEST_CODE_SEARCH_OPTIONS, callbackContext);
    }

    private void searchProductOptions(final JSONArray args, final CallbackContext callbackContext) {
        Intent intent = new Intent().setClass(cordova.getActivity(), ProductActivity.class);
        intent.putExtra("action", "search_options");
        startActivityForResult(intent, GlobalParam.ACTION_REQUEST_CODE_SEARCH_OPTIONS, callbackContext);
    }

    private void editMarket(final JSONArray args, final CallbackContext callbackContext) throws ClassNotFoundException, JSONException {
        Intent intent = new Intent();
        intent.putExtra("action", "edit");
        intent.putExtra("id", args.getString(0));
        intent.setClass(cordova.getActivity(), MarketActivity.class);
        startActivityForResult(intent, GlobalParam.ACTION_REQUEST_CODE_EDIT_MARKET, callbackContext);
    }


    private void completeMarket(JSONArray args, final CallbackContext callbackContext) throws JSONException {
        JSONObject newmarket = args.getJSONObject(0);
        Intent intent = new Intent();
        intent.putExtra("market",newmarket.toString());

        if (args.getString(1).equals("edit")) {
            cordova.getActivity().setResult(Activity.RESULT_OK, intent);
        } else {
            intent.putExtra("action", "view");
            intent.setClass(cordova.getActivity(), MarketActivity.class);
            cordova.startActivityForResult(BabyPlugin.this, intent, 0);
        }
        cordova.getActivity().finish();
    }

    private void payMarket(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Intent intent = new Intent();
        intent.putExtra("action", "pay");
        intent.putExtra("id", args.getString(0));
        intent.setClass(cordova.getActivity(), MarketActivity.class);
        cordova.startActivityForResult(BabyPlugin.this, intent, 0);
    }

    private void startNewMarket(final JSONArray args, final CallbackContext callbackContext) throws ClassNotFoundException, JSONException {
        final String category_id = args.getString(0);

        ModCat nc =mCatMap.getModCat(category_id);
        if (nc != null) {
            startNewMarketActivity(nc);
        } else {
            NetUtil.getCinfo(category_id).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    sendErrorMessage(BabyUtil.getString(cordova,R.string.category_error), callbackContext);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        if (json == null || json.optInt("code", -1) < 0 || !json.has("body")) {
                            String msg = json != null && json.has("error") ? json.optString("error"):BabyUtil.getString(cordova,R.string.category_error);
                            throw new Exception(msg);
                        }

                        ModCat nc = new ModCat(json.getJSONObject("body"));
                        nc.category_id = category_id;
                        startNewMarketActivity(nc);
                    } catch (Exception e) {
                        sendErrorMessage(e.getMessage(), callbackContext);
                    }
                }
            });
        }
    }

    private void startNewMarketActivity(ModCat nc) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("mc", nc);
        Intent intent = new Intent();
        intent.putExtra("action", "add");
        intent.putExtra("category", bundle);
        intent.setClass(cordova.getActivity(), MarketActivity.class);
        cordova.startActivityForResult(BabyPlugin.this, intent, 0);
    }

    private void viewMarket(JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Intent intent = new Intent();
        intent.putExtra("action", "view");
        intent.putExtra("id", args.getString(0));
        intent.setClass(cordova.getActivity(), MarketActivity.class);
        cordova.startActivityForResult(BabyPlugin.this, intent, 0);
    }


    private void paySelect(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        final String market_id = args.getString(0);
        final double total_fee = args.getDouble(1);
        if (market_id.isEmpty() || total_fee <= 0) {
            callbackContext.error("error param");
            return;
        }

        final PayselDialog dlg = new PayselDialog(cordova.getActivity());
        dlg.setOperationListener(new PayselDialog.OnOperationListener(){
            @Override
            public void onSelected(int id) {
                dlg.dismiss();
                Intent intent = new Intent();
                intent.putExtra("pay_channel", id);
                intent.putExtra("market_id", market_id);
                intent.putExtra("total_fee", total_fee);
                if (id == R.id.cardpay) {
                    intent.setClass(cordova.getActivity(), CardpayActivity.class);
                } else {
                    intent.setClass(cordova.getActivity(), PrepayActivity.class);
                }
                startActivityForResult(intent, GlobalParam.ACTION_REQUEST_CODE_PAY_MARKET, callbackContext);
            }

            @Override
            public void onCancel() {
                dlg.dismiss();
            }
        });
        dlg.show();
    }

    private void scanQrcode(JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Intent intent = new Intent().setClass(cordova.getActivity(), CaptureActivity.class);
        startActivityForResult(intent, GlobalParam.ACTION_REQUEST_CODE_SCAN_QRCODE, callbackContext);
    }

    private void scanCardpay(JSONArray args, final CallbackContext callbackContext) throws JSONException {
        cordova.onMessage(GlobalParam.ACTION_SCAN_CARDPAY, args);
    }

    private void listMarket(JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Intent intent = new Intent();
        intent.putExtra("action", "index");
        intent.setClass(cordova.getActivity(), MarketActivity.class);
        cordova.startActivityForResult(BabyPlugin.this, intent, 0);
    }

    private void marketEditProduct(JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Intent intent = new Intent().setClass(cordova.getActivity(), MarketActivity.class);
        intent.putExtra("action", "product_edit");
        intent.putExtra("id", args.getString(0));
        intent.putExtra("market_product_id", args.getString(1));
        startActivityForResult(intent, GlobalParam.ACTION_REQUEST_CODE_EDIT_PRODUCT, callbackContext);
    }

    private void marketAddProduct(JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Intent intent = new Intent().setClass(cordova.getActivity(), MarketActivity.class);
        intent.putExtra("action", "product_add");
        intent.putExtra("id", args.getString(0));
        startActivityForResult(intent, GlobalParam.ACTION_REQUEST_CODE_ADD_PRODUCT, callbackContext);
    }

    private void startActivity(JSONArray args) throws ClassNotFoundException, JSONException {
        Intent intent = new Intent().setClass(cordova.getActivity(), Class.forName(args.getString(0)));
        cordova.startActivityForResult(this, intent, 0);
    }

    private void startWebActivity(JSONArray args) throws ClassNotFoundException, JSONException {
        Intent intent = new Intent().setClass(cordova.getActivity(), WebCordovaActivity.class);
        String url = GlobalParam.SERVER_PREFIX + args;
        if (url.indexOf("?") == -1) {
            url += "?";
        }
        url += "/role_id/" +  BabyUtil.getLoginResult(cordova.getActivity()).role_id;
        intent.putExtra("url", url);
        cordova.startActivityForResult(this, intent, 0);
    }

    private void startAssetActivity(JSONArray args) throws ClassNotFoundException, JSONException {
        Intent intent = new Intent().setClass(cordova.getActivity(), WebCordovaActivity.class);
        intent.putExtra("url", "file:///android_asset/www/" + args);
        cordova.startActivityForResult(this, intent, 0);
    }

    private void printMarket(JSONArray args, final CallbackContext callbackContext) throws JSONException {
        String market = args.getString(0);
        Intent intent = new Intent().setClass(cordova.getActivity(), PrinterActivity.class);
        intent.putExtra("market", market);
        startActivityForResult(intent, GlobalParam.ACTION_REQUEST_CODE_PRINT_MARKET, callbackContext);
    }

    private void login(JSONArray args, final CallbackContext callbackContext)  throws ClassNotFoundException, JSONException {
        String username = args.getString(0);
        if (username.equals("")) {
            Toast.makeText(cordova.getActivity(), R.string.please_input_username, Toast.LENGTH_SHORT).show();
            return;
        }

        String password =  args.getString(1);
        if (password.equals("")) {
            Toast.makeText(cordova.getActivity(), R.string.please_input_password, Toast.LENGTH_SHORT).show();
            return;
        }

        Message message = new Message();
        message.what = GlobalParam.SHOW_PROGRESS_DIALOG;
        message.obj = BabyUtil.getString(cordova,R.string.loading_login);
        mBaseHandler.sendMessage(message);

        NetUtil.getLogin(username, password).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendErrorMessage(BabyUtil.getString(cordova,R.string.login_error), callbackContext);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    if (json == null || json.optInt("code", -1) < 0 || !json.has("body")) {
                        String msg = json != null && json.has("error") ? json.optString("error") : BabyUtil.getString(cordova, R.string.category_error);
                        throw new Exception(msg);
                    }
                    Login lr = new Login(json.getJSONObject("body"));
                    BabyUtil.saveLoginResult(cordova.getActivity(), lr);
                    cordova.getActivity().startActivity(new Intent().setClass(cordova.getActivity(), MainActivity.class));
                    cordova.getActivity().finish();
                }catch (Exception e) {
                    sendErrorMessage(e.getMessage(), callbackContext);
                }
            }
        });

        PluginResult mPlugin = new PluginResult(PluginResult.Status.NO_RESULT);
        mPlugin.setKeepCallback(true);
        callbackContext.sendPluginResult(mPlugin);
    }

    private void getLoginInfo(final CallbackContext callbackContext) throws JSONException{
        Login login = BabyUtil.getLoginResult(cordova.getActivity());
        if (login == null || callbackContext == null) {
            callbackContext.error("not login");
            return;
        }
        callbackContext.success(login.toJSON());
    }

    @Override
	public Boolean shouldAllowRequest(String url) {
		return true;
	}

	public boolean onOverrideUrlLoading(String url) {
		return false;
	}

	@Override
	public Boolean shouldAllowNavigation(String url) {
		// TODO Auto-generated method stub
		return true;
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        Object mapobj = mMapCallback.get(requestCode);
        if (resultCode == Activity.RESULT_OK) {
            try {
                Bundle extras = intent.getExtras();
                if (mapobj != null) {
                    CordovaCallback callback = (CordovaCallback)mapobj;
                    onActivityResult(callback.requestCode, extras, callback.context);
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mapobj!= null) {
            mMapCallback.remove(requestCode);
            CordovaCallback callback = (CordovaCallback)mapobj;
            if (resultCode != Activity.RESULT_OK && callback != null) {
                callback.context.error("cancel");
            }
        }
    }

    private void onActivityResult(int requestCode, Bundle extras, final CallbackContext callbackContext) throws JSONException {
        switch (requestCode) {
            case GlobalParam.ACTION_REQUEST_CODE_SELECT_CUSTOMER: {
                callbackContext.success(extras.getString("customer_id"));
                break;
            }
            case GlobalParam.ACTION_REQUEST_CODE_SELECT_PRODUCT: {
                callbackContext.success(extras.getString("product_id"));
                break;
            }
            case GlobalParam.ACTION_REQUEST_CODE_PRINT_MARKET:
             {
                callbackContext.success();
                break;
            }
            case GlobalParam.ACTION_REQUEST_CODE_EDIT_PRODUCT:
            case GlobalParam.ACTION_REQUEST_CODE_ADD_PRODUCT:
            case GlobalParam.ACTION_REQUEST_CODE_EDIT_MARKET: {
                callbackContext.success(extras.getString("market"));
                break;
            }
            case GlobalParam.ACTION_REQUEST_CODE_SEARCH_OPTIONS:
            case GlobalParam.ACTION_REQUEST_CODE_PAY_MARKET: {
                if (extras == null) {
                    callbackContext.success();
                } else {
                    callbackContext.success(extras.getString("params"));
                }
                break;
            }
            case GlobalParam.ACTION_REQUEST_CODE_SCAN_QRCODE: {
                callbackContext.success(extras.getString("result"));
                break;
            }
            default:
                break;
        }
    }


}
