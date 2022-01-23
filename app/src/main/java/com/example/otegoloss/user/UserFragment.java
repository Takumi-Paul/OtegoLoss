/*
12/29
Kobayashi
ユーザ画面を生成するプログラム
 */

package com.example.otegoloss.user;

import android.os.Bundle;
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

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
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
import java.util.Objects;

public class UserFragment extends Fragment {

    public ImageView profile_image;
    public TextView profile_username;
    public TextView profile_userid;
    public TextView profile_message;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        profile_image = view.findViewById(R.id.accountImage_imageView) ;
        profile_username = view.findViewById(R.id.profileUserName_textView) ;
        profile_userid = view.findViewById(R.id.profileUserID_textView) ;
        profile_message = view.findViewById(R.id.profileUserMessage_textView) ;


        Button nextSettingProfileButton = view.findViewById(R.id.nextSettingProfile_button);
        Button nextMeterButton = view.findViewById(R.id.nextMeter_button);
        Button nextSettingCreditButton = view.findViewById(R.id.nextSettingCredit_button);
        Button nextSettingAddressButton = view.findViewById(R.id.nextSettingAddress_button);
        Button nextProcedureRegisterButton = view.findViewById(R.id.nextProcedureRegister_button);
        Button nextInformationButton = view.findViewById(R.id.nextInformation_button);
        Button nextDeleteAccountButton = view.findViewById(R.id.nextDeleteAccount_button);

        // http通信
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/ProductDetails.php?product_id=g0000001");
                    // 処理開始時刻
                    startTime = System.currentTimeMillis();
                    HttpURLConnection con =(HttpURLConnection)url.openConnection();
                    final String str = InputStreamToString(con.getInputStream());

                    // 終了時刻
                    endTime = System.currentTimeMillis();
                    Log.d("HTTP", str);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(String.valueOf(str));
                            System.out.println(endTime - startTime);

                            JSONObject jsnObject = ChangeJson(str);
                            try {
                                // Jsonのキーを指定すれば対応する値が入る
                                //profile_image.setImageBitmap(jsnObject.getString("user_profile_image"));
                                profile_username.setText(jsnObject.getString("user_name"));
                                profile_userid.setText(jsnObject.getString("user_id"));
                                profile_message.setText(jsnObject.getString("user_profile_message"));
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
        }).start();

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

    // http通信で受け取ったデータをString化する
    static String InputStreamToString(InputStream is) throws IOException {
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
    static JSONObject ChangeJson(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            // JSONArray jsonArray = jsonObject.getJSONArray("sample");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                //Log.d("Check", jsonData.getString("user_profile_image"));
                Log.d("Check", jsonData.getString("user_name"));
                Log.d("Check", jsonData.getString("user_id"));
                Log.d("Check", jsonData.getString("user_profile_message"));
                return jsonData;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}