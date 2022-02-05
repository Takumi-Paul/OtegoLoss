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

    // http通信で返ってくる値
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

        //登録ボタンが押された時の処理
        Button registerButton  = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //商品名と出品者名, パスワードを取得
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
        } else if (mailAddressString.length() < 15) {
            Toast.makeText(this, "メールアドレスは15文字以上にしてください", Toast.LENGTH_LONG).show();
        } else if (userNameString.length() < 2 || userNameString.length() > 10) {
            Toast.makeText(this, "ユーザ名は2文字以上、10文字以下にしてください", Toast.LENGTH_LONG).show();
        } else if (!passwordString.equals(rePasswordString)) {
            Toast.makeText(this, "パスワードが間違っています", Toast.LENGTH_LONG).show();
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
                        URL path = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/InsertAccount.php");

                        // POSTで送るStringデータ
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

                        System.out.println(postData);
                        // サーバとパイプをつなぐ
                        OutputStream os = con.getOutputStream();
                        PrintStream ps = new PrintStream(os);
                        // リクエストパラメータを送信する
                        ps.write(postData.getBytes());
                        // ファイルを書き込む
                        ps.flush();
                        // ファイルを閉じる
                        ps.close();

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

                System.out.println(str);
                // 返ってくるuser_idの先頭がuなら
                if (str.startsWith("u")) {

                    // "userIDData"という名前でインスタンスを生成
                    userIDData = getSharedPreferences("DataStore", MODE_PRIVATE);
                    // 入力文字列を"userID"に書き込む
                    SharedPreferences.Editor editor = userIDData.edit();
                    editor.putString("userID", str);
                    // userIDを端末内に保存
                    editor.commit();

                    // HOME画面に遷移
                    Intent accountIntent = new Intent(this, MainActivity.class);
                    startActivity(accountIntent);

                    // ダイアログ表示
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("ロード中");
                    progressDialog.setMessage("処理しています");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                } else {
                    // エラー処理
                    Toast.makeText(this, "登録できませんでした", Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }



    }
}