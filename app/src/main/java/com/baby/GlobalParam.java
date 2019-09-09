package com.baby;

import android.os.Environment;

public class GlobalParam {

	//public static final String IP = "192.168.1.103";public static final String PORT = ":8082";
	//public static final String IP = "10.0.4.135";public static final String PORT = ":8012";
	//public static final String IP = "192.168.1.100";public static final String PORT = ":8012";
	public static final String IP = "a.ayihui.cn";public static final String PORT = ":80";
	public static final String SERVER_PREFIX = "http://" + IP + PORT + "/";


	public static final String ACTION_DESTROY_CURRENT_ACTIVITY = "com.bebo.intent.action.destroy.current.activity";
	public static final String DOWNLOAD_DIR = Environment.getExternalStorageDirectory().getPath()+ "/com.baby/download";

	public final static int MSG_TICE_OUT_EXCEPTION = 11307;
	public final static int MSG_LOAD_ERROR=11818;

	public final static int SHOW_PROGRESS_DIALOG = 0x11112;
	public final static int HIDE_PROGRESS_DIALOG = 0x11113;
	public final static int MSG_NETWORK_ERROR = 11114;
	public final static int MSG_TIMEOUT_ERROR = 11115;
	public final static int MSG_TOASH_MESSAGE = 11116;

	public final static int SHOW_UPGRADE_DIALOG = 10001;
	public final static int NO_NEW_VERSION = 11315;

	public final static int ACTION_REQUEST_NONE = 1;
	public final static int ACTION_REQUEST_CODE_SELECT_CUSTOMER = 2;
	public final static int ACTION_REQUEST_CODE_SELECT_PRODUCT = 4;
	public final static int ACTION_REQUEST_CODE_SEARCH_OPTIONS = 5;

	public final static int ACTION_REQUEST_CODE_ADD_PRODUCT = 6;
	public final static int ACTION_REQUEST_CODE_EDIT_PRODUCT = 7;
	public final static int ACTION_REQUEST_CODE_EDIT_MARKET = 8;
	public final static int ACTION_REQUEST_CODE_PAY_MARKET = 9;
	public final static int ACTION_REQUEST_CODE_SCAN_QRCODE = 10;
	public final static int ACTION_REQUEST_CODE_PRINT_MARKET = 11;
    public final static int ACTION_REQUEST_CODE_INPUT_PAY_PASSWORD = 12;
    public final static int ACTION_REQUEST_CODE_CARDPAY = 13;

	public final static int PAY_TYPE_CONTACTLESS = 1;
	public final static int PAY_TYPE_MSR = 2;
	public final static int PAY_TYPE_IC = 3;

	public final static String ACTION_FINISH_ACTIVITY = "finish_activity";
	public final static String ACTION_LOGIN = "action_login";
	public final static String ACTION_LOGOUT = "action_logout";
	public final static String ACTION_SETTING = "action_setting";

	public static final String ACTION_GET_LOGIN_INFO = "get_login_info";
	public final static String ACTION_START_ACTIVITY = "start_activity";
	public final static String ACTION_SHOW_MESSAGE_DIALOG = "show_message_dialog";
	public final static String ACTION_SHOW_PROCESS_DIALOG = "show_process_dialog";
	public final static String ACTION_HIDE_PROCESS_DIALOG = "hide_process_dialog";
	public final static String ACTION_SHOW_TOASH = "show_toash";
	public final static String ACTION_PRINT_MARKET = "print_market";

	public static final String ACTION_START_WEB_ACTIVITY = "start_web_activity";
	public static final String ACTION_START_ASSET_ACTIVITY = "start_asset_activity";
	public static final String ACTION_SELECT_CUSTOMER = "select_customer";


	public static final String ACTION_COMPILE_SELECT_CUSTOMER = "complete_select_customer";
    public static final String ACTION_SELECT_PRODUCT = "select_product";
    public static final String ACTION_COMPILE_SELECT_PRODUCT = "complete_select_product";


	public static final String ACTION_SEARCH_PRODUCT_OPTIONS = "search_product_options";
	public static final String ACTION_SEARCH_CUSTOMER_OPTIONS = "search_customer_options";
	public static final String ACTION_SEARCH_MARKET_OPTIONS = "search_market_options";
	public static final String ACTION_COMPILE_SEARCH_OPTIONS = "complete_search_options";

	public static final String ACTION_NEW_MARKET = "start_new_market";
	public static final String ACTION_COMPILE_MARKET = "complete_market";
	public static final String ACTION_VIEW_MARKET = "view_market";
	public static final String ACTION_EDIT_MARKET = "edit_market";
	public static final String ACTION_LIST_MARKET = "list_market";
	public static final String ACTION_PAY_MARKET = "pay_market";

	public static final String ACTION_MARKET_EDIT_PRODUCT = "market_edit_product";
	public static final String ACTION_MARKET_ADD_PRODUCT = "market_add_product";

	public static final String ACTION_PAY_SELECT = "pay_select";

	public static final String ACTION_SCAN_QRCODE = "scan_qrcode";
	public static final String ACTION_SCAN_CARDPAY = "scan_cardpay";

}
