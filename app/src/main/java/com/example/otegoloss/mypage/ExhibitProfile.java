package com.example.otegoloss.mypage;

import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.ListViewAdapter;
import com.example.otegoloss.R;
import com.example.otegoloss.ReviewListViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class ExhibitProfile extends Fragment {

    public static ProgressDialog progressDialog;

    public ImageView profile_image;
    public TextView profile_username;
    public TextView profile_message;
    public TextView userId_textView;
    public ImageButton heartImage;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    // 画像関連
    String user_image_url;
    Bitmap imgBmp;

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;
    String userID;

    // お気に入りの判定値
    private String fav_bool;
    private Boolean heart_flag = false;

    // レビュー
    private String[] userArray;
    private int[] assessmentArray;
    private String[] commentArray;
    private String review_str;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_exhibit_profile, container, false);

        // BundleでFavoriteUser画面の値を受け取り
        Bundle bundle = getArguments();
        // 出品者ID
        String sellerID = bundle.getString("USER_ID", "");

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000003";
        }
        System.out.println(userID);


        profile_image = view.findViewById(R.id.userImage) ;
        profile_username = view.findViewById(R.id.userNameTextView) ;
        profile_message = view.findViewById(R.id.userMessageTextView) ;
        userId_textView = view.findViewById(R.id.userIDTextView);
        heartImage = view.findViewById(R.id.heart);

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
                    map.put("user_id", sellerID);
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

                    // 画像の受信
                    JSONObject jsnObject = ConnectionJSON.ChangeJson(str);
                    user_image_url = jsnObject.getString("user_profile_image");

                    // phpファイルまでのリンク
                    URL img_url = null;
                    try {
                        img_url = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/" + user_image_url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    System.out.println(img_url);
                    Bitmap bmp = ConnectionJSON.downloadImage(img_url);

                    URL fav_path = null;
                    try {
                        fav_path = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/CheckFavorite.php");
                        // クエリ文字列を連想配列に入れる
                        Map<String, String> fav_map = new HashMap<String, String>();
                        fav_map.put("user_id", userID);
                        fav_map.put("favorite_user_id", sellerID);
                        // クエリ文字列組み立て・URL との連結
                        StringJoiner fav_stringUrl = new StringJoiner("&", fav_path + "?", "");
                        for (Map.Entry<String, String> param: fav_map.entrySet()) {
                            fav_stringUrl.add(param.getKey() + "=" + param.getValue());
                        }
                        URL fav_url = new URL(fav_stringUrl.toString());
                        // 処理開始時刻
                        startTime = System.currentTimeMillis();
                        //お気に入りかの受信
                        HttpURLConnection fav_con =(HttpURLConnection)fav_url.openConnection();
                        fav_bool = ConnectionJSON.InputStreamToString(fav_con.getInputStream());

                        // 終了時刻
                        endTime = System.currentTimeMillis();
                        Log.d("HTTP", fav_bool);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    URL review_path = null;
                    try {
                        review_path = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/Review.php");
                        // クエリ文字列を連想配列に入れる
                        Map<String, String> review_map = new HashMap<String, String>();
                        review_map.put("user_id", sellerID);
                        // クエリ文字列組み立て・URL との連結
                        StringJoiner review_stringUrl = new StringJoiner("&", review_path + "?", "");
                        for (Map.Entry<String, String> param: review_map.entrySet()) {
                            review_stringUrl.add(param.getKey() + "=" + param.getValue());
                        }
                        URL review_url = new URL(review_stringUrl.toString());
                        System.out.println(review_url);
                        // 処理開始時刻
                        startTime = System.currentTimeMillis();
                        //レビューの受信
                        HttpURLConnection review_con =(HttpURLConnection)review_url.openConnection();
                        review_str = ConnectionJSON.InputStreamToString(review_con.getInputStream());
                        // Jsonのキーを指定すれば対応する値が入る
                        //配列の取得
                        List<String> userList = ConnectionJSON.ChangeArrayJSON(review_str, "user_name");
                        userArray = userList.toArray(new String[userList.size()]);
                        List<String> assList = ConnectionJSON.ChangeArrayJSON(review_str, "assessment");
                        String[] assString = assList.toArray(new String[assList.size()]);
                        assessmentArray = Stream.of(assString).mapToInt(Integer::parseInt).toArray();
                        List<String> commentList = ConnectionJSON.ChangeArrayJSON(review_str, "comment");
                        commentArray = commentList.toArray(new String[commentList.size()]);

                        // 終了時刻
                        endTime = System.currentTimeMillis();
                        Log.d("HTTP", fav_bool);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(String.valueOf(str));
                            System.out.println(endTime - startTime);

                            try {
                                // Jsonのキーを指定すれば対応する値が入る
                                profile_username.setText(jsnObject.getString("user_name"));
                                profile_message.setText(jsnObject.getString("user_profile_message"));
                                userId_textView.setText(sellerID);

                                ListView reviewList = view.findViewById(R.id.reviewList);
                                BaseAdapter arrayAdapter = new ReviewListViewAdapter(
                                        getActivity().getApplicationContext(), R.layout.review_list, userArray, assessmentArray, commentArray);

                                reviewList.setAdapter(arrayAdapter);

                                if (bmp == null) {
                                    Bitmap normalBmp = BitmapFactory.decodeResource(getResources(), R.drawable.user);
                                    imgBmp = normalBmp;
                                } else {
                                    imgBmp = bmp;
                                }
                                System.out.println(imgBmp);
                                System.out.println("connect");
                                profile_image.setImageBitmap(bmp);

                                if (fav_bool.equals("true")) {
                                    heartImage.setImageResource(R.drawable.pink_heart);
                                    heart_flag = true;
                                    System.out.println("heart.true");
                                } else if (fav_bool.equals("false")){
                                    heartImage.setImageResource(R.drawable.empty_heart);
                                    heart_flag = false;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (IOException | JSONException e) {
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

        heartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (heart_flag) {
                    // http通信
                    Thread t = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            try {
                                // phpファイルまでのリンク
                                String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/DeliteFavorite.php";

                                // クエリ文字列を連想配列に入れる
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("user_id", userID);
                                map.put("favorite_user_id", sellerID);
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
                                        heartImage.setImageResource(R.drawable.empty_heart);
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println(e);
                            }
                        }
                    });
                    try {
                        // ダイアログ表示
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("ロード中");
                        progressDialog.setMessage("処理しています");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        t.start();
                        t.join();
                        progressDialog.dismiss();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }

                } else {
                    // http通信
                    Thread t = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            try {
                                // phpファイルまでのリンク
                                String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/InsertFavorite.php";

                                // クエリ文字列を連想配列に入れる
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("user_id", userID);
                                map.put("favorite_user_id", sellerID);
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
                                        heartImage.setImageResource(R.drawable.pink_heart);
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println(e);
                            }
                        }
                    });
                    try {
                        // ダイアログ表示
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("ロード中");
                        progressDialog.setMessage("処理しています");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        t.start();
                        t.join();
                        progressDialog.dismiss();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        //「レビューする」を押したときの処理
        Button ReviewButton = view.findViewById(R.id.reviewButton);

        ReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle nbundle = new Bundle();
                nbundle.putString("SELLER_ID", sellerID);

                Navigation.findNavController(view).navigate(R.id.action_exhibitProfile_to_reviewUser, nbundle);
            }
        });

        return view;
    }
}
