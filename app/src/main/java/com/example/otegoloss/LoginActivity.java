package com.example.otegoloss;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static ProgressDialog progressDialog;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    private EditText mailAddress;
    private EditText password;

    // http通信で返ってくる値
    String str;
    String correctPass;

    // ユーザIDを保存するメモリ
    private SharedPreferences userIDData;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mailAddress = (EditText) findViewById(R.id.loginEmailAddressEditText);
        password = (EditText) findViewById(R.id.loginPasswordAddressEditText);

        //登録ボタンが押された時の処理
        Button loginButton  = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //商品名と出品者名, パスワードを取得
        String mailAddressString = mailAddress.getText().toString();
        String passwordString = password.getText().toString();

        System.out.println(mailAddressString);
        System.out.println(passwordString);

        if (mailAddressString.equals("") || passwordString.equals("")) {
            Toast.makeText(this, "入力が足りません", Toast.LENGTH_LONG).show();
        } else if (mailAddressString.length() < 15) {
            Toast.makeText(this, "メールアドレスは15文字以上にしてください", Toast.LENGTH_LONG).show();
        } else if (passwordString.length() < 8 || passwordString.length() > 16) {
            Toast.makeText(this, "パスワードは8文字以上にしてください", Toast.LENGTH_LONG).show();
        } else {

            // http通信
            Thread t = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    try {
                        // phpファイルまでのリンク
                        URL path = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/ReturnPassFromEmail.php");

                        // クエリ文字列を連想配列に入れる
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("user_mail", mailAddressString);
                        // クエリ文字列組み立て・URL との連結
                        StringJoiner stringUrl = new StringJoiner("&", path + "?", "");
                        for (Map.Entry<String, String> param : map.entrySet()) {
                            stringUrl.add(param.getKey() + "=" + param.getValue());
                        }
                        URL url = new URL(stringUrl.toString());

                        System.out.println(path);
                        // 処理開始時刻
                        startTime = System.currentTimeMillis();
                        HttpURLConnection con = (HttpURLConnection) path.openConnection();
                        con.setRequestMethod("GET");
                        con.setUseCaches(false);// キャッシュ利用
                        con.setDoOutput(true);// リクエストのボディの送信を許可(GETのときはfalse,POSTのときはtrueにする)
                        con.setDoInput(true);// レスポンスのボディの受信を許可

                        // レスポンスコード確認
                        final int responseCode = con.getResponseCode();
                        System.out.println(responseCode);

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            // レスポンスコードが200ならStringに変換
                            str = ConnectionJSON.InputStreamToString(con.getInputStream());
                        }

                        // 終了時刻
                        endTime = System.currentTimeMillis();
                        Log.d("HTTP", str);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println(String.valueOf(str));
                                System.out.println(endTime - startTime);

                                JSONObject jsnObject = ConnectionJSON.ChangeJson(str);
                                try {
                                    correctPass = jsnObject.getString("user_password");
                                    userID = jsnObject.getString("user_id");
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
                // スレッド開始
                t.start();
                // スレッドが終わるまで他の処理を停止
                t.join();
                if (correctPass == passwordString) {
                    // "userIDData"という名前でインスタンスを生成
                    userIDData = getSharedPreferences("DataStore", MODE_PRIVATE);
                    // 入力文字列を"userID"に書き込む
                    SharedPreferences.Editor editor = userIDData.edit();
                    editor.putString("userID", userID);
                    // userIDを端末内に保存
                    editor.commit();

                    // HOME画面に遷移
                    Intent accountIntent = new Intent(this, MainActivity.class);
                    startActivity(accountIntent);
                } else {
                    Toast.makeText(this, "パスワードが誤っています", Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}