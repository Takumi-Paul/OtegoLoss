package com.example.otegoloss.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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

public class ProducerInfoFragment extends Fragment {

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;
    String userID;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    private String BankName;
    private String BlanchName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_producer_information, container, false);

        // メアドの定義
        EditText proEmailAddressEditText = (EditText) view.findViewById(R.id.proEmailAddress_editText);
        // 文字数制限(30文字)
        InputFilter[] proEmailAddressEditTextfilters = new InputFilter[1];
        proEmailAddressEditTextfilters[0] = new InputFilter.LengthFilter(30);
        // 半角英大文字のみ
        InputFilter proEmailAddressEditTextinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[a-zA-Z]+$")) {
                    proEmailAddressEditText.setFilters(proEmailAddressEditTextfilters);

                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        proEmailAddressEditText.setFilters(new InputFilter[]{proEmailAddressEditTextinputFilter});

        // 電話番号の定義
        EditText phoneNumberEditText= (EditText) view.findViewById(R.id.phoneNumber_editText);
        // 文字数制限(11桁)
        InputFilter[] phoneNumberEditTextTextfilters = new InputFilter[1];
        phoneNumberEditTextTextfilters[0] = new InputFilter.LengthFilter(11);
        // 数字のみ
        InputFilter phoneNumberEditTextinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[0-9]+$")) {
                    phoneNumberEditText.setFilters(phoneNumberEditTextTextfilters);

                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        phoneNumberEditText.setFilters(new InputFilter[]{phoneNumberEditTextinputFilter});


        // メアドの定義
        EditText accountNameEditText = (EditText) view.findViewById(R.id.accountName_editText);
        // 文字数制限(30文字)
        InputFilter[] accountNameEditTextTextfilters = new InputFilter[1];
        accountNameEditTextTextfilters[0] = new InputFilter.LengthFilter(30);
        // カタカナのみ
        InputFilter accountNameEditTextTextinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[ア-ン]+$")) {
                    accountNameEditText.setFilters(accountNameEditTextTextfilters);

                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        accountNameEditText.setFilters(new InputFilter[]{accountNameEditTextTextinputFilter});

        //銀行のSpinner処理
        Spinner bank_spinner = (Spinner)view.findViewById(R.id.choiceBank_spinner);
        bank_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter,
                                       View v, int position, long id) {
                String bank = (String)adapter.getSelectedItem();
                BankName = bank;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner bank_blanch_spinner = (Spinner)view.findViewById(R.id.choiceBranch_spinner);
        bank_blanch_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter,
                                       View v, int position, long id) {
                String bank = (String)adapter.getSelectedItem();
                BlanchName = bank;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000003";
        }
        System.out.println(userID);

        Button registerButton = view.findViewById(R.id.proRegister_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // http通信
                Thread t = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            // phpファイルまでのリンク
                            String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/InsertProducer.php";

                            // クエリ文字列を連想配列に入れる
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("user_id", userID);
                            map.put("tel_number", phoneNumberEditText.getText().toString());
                            map.put("bank_name", BankName);
                            map.put("bank_branch_name", BlanchName);
                            map.put("bank_number", "000");
                            map.put("bank_account_name",accountNameEditText.getText().toString());
                            map.put("id_image", "");

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

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

}
