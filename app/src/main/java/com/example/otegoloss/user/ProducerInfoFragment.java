package com.example.otegoloss.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ProducerInfoFragment extends Fragment {

    private EditText proEmailAddressEditText;
    private EditText phoneNumberEditText;
    private EditText bankEditText;
    private EditText branchEditText;
    private EditText accountNameEditText;

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


        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000003";
        }
        System.out.println(userID);

        EditText proEmailAddressEditText = (EditText) view.findViewById(R.id.proEmailAddress_editText);
        EditText phoneNumberEditText = (EditText) view.findViewById(R.id.phoneNumber_editText);
        EditText bankEditText = (EditText) view.findViewById(R.id.bank_editText);
        EditText branchEditText = (EditText) view.findViewById(R.id.branch_editText);
        EditText accountNameEditText = (EditText) view.findViewById(R.id.accountName_editText);

        Button registerButton = view.findViewById(R.id.proRegister_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String proEmailAddressEditTexts = proEmailAddressEditText.getText().toString();
                String phoneNumberEditTexts = phoneNumberEditText.getText().toString();
                String bankEditTexts = bankEditText.getText().toString();
                String branchEditTexts = branchEditText.getText().toString();
                String accountNameEditTexts = accountNameEditText.getText().toString();

                if (proEmailAddressEditTexts.equals("") || phoneNumberEditTexts.equals("") || bankEditTexts.equals("") || branchEditTexts.equals("") || accountNameEditTexts.equals("")) {
                    Toast.makeText(view.getContext(), "入力欄に不備があります", Toast.LENGTH_LONG).show();
                } else if (proEmailAddressEditTexts.matches("^[a-zA-Z]*$") && accountNameEditTexts.matches("^[ァ-ヶｱ-ﾝﾞﾟ]*$")) {

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
                                map.put("bank_name", phoneNumberEditText.getText().toString());
                                map.put("bank_branch_name", bankEditText.getText().toString());
                                map.put("bank_number", "000");
                                map.put("bank_account_name", accountNameEditText.getText().toString());
                                map.put("id_image", "");

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

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return view;
    }

}
