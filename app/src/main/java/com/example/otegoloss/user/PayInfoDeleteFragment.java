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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class PayInfoDeleteFragment extends Fragment {

    public TextView creditcard_companyd1;
    public TextView creditcard_numberd1;
    public TextView creditcard_companyd2;
    public TextView creditcard_numberd2;

    private String[] creditcard_companiesd;
    private String[] creditcard_numbersd;
    String[] cardID;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;
    String userID;
    String cardID1;
    String cardID2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_pay_info_delete, container, false);

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000001";
        }
        System.out.println(userID);

        creditcard_companyd1 = view.findViewById(R.id.texiview_creditcard_companyd1) ;
        creditcard_numberd1 = view.findViewById(R.id.textView_creditcard_numberd1) ;
        creditcard_companyd2 = view.findViewById(R.id.texiview_creditcard_companyd2) ;
        creditcard_numberd2 = view.findViewById(R.id.textView_creditcard_numberd2) ;

        // http通信
        Thread t = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/ListingCredit.php";

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
                                List<String> credit_idList = ConnectionJSON.ChangeArrayJSON(str, "card_id");
                                cardID = credit_idList.toArray(new String[credit_idList.size()]);

                                List<String> credit_companydList = ConnectionJSON.ChangeArrayJSON(str, "card_comp");
                                creditcard_companiesd = credit_companydList.toArray(new String[credit_companydList.size()]);
                                creditcard_companyd1.setText(creditcard_companiesd[0]);
                                creditcard_companyd2.setText(creditcard_companiesd[1]);

                                List<String> creditcard_numberdList = ConnectionJSON.ChangeArrayJSON(str, "card_number");
                                creditcard_numbersd = creditcard_numberdList.toArray(new String[creditcard_numberdList.size()]);
                                creditcard_numberd1.setText(creditcard_numbersd[0]);
                                creditcard_numberd2.setText(creditcard_numbersd[1]);
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

        Button creditdeleteButton1 = view.findViewById(R.id.creditdelete_button1);
        creditdeleteButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // http通信
                Thread t = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            // phpファイルまでのリンク
                            String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/DeleteCredit.php";

                            // クエリ文字列を連想配列に入れる
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("user_id", userID);
                            map.put("card_id", cardID[0]);
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
                                    System.out.println(str);
                                    System.out.println(endTime - startTime);

//                                    SharedPreferences.Editor editor = userIDData.edit();
//                                    editor.putString("userID", "");
                                    userIDData.edit().remove("cardID[0]");
                                    Toast.makeText(view.getContext() , str, Toast.LENGTH_LONG).show();

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
            }
        });

        Button creditdeleteButton2 = view.findViewById(R.id.creditdelete_button2);
        creditdeleteButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // http通信
                Thread t = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            // phpファイルまでのリンク
                            String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/DeleteCredit.php";

                            // クエリ文字列を連想配列に入れる
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("user_id", userID);
                            map.put("card_id", cardID[1]);
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
                                    System.out.println(str);
                                    System.out.println(endTime - startTime);

//                                    SharedPreferences.Editor editor = userIDData.edit();
//                                    editor.putString("userID", "");
                                    userIDData.edit().remove("cardID[1]");
                                    Toast.makeText(view.getContext() , str, Toast.LENGTH_LONG).show();

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
            }
        });

        return view;
    }

}
