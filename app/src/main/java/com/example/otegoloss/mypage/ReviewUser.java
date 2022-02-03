/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.mypage;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.R;
import com.example.otegoloss.shipping.ViewYetSoldOutHistoryFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ReviewUser extends Fragment {

    public EditText review_comment;
    public ImageButton starImage1;
    public ImageButton starImage2;
    public ImageButton starImage3;
    public ImageButton starImage4;
    public ImageButton starImage5;
    public Button completeButton;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;
    String userID;

    int starCount = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_review, container, false);

        Bundle bundle =  getArguments();

        String sellerID = bundle.getString("SELLER_ID", "");

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000003";
        }
        System.out.println(userID);

        review_comment = view.findViewById(R.id.editTextComment) ;
        starImage1 = view.findViewById(R.id.rStar1) ;
        starImage2 = view.findViewById(R.id.rStar2);
        starImage3 = view.findViewById(R.id.rStar3);
        starImage4 = view.findViewById(R.id.rStar5);
        starImage5 = view.findViewById(R.id.rStar4);
        completeButton = view.findViewById(R.id.review_finish_button);

        starImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starImage1.setImageResource(R.drawable.star);
                starImage2.setImageResource(R.drawable.star_empty);
                starImage3.setImageResource(R.drawable.star_empty);
                starImage4.setImageResource(R.drawable.star_empty);
                starImage5.setImageResource(R.drawable.star_empty);


                starCount = 1;
            }
        });
        starImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starImage1.setImageResource(R.drawable.star);
                starImage2.setImageResource(R.drawable.star);
                starImage3.setImageResource(R.drawable.star_empty);
                starImage4.setImageResource(R.drawable.star_empty);
                starImage5.setImageResource(R.drawable.star_empty);
                starCount = 2;
            }
        });
        starImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starImage1.setImageResource(R.drawable.star);
                starImage2.setImageResource(R.drawable.star);
                starImage3.setImageResource(R.drawable.star);
                starImage4.setImageResource(R.drawable.star_empty);
                starImage5.setImageResource(R.drawable.star_empty);
                starCount = 3;
            }
        });
        starImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starImage1.setImageResource(R.drawable.star);
                starImage2.setImageResource(R.drawable.star);
                starImage3.setImageResource(R.drawable.star);
                starImage4.setImageResource(R.drawable.star);
                starImage5.setImageResource(R.drawable.star_empty);
                starCount = 4;
            }
        });
        starImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starImage1.setImageResource(R.drawable.star);
                starImage2.setImageResource(R.drawable.star);
                starImage3.setImageResource(R.drawable.star);
                starImage4.setImageResource(R.drawable.star);
                starImage5.setImageResource(R.drawable.star);
                starCount = 5;
            }
        });


        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment = review_comment.getText().toString();
                if (comment.equals("")) {
                    comment = "コメントなし";
                }

                // http通信
                String finalComment = comment;
                Thread t = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            // phpファイルまでのリンク
                            URL path = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/InsertReview.php");

                            // POSTで送るStringデータ
                            String postData = "user_id=" + userID +
                                    "&review_user_id=" + sellerID +
                                    "&assessment=" +  Integer.toString(starCount) +
                                    "&comment=" + finalComment;

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


                            final String str = ConnectionJSON.InputStreamToString(con.getInputStream());

                            // 終了時刻
                            endTime = System.currentTimeMillis();
                            Log.d("HTTP", str);

                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(e);
                        }
                    }
                });

                try {
                    t.start();
                    t.join();
                    Toast.makeText(view.getContext(), "レビューしました", Toast.LENGTH_LONG);


                    Navigation.findNavController(view).popBackStack();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

}