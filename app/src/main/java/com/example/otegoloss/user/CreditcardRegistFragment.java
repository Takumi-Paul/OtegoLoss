package com.example.otegoloss.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


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

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class CreditcardRegistFragment extends Fragment {

    String CompName;
    String Month;
    String Year;

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
        View view = inflater.inflate(R.layout.fragment_view_creditcard_regist, container, false);

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000003";
        }
        System.out.println(userID);

        // クレカ名義人の定義
        EditText cardNameEditText = (EditText)view.findViewById(R.id.cardName_editText);

        // クレカ番号の定義
        EditText cardNumberEditText = (EditText)view.findViewById(R.id.cardNumber_editText);

        // セキュリティコードの定義
        EditText cardCodeEditText = (EditText)view.findViewById(R.id.cardCode_editText);

        Spinner compSpinner = (Spinner)view.findViewById(R.id.company_spinner);
        compSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter,
                                       View v, int position, long id) {
                String comp = (String)adapter.getSelectedItem();
                CompName = comp;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner monthSpinner = (Spinner)view.findViewById(R.id.month_spinner);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter,
                                       View v, int position, long id) {
                String month = (String)adapter.getSelectedItem();
                Month = month;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner yearSpinner = (Spinner)view.findViewById(R.id.year_spinner);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter,
                                       View v, int position, long id) {
                String year = (String)adapter.getSelectedItem();
                Year = year;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button inputCreditButton = view.findViewById(R.id.inputCredit_button);
        inputCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNameEditTexts = cardNameEditText.getText().toString();
                String cardNumberEditTexts = cardNumberEditText.getText().toString();
                String cardCodeEditTexts = cardCodeEditText.getText().toString();

                if(cardNameEditTexts.equals("") || cardNumberEditTexts.equals("") || cardCodeEditTexts.equals("")){
                    Toast.makeText(view.getContext(), "入力欄に不備があります", Toast.LENGTH_LONG).show();
                } else if(cardNameEditTexts.matches("^[A-Z]*$") && cardNumberEditTexts.matches("^[0-9]*$") && cardCodeEditTexts.matches("^[0-9]*$")) {

                    // http通信
                    Thread t = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            try {
                                // phpファイルまでのリンク
                                String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/InsertCredit.php";

                                // クエリ文字列を連想配列に入れる
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("user_id", userID);
                                map.put("card_number", cardNumberEditTexts);
                                map.put("security_number", cardCodeEditTexts);
                                map.put("card_comp", CompName);
                                map.put("nominee", cardNumberEditTexts);
                                map.put("validated_date", Month + Year);

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

                    try {
                        t.start();
                        t.join();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Navigation.findNavController(view).navigate(R.id.action_creditcard_regist_to_pay_info_regist_comp);
                } else {
                    Toast.makeText(view.getContext(), "入力欄に不備があります", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

}
