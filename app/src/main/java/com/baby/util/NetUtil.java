package com.baby.util;


import com.baby.GlobalParam;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NetUtil {
	private static final OkHttpClient mOkHttpClient = new OkHttpClient();

	static public class Uri {

		public HashMap<String, String> map = new HashMap<String, String>();

		public Uri add(String k, String v) {
			map.put(k, v);
			return this;
		}

		public String build(String url) {
			String param = "?";
			for (String key : map.keySet()) {
				if (map.get(key) != null) {
					param +=key + "=" + map.get(key) + "&";
				}
			}
			return url + param;
		}
	}

	static public Call getLogin(String phone, String password) {
		Uri request = new Uri().add("m", "PyUser").add("a", "login")
			.add("name", phone)
			.add("password", password);
		return mOkHttpClient.newCall(new Request.Builder().url(request.build(GlobalParam.SERVER_PREFIX)).build());
	}

	static public Call checkUpgrade(int version) {
		Uri request = new Uri().add("m", "Upgrade").add("a", "check")
			.add("version", String.valueOf(version));
		return mOkHttpClient.newCall(new Request.Builder().url(request.build(GlobalParam.SERVER_PREFIX)).build());
	}

	static public Call getCinfo(String category_id) {
		Uri request = new Uri().add("m", "PyIndex").add("a", "getcinfo")
			.add("cid", String.valueOf(category_id));
		return mOkHttpClient.newCall(new Request.Builder().url(request.build(GlobalParam.SERVER_PREFIX)).build());
	}

	static public Call getAllCinfo() {
		Uri request = new Uri().add("m", "PyIndex").add("a", "getallcinfo");
		return mOkHttpClient.newCall(new Request.Builder().url(request.build(GlobalParam.SERVER_PREFIX)).build());
	}
}
