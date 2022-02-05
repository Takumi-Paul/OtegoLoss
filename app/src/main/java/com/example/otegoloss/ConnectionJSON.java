package com.example.otegoloss;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
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

    public static Bitmap downloadImage(URL url) {
        Bitmap bmp = null;
        HttpURLConnection urlConnection = null;

        try {
            // HttpURLConnection インスタンス生成
            urlConnection = (HttpURLConnection) url.openConnection();

            // タイムアウト設定
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(20000);

            // リクエストメソッド
            urlConnection.setRequestMethod("GET");

            // リダイレクトを自動で許可しない設定
            urlConnection.setInstanceFollowRedirects(false);

            // ヘッダーの設定(複数設定可能)
            urlConnection.setRequestProperty("Accept-Language", "jp");

            // 接続
            urlConnection.connect();
            int resp = urlConnection.getResponseCode();

            switch (resp){
                case HttpURLConnection.HTTP_OK:
                    try(InputStream is = urlConnection.getInputStream()){
                        bmp = BitmapFactory.decodeStream(is);

                        is.close();
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return bmp;
    }


}
