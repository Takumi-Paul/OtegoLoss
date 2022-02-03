package com.example.otegoloss.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class MeterFragment extends Fragment {

    public ImageView image_mater;
    public TextView meter_username;
    public TextView meter_weight;

    // ユーザID
    String userID;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_meter, container, false);

        image_mater = view.findViewById(R.id.imageView_mater) ;
        meter_weight = view.findViewById(R.id.meter_weight) ;

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        //userID = "u0000001";


        Resources res = getResources();
        Drawable meter0 = ResourcesCompat.getDrawable(res, R.drawable.meter0, null);
        Drawable meter1 = ResourcesCompat.getDrawable(res, R.drawable.meter1, null);
        Drawable meter2 = ResourcesCompat.getDrawable(res, R.drawable.meter2, null);
        Drawable meter3 = ResourcesCompat.getDrawable(res, R.drawable.meter3, null);
        Drawable meter4 = ResourcesCompat.getDrawable(res, R.drawable.meter4, null);

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
                    for (Map.Entry<String, String> param: map.entrySet()) {
                        stringUrl.add(param.getKey() + "=" + param.getValue());
                    }
                    URL url = new URL(stringUrl.toString());
                    System.out.println(url);
                    // 処理開始時刻
                    startTime = System.currentTimeMillis();
                    HttpURLConnection con =(HttpURLConnection)url.openConnection();
                    final String str = ConnectionJSON.InputStreamToString(con.getInputStream());

                    // 終了時刻
                    endTime = System.currentTimeMillis();
                    Log.d("HTTP", str);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(String.valueOf(str));
                            System.out.println(endTime - startTime);

                            JSONObject jsnObject = ConnectionJSON.ChangeJson(str);
                            //try {
                                // Jsonのキーを指定すれば対応する値が入る
                            try {
                                meter_weight.setText(jsnObject.getString("gross_weight"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //meter_weight.setText("1000000");
                                String meter_weights = meter_weight.getText().toString();
                                if(meter_weights.equals("重さ")){
                                    //image_mater.setImageDrawable(meter0);
                                }else {
                                    int meter_weighti = Integer.parseInt(meter_weights);
                                    if (meter_weighti < 1000) {
                                        image_mater.setImageDrawable(meter0);
                                    }else if(meter_weighti < 2000){
                                        image_mater.setImageDrawable(meter1);
                                    }else if(meter_weighti < 5000){
                                        image_mater.setImageDrawable(meter2);
                                    }else if(meter_weighti < 10000){
                                        image_mater.setImageDrawable(meter3);
                                    }else{
                                        image_mater.setImageDrawable(meter4);
                                    }
                                }
                            //} catch (JSONException e) {
                                //e.printStackTrace();
                            //}

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }
        });

        try {
            t.start();
            t.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        return view;
    }

}
