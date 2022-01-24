package com.example.otegoloss;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConnectionJSON {

    // http通信で受け取ったデータをString化する
    public static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    // Jsonデータに変換
    public static JSONObject ChangeJson(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            JSONObject jsonData = new JSONObject();
            // JSONArray jsonArray = jsonObject.getJSONArray("sample");
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonData = jsonArray.getJSONObject(i);
            }
            return jsonData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> ChangeArrayJSON(String str, String name) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            JSONObject jsonData = new JSONObject();
            List<String> stringList = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonData = jsonArray.getJSONObject(i);
                stringList.add(jsonData.getString(name));
            }
            return stringList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
