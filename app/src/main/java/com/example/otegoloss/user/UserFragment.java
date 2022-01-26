/*
12/29
Kobayashi
ユーザ画面を生成するプログラム
 */

package com.example.otegoloss.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.StartActivity;
import com.example.otegoloss.databinding.FragmentUserBinding;
import com.example.otegoloss.shipping.EntryOfExhibitInfoFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class UserFragment extends Fragment {

    public ImageView profile_image;
    public TextView profile_username;
    public TextView profile_message;

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
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        profile_image = view.findViewById(R.id.accountImage_imageView) ;
        profile_username = view.findViewById(R.id.profileUserName_textView) ;
        profile_message = view.findViewById(R.id.profileUserMessage_textView) ;

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000003";
        }
        System.out.println(userID);

        Button nextSettingProfileButton = view.findViewById(R.id.nextSettingProfile_button);
        Button nextMeterButton = view.findViewById(R.id.nextMeter_button);
        Button nextSettingCreditButton = view.findViewById(R.id.nextSettingCredit_button);
        Button nextSettingAddressButton = view.findViewById(R.id.nextSettingAddress_button);
        Button nextProcedureRegisterButton = view.findViewById(R.id.nextProcedureRegister_button);
        Button nextInformationButton = view.findViewById(R.id.nextInformation_button);
        Button nextDeleteAccountButton = view.findViewById(R.id.nextDeleteAccount_button);

        // http通信
        Thread t = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/UserProfile.php";

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
                                // Jsonのキーを指定すれば対応する値が入る
                                profile_username.setText(jsnObject.getString("user_name"));
                                profile_message.setText(jsnObject.getString("user_profile_message"));
                                //profile_image.setText(jsnObject.getString("user_profile_image"));
                            } catch (JSONException e) {
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

        nextSettingProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_profile_config);
            }
        });

        nextMeterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_meter);
            }
        });

        nextSettingCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_payinfoconfig);
            }
        });

        nextSettingAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_shipping_addr_info_config);
            }
        });

        nextProcedureRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_producer_agreement);
            }
        });

        nextInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_information);
            }
        });

        nextDeleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_deletion_agreement);
            }
        });


        return view;
    }

}