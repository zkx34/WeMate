package com.zkx.weipo.app.openapi.models;

import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/12.
 */
public class RepostList {

    public List<Reposts> repostsList;
    public Reposts reposts;
    public String previous_cursor;
    public String next_cursor;
    public int total_number;

    public static RepostList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        RepostList reposts = new RepostList();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            reposts.previous_cursor = jsonObject.optString("previous_cursor", "0");
            reposts.next_cursor     = jsonObject.optString("next_cursor", "0");
            reposts.total_number    = jsonObject.optInt("total_number", 0);

            JSONArray jsonArray      = jsonObject.optJSONArray("reposts");
            if (jsonArray != null && jsonArray.length() > 0) {
                int length = jsonArray.length();
                reposts.repostsList = new ArrayList<Reposts>(length);
                for (int ix = 0; ix < length; ix++) {
                    reposts.repostsList.add(Reposts.parse(jsonArray.getJSONObject(ix)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reposts;
    }
}

