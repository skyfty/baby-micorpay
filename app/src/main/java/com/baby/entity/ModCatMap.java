package com.baby.entity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ModCatMap implements Serializable {
	public HashMap<String,ModCat> mMapCat = new HashMap<String,ModCat>();

    public ModCat getModCat(String cid) {
        if (!mMapCat.containsKey(cid)) {
            return null;
        }
        return mMapCat.get(cid);
    }

	public ModCatMap(JSONArray json) throws JSONException {
        for(int i = 0; i < json.length(); ++i) {
            ModCat newcat = new ModCat(json.optJSONObject(i));
            mMapCat.put(newcat.category_id, newcat);
        }
	}
}
