package com.example.otegoloss.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;


import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.databinding.FragmentUserBinding;
import com.example.otegoloss.shipping.uploadImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class ProfileConfigFragment extends Fragment {

    private EditText config_username;
    private EditText config_usermessage;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;
    String userID;
    String password;
    String mail;
    String weight;
    String name;
    String message;

    String str;

    // 画像の取り込み
    private ImageView input_image;
    private ImageButton input_button;

    // 画像ファイルを開く
    private ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            input_image.setImageURI(result);
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_profile_config, container, false);

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000003";
        }
        System.out.println(userID);

        // ユーザ名の定義
        config_username = (EditText)view.findViewById(R.id.userName_editText) ;

        // プロフィールメッセージの定義
        config_usermessage = (EditText)view.findViewById(R.id.profileMessage_editText) ;

        // ユーザ画像
        input_image = (ImageView) view.findViewById(R.id.account_imageView);
        input_button = (ImageButton) view.findViewById(R.id.inputAccount_imageButton);


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
                                password = jsnObject.getString("user_password");
                                mail = jsnObject.getString("user_mail");
                                weight = jsnObject.getString("gross_weight");
                                name = jsnObject.getString("user_name");
                                message = jsnObject.getString("user_profile_message");

                                config_username.setText(name);
                                config_usermessage.setText(message);

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 画像ファイルを開く
        input_button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        }));


        Button inputCompleteButton = view.findViewById(R.id.inputComplete_button);
        inputCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String config_usernames = config_username.getText().toString();

                String config_usermessages = config_usermessage.getText().toString();

                // 画像ファイル取得
                Bitmap bitmap = ((BitmapDrawable)input_image.getDrawable()).getBitmap();
                byte[] byteArray = encodeImage(bitmap);

                // http通信
                Thread t = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            // phpファイルまでのリンク
                            URL path = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/UpdateProfile.php");

                            // POSTで送るStringデータ
                            String postData = "user_id=" + userID+
                                    "&user_password=" +  password+
                                    "&user_name=" + config_usernames +
                                    "&user_mail=" + mail +
                                    "&gross_weight=" + weight +
                                    "&user_profile_image=" + "" +
                                    "&user_profile_message=" + config_usermessages;

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
                            URL imgUrl = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/upload.php");

                            uploadImage upimg = new uploadImage();
                            upimg.upload(bitmap, imgUrl, userID);

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
                    // スレッド開始
                    t.start();
                    // スレッドが終わるまで他の処理を停止
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Navigation.findNavController(view).navigate(R.id.action_profile_config_to_navigation_user);
            }
        });

        return view;
    }

    private byte[] encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b = baos.toByteArray();
        //String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return b;
    }

}
