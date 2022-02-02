package com.example.otegoloss;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ChangeBackgraund {


    public static void changeBackGround(LinearLayout view, String userID){

        // http通信
        Thread t = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/ReturnUidFromWeight.php";

                    // クエリ文字列を連想配列に入れる
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("user_id", userID);
                    // クエリ文字列組み立て・URL との連結
                    StringJoiner stringUrl = new StringJoiner("&", path + "?", "");
                    for (Map.Entry<String, String> param : map.entrySet()) {
                        stringUrl.add(param.getKey() + "=" + param.getValue());
                    }
                    URL url = new URL(stringUrl.toString());
                    System.out.println(url);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    final String str = ConnectionJSON.InputStreamToString(con.getInputStream());
                    JSONObject jsnObject = ConnectionJSON.ChangeJson(str);
                    int weight = Integer.parseInt(jsnObject.getString("gross_weight"));
                    System.out.println("weight=" + weight);
                    Log.d("HTTP", str);

                    if (weight < 1000) {
                        view.setBackgroundResource(R.drawable.screen1);
                    } else if (weight < 3000) {
                        view.setBackgroundResource(R.drawable.screen2);
                    } else if (weight < 6000) {
                        view.setBackgroundResource(R.drawable.screen3);
                    } else if (weight < 10000) {
                        view.setBackgroundResource(R.drawable.screen4);
                    } else {
                        view.setBackgroundResource(R.drawable.screen4);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            t.start();
            t.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }

}
