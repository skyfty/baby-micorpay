package com.baby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.PluginManager;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;
import org.json.JSONException;
import org.json.JSONObject;

public class WebCordovaActivity extends BaseActivity {

	Toolbar toolbar;
	protected  String mUrl;
	public CordovaWebView webInterface;
	private CordovaInterfaceImpl cordovaInterface = new CordovaInterfaceImpl(this) {
		public Object onMessage(String id, Object data) {
			return WebCordovaActivity.this.onMessage(id, data);
		}
	};

	public Object onMessage(String id, Object data) {
		if("exit".equals(id)) {
			this.finish();
		}
		return null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onCreateView(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
			moveTaskToBack(true);
		}
		ConfigXmlParser parser = new ConfigXmlParser();
		parser.parse(this);
		SystemWebView webView = (SystemWebView) findViewById(R.id.cordovaWebView);

		try {
			SystemWebViewEngine e = new SystemWebViewEngine(webView);
			webInterface = new CordovaWebViewImpl(e);
			webInterface.init(cordovaInterface, parser.getPluginEntries(), parser.getPreferences());

		}catch (Exception e) {
			e.printStackTrace();
        }

		String launchUrl = "index.html";
		if (!mUrl.isEmpty()) {
			launchUrl = mUrl;
		} else if (extras != null) {
			launchUrl = extras.getString("url", "index.html");
		}
		webView.loadUrl(launchUrl);
	}

	protected void onCreateView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_webnobar);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		cordovaInterface.onActivityResult(requestCode, resultCode, intent);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		PluginManager pluginManager = webInterface.getPluginManager();
		if(pluginManager != null)
		{
			pluginManager.onDestroy();
		}
	}
	public void calljs(String fun) {
		webInterface.loadUrl("javascript:"+fun+"();");
	}
	public void calljs(String fun, String p1) {
		webInterface.loadUrl("javascript:"+fun+"("+"'"+p1+"'"+");");
	}
	public void calljs(String fun, String p1, String p2) {
		webInterface.loadUrl("javascript:"+fun+"('"+p1+"','"+p2+"');");
	}
	public void calljs(String fun, String p1, String p2, String p3) {
		webInterface.loadUrl("javascript:"+fun+"('"+p1+"','"+p2+"','"+p3+"');");
	}
}
