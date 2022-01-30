package com.example.otegoloss.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ShippingAddrInfoDeleteFragment extends Fragment {

    public TextView shipping_info_realnamed;
    public TextView postalcoded;
    public TextView user_addrd;
    public TextView shipping_info_realnamed2;
    public TextView postalcoded2;
    public TextView user_addrd2;

    private String[] postalcodeds;
    private String[] user_addrds;
    private String[] realnameds;
    private String[] shippingID;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_shipping_address_info_delete, container, false);

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000003";
        }
        System.out.println(userID);

        shipping_info_realnamed = view.findViewById(R.id.textView_shipping_info_realnamed1) ;
        postalcoded = view.findViewById(R.id.textView_postalcoded1) ;
        user_addrd = view.findViewById(R.id.textView_user_addrd1) ;
        shipping_info_realnamed2 = view.findViewById(R.id.textView_shipping_info_realnamed2) ;
        postalcoded2 = view.findViewById(R.id.textView_postalcoded2) ;
        user_addrd2 = view.findViewById(R.id.textView_user_addrd2) ;

        // http通信
        Thread t = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/ListingDelivery.php";

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
                            try {
                                List<String> credit_idList = ConnectionJSON.ChangeArrayJSON(str, "d_address_id");
                                shippingID = credit_idList.toArray(new String[credit_idList.size()]);

                                List<String> postalcodedList = ConnectionJSON.ChangeArrayJSON(str, "postal_code");
                                List<String> addrdList = ConnectionJSON.ChangeArrayJSON(str, "address");
                                List<String> realnamedList = ConnectionJSON.ChangeArrayJSON(str, "real_name");

                                postalcodeds = postalcodedList.toArray(new String[postalcodedList.size()]);
                                user_addrds = addrdList.toArray(new String[addrdList.size()]);
                                realnameds = realnamedList.toArray(new String[realnamedList.size()]);

                                shipping_info_realnamed.setText(realnameds[0]);
                                postalcoded.setText(postalcodeds[0]);
                                user_addrd.setText(user_addrds[0]);
                                shipping_info_realnamed2.setText(realnameds[1]);
                                postalcoded2.setText(postalcodeds[1]);
                                user_addrd2.setText(user_addrds[1]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

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

        Button shippingdeleteButton1 = view.findViewById(R.id.shippingdelete_button);
        shippingdeleteButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shipping_info_realnameds = shipping_info_realnamed.getText().toString();
                if(shipping_info_realnameds.equals("NO DATA")){
                    Toast.makeText(view.getContext() , "登録されていません", Toast.LENGTH_LONG).show();
                }else {
                    // http通信
                    Thread t = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            try {
                                // phpファイルまでのリンク
                                String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/DeleteDelivery.php";

                                // クエリ文字列を連想配列に入れる
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("user_id", userID);
                                map.put("d_address_id", shippingID[0]);
                                // クエリ文字列組み立て・URL との連結
                                StringJoiner stringUrl = new StringJoiner("&", path + "?", "");
                                for (Map.Entry<String, String> param : map.entrySet()) {
                                    stringUrl.add(param.getKey() + "=" + param.getValue());
                                }
                                URL url = new URL(stringUrl.toString());
                                System.out.println(url);
                                // 処理開始時刻
                                startTime = System.currentTimeMillis();
                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                final String str = ConnectionJSON.InputStreamToString(con.getInputStream());

                                // 終了時刻
                                endTime = System.currentTimeMillis();
                                Log.d("HTTP", str);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println(str);
                                        System.out.println(endTime - startTime);

//                                    SharedPreferences.Editor editor = userIDData.edit();
//                                    editor.putString("userID", "");
                                        userIDData.edit().remove("shippingID[0]");
                                        Toast.makeText(view.getContext(), str, Toast.LENGTH_LONG).show();

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

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Navigation.findNavController(view).navigate(R.id.action_shipping_addr_info_delete_to_shipping_addr_info_config);
                }
            }
        });

        Button shippingdeleteButton2 = view.findViewById(R.id.shippingdelete_button2);
        shippingdeleteButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shipping_info_realnamed2s = shipping_info_realnamed2.getText().toString();
                if(shipping_info_realnamed2s.equals("NO DATA")){
                    Toast.makeText(view.getContext() , "登録されていません", Toast.LENGTH_LONG).show();
                }else{
                    // http通信
                    Thread t = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            try {
                                // phpファイルまでのリンク
                                String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/DeleteDelivery.php";

                                // クエリ文字列を連想配列に入れる
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("user_id", userID);
                                map.put("d_address_id", shippingID[1]);
                                // クエリ文字列組み立て・URL との連結
                                StringJoiner stringUrl = new StringJoiner("&", path + "?", "");
                                for (Map.Entry<String, String> param : map.entrySet()) {
                                    stringUrl.add(param.getKey() + "=" + param.getValue());
                                }
                                URL url = new URL(stringUrl.toString());
                                System.out.println(url);
                                // 処理開始時刻
                                startTime = System.currentTimeMillis();
                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                final String str = ConnectionJSON.InputStreamToString(con.getInputStream());

                                // 終了時刻
                                endTime = System.currentTimeMillis();
                                Log.d("HTTP", str);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println(str);
                                        System.out.println(endTime - startTime);

//                                    SharedPreferences.Editor editor = userIDData.edit();
//                                    editor.putString("userID", "");
                                        userIDData.edit().remove("shippingID[1]");
                                        Toast.makeText(view.getContext(), str, Toast.LENGTH_LONG).show();

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

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Navigation.findNavController(view).navigate(R.id.action_shipping_addr_info_delete_to_shipping_addr_info_config);
                }
            }
        });

        return view;
    }

}
