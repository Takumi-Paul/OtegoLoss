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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ThemedSpinnerAdapter;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;


import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.ListViewAdapter;
import com.example.otegoloss.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class FavoriteUserView extends Fragment {

    // ユーザIDのデータ
    private SharedPreferences userIDData;
    String userID;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    private String[] favoriteUserNames;
    //private static final int[] photos = {
     //       R.drawable.user, R.drawable.user
    //};

    private String[] favoriteIDs;

    // 画像URL
    private String[] imgURL;

    private List<Bitmap> imgList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_view_favorite_user, container, false);

        // userIDを取得
        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        if (userID == "error") {
            userID = "u0000001";
        }
        System.out.println(userID);

        // http通信
        Thread t = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/favorite.php";

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

                            JSONObject jsnObject = ConnectionJSON.ChangeJson(str);
                            // Jsonのキーを指定すれば対応する値が入る
                            // productNameTextView.setText(jsnObject.getString("product_name"));
                            List<String> favoriteIDList = ConnectionJSON.ChangeArrayJSON(str, "favorite_user_id");
                            favoriteIDs = favoriteIDList.toArray(new String[favoriteIDList.size()]);
                            List<String> favoriteList = ConnectionJSON.ChangeArrayJSON(str, "user_name");
                            favoriteUserNames = favoriteList.toArray(new String[favoriteList.size()]);
                            List<String> imgStrList = ConnectionJSON.ChangeArrayJSON(str, "user_profile_image");
                            imgURL = imgStrList.toArray(new String[imgStrList.size()]);

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }
        });

        Thread t_img = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < imgURL.length; i++) {
                    // phpファイルまでのリンク
                    URL img_url = null;
                    try {
                        img_url = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/" + imgURL[i]);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    System.out.println(img_url);
                    Bitmap bmp = ConnectionJSON.downloadImage(img_url);
                    Bitmap normalBmp = BitmapFactory.decodeResource(getResources(), R.drawable.box);
                    if (bmp != null) {
                        imgList.add(bmp);
                    } else {
                        imgList.add(normalBmp);
                    }
                    System.out.println("connect");
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressDialog.dismiss();
                        settingUI(view);
                    }
                });
            }
        });

        try {
            t.start();
            t.join();
            t_img.start();
            t_img.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return view;
    }

    public void settingUI(View view) {

        ListView favoriteUserList = view.findViewById(R.id.favoriteUser_list);
        BaseAdapter arrayAdapter = new com.example.otegoloss.ListViewAdapter(
                getActivity().getApplicationContext(), R.layout.list, favoriteUserNames, imgList);

        favoriteUserList.setAdapter(arrayAdapter);

        favoriteUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // bundleに受け渡したい値を保存
                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", favoriteIDs[position]);
                // Navigation遷移
                Navigation.findNavController(view).navigate(R.id.action_favoriteUserView_to_exhibitProfile, bundle);
            }
        });
    }


}
