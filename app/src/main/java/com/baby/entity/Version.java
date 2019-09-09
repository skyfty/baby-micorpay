package com.baby.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class Version implements Serializable{
	private static final long serialVersionUID = 19874543545465L;
	public int version;
	public String name;
	public String url;
	public String description;

	public Version(JSONObject json) throws JSONException {
		if (json.has("version")) version = json.optInt("version", -1);
		if (json.has("name")) name = json.optString("name");
		if (json.has("url")) url = json.optString("url");
		if (json.has("description")) description = json.optString("description");
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("version", version);
		obj.put("name", name);
		obj.put("url", url);
		obj.put("description", description);
		return obj;
	}

}
