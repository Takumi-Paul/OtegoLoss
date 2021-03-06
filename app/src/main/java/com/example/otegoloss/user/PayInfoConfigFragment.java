package com.example.otegoloss.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.databinding.FragmentUserBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class PayInfoConfigFragment extends Fragment {

    public TextView creditcard_company1;
    public TextView creditcard_number1;
    public TextView creditcard_company2;
    public TextView creditcard_number2;

    private String[] creditcard_companies;
    private String[] creditcard_numbers;
    private String[] cardID;

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
        View view = inflater.inflate(R.layout.fragment_view_pay_info_config, container, false);

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000003";
        }
        System.out.println(userID);

        creditcard_company1 = view.findViewById(R.id.texiview_creditcard_company) ;
        creditcard_number1 = view.findViewById(R.id.textView_creditcard_number) ;
        creditcard_company2 = view.findViewById(R.id.texiview_creditcard_company2) ;
        creditcard_number2 = view.findViewById(R.id.textView_creditcard_number2) ;

        Button addCreditButton = view.findViewById(R.id.addCredit_button);
        Button deleteCreditButton = view.findViewById(R.id.deleteCredit_button);

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
                                List<String> credit_companyList = ConnectionJSON.ChangeArrayJSON(str, "card_comp");
                                List<String> creditcard_numberList = ConnectionJSON.ChangeArrayJSON(str, "card_number");
                                creditcard_companies = credit_companyList.toArray(new String[credit_companyList.size()]);
                                creditcard_numbers = creditcard_numberList.toArray(new String[creditcard_numberList.size()]);
                                creditcard_company1.setText(creditcard_companies[0]);
                                creditcard_number1.setText(creditcard_numbers[0]);
                                creditcard_company2.setText(creditcard_companies[1]);
                                creditcard_number2.setText(creditcard_numbers[1]);
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

        addCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String creditcard_company2s = creditcard_company2.getText().toString();
                if(creditcard_company2s.equals("NO DATA")) {
                    Navigation.findNavController(view).navigate(R.id.action_payinfoconfig_to_creditcard_regist);
                }else{
                    Toast.makeText(view.getContext(), "登録の上限に達しています", Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_payinfoconfig_to_pay_info_delete);
            }
        });

        return view;
    }

}
