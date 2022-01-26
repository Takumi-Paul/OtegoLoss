/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.home;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ViewPurchaseInformation1 extends Fragment {

    ProgressDialog progressDialog;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_purchase_information1, container, false);

        // BundleでHome画面の値を受け取り
        Bundle bundle = getArguments();

        // ユーザID
        String userID = bundle.getString("USER_ID", "");
        System.out.println("bundle"+userID);

        String productID = bundle.getString("PRODUCT_ID", "");

        String cardID = bundle.getString("CARD_ID", "");

        String daddressID = bundle.getString("SHIPPING_ID", "");


        //「購入確定」ボタンが押された時の処理
        Button BuyCompletedButton = view.findViewById(R.id.buyCompletedButton);


        BuyCompletedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // http通信
                Thread t = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            // phpファイルまでのリンク
                            String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/InsertPurchase.php";

                            // クエリ文字列を連想配列に入れる
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("product_id", productID);
                            map.put("purchaser_id", userID);
                            map.put("card_id", cardID);
                            map.put("address_id", daddressID);
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
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(e);
                        }
                    }
                });

                try{
                    t.start();
                    System.out.println("start");
                    // ダイアログ表示
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("ロード中");
                    progressDialog.setMessage("処理しています");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    t.join();
                    progressDialog.dismiss();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Navigation.findNavController(view).navigate(R.id.action_purchase_information1_to_fragmentViewPurchaseCompleted);

            }
        });

        return view;
    }

}