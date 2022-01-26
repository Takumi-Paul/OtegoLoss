package com.example.otegoloss.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.StartActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class AccountDeleteFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_view_request_to_delete, container, false);

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

//        // 所属親アクティビティを取得
//        MainActivity activity = (MainActivity) getActivity();
//        // 戻るボタンを表示する
//        activity.setupBackButton(true);
//
//        // この記述でフラグメントでアクションバーメニューが使えるようになる
//        setHasOptionsMenu(true);

        if (userID == "error") {
            userID = "u0000001";
        }
        System.out.println(userID);

        Button deleteButton = view.findViewById(R.id.append_button);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // http通信
                Thread t = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            // phpファイルまでのリンク
                            String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/DeleteAccount.php";

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
                                    System.out.println(str);
                                    System.out.println(endTime - startTime);

//                                    SharedPreferences.Editor editor = userIDData.edit();
//                                    editor.putString("userID", "");
                                    userIDData.edit().remove("userID");
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

                    Navigation.findNavController(view).navigate(R.id.action_accountdelete_to_delete_comp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });



        return view;
    }

}
