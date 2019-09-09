package com.baby.entity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Login implements Serializable {
	public String role_id;
	public String name;
	public String idcode;

	public Login(JSONObject json) throws JSONException {
		if (json.has("role_id")) role_id = json.optString("role_id");
		if (json.has("name")) name = json.optString("name");
		if (json.has("idcode")) idcode = json.optString("idcode");
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("role_id", role_id);
		obj.put("name", name);
		obj.put("idcode", idcode);
		return obj;
	}
}
