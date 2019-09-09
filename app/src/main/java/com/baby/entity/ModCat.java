package com.baby.entity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ModCat implements Serializable {
	public String category_id;
	public String name;

	public ModCat(JSONObject json) throws JSONException {
		if (json.has("category_id")) category_id = json.optString("category_id");
		if (json.has("name")) name = json.optString("name");
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		obj.put("category_id", category_id);
		return obj;
	}
}
