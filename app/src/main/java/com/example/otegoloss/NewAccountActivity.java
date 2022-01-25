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

public class NewAccountActivity extends AppCompatActivity implements View.OnClickListener {

    public static ProgressDialog progressDialog;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    private EditText mailAddress;
    private EditText userName;
    private EditText password;
    private EditText rePassword;

    String str;
    // ユーザIDを保存するメモリ
    private SharedPreferences userIDData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        mailAddress = (EditText) findViewById(R.id.emailAddressEditText);
        userName = (EditText) findViewById(R.id.userNameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        rePassword = (EditText) findViewById(R.id.rePasswordEditText);

        //検索ボタンが押された時の処理
        Button registerButton  = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //商品名と出品者名を取得
        String mailAddressString = mailAddress.getText().toString();
        String userNameString = userName.getText().toString();
        String passwordString = password.getText().toString();
        String rePasswordString = rePassword.getText().toString();

        System.out.println(mailAddressString);
        System.out.println(passwordString);
        System.out.println(rePasswordString);

        if (mailAddressString.equals("") || userNameString.equals("") || passwordString.equals("") ||
                rePasswordString .equals("")) {
            Toast.makeText(this, "入力が足りません", Toast.LENGTH_LONG).show();
        } else if (passwordString.equals(rePasswordString)) {
            Toast.makeText(this, "パスワードが間違っています", Toast.LENGTH_LONG).show();
        } else if (passwordString.length() < 8) {
            Toast.makeText(this, "パスワードは8文字以上にしてください", Toast.LENGTH_LONG).show();
        } else {
            // http通信
            Thread t = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    try {
                        // phpファイルまでのリンク
                        URL path = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/InsertAccount.php");


                        // クエリ文字列を連想配列に入れる
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("user_password", passwordString);
                        map.put("user_name", userNameString);
                        map.put("user_mail", mailAddressString);
                        map.put("gross_weight", 0);
                        map.put("user_profile_image", "");
                        map.put("user_profile_message", "");
                        // クエリ文字列組み立て・URL との連結
//                        StringJoiner stringUrl = new StringJoiner("&", path + "?", "");
//                        for (Map.Entry<String, String> param: map.entrySet()) {
//                            stringUrl.add(param.getKey() + "=" + param.getValue());
//                        }
//                        URL url = new URL(stringUrl.toString());

                        String postData = "user_password=" + passwordString +
                                "&user_name=" + userNameString +
                                "&user_mail=" + mailAddressString +
                                "&gross_weight=" + 0 +
                                "&user_profile_image=" + "" +
                                "&user_profile_message=" + "";


                        System.out.println(path);
                        // 処理開始時刻
                        startTime = System.currentTimeMillis();
                        HttpURLConnection con =(HttpURLConnection)path.openConnection();
                        con.setRequestMethod("POST");
                        con.setUseCaches(false);// キャッシュ利用
                        con.setDoOutput(true);// リクエストのボディの送信を許可(GETのときはfalse,POSTのときはtrueにする)
                        con.setDoInput(true);// レスポンスのボディの受信を許可
                        //con.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                        System.out.println(postData);
                        OutputStream os = con.getOutputStream();
                        PrintStream ps = new PrintStream(os);
                        ps.write(postData.getBytes());
                        ps.close();

                        con.connect();

                        final int responseCode = con.getResponseCode();
                        System.out.println(responseCode);

                        if (responseCode == HttpURLConnection.HTTP_OK) {
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

                                //JSONObject jsnObject = ConnectionJSON.ChangeJson(str);
                                //try {
                                    System.out.println(str);

                                //}

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

                // 返ってくるuser_idの先頭がuなら
                if (str.startsWith("u")) {

                    // "userIDData"という名前でインスタンスを生成
                    userIDData = getSharedPreferences("DataStore", MODE_PRIVATE);
                    // 入力文字列を"input"に書き込む
                    SharedPreferences.Editor editor = userIDData.edit();
                    editor.putString("userID", str);
                    editor.commit();

                    Intent accountIntent = new Intent(this, MainActivity.class);
                    startActivity(accountIntent);

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("処理しています");
                    progressDialog.setMessage("メッセージ");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                } else {
                    Toast.makeText(this, "登録できませんでした", Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }



    }
}