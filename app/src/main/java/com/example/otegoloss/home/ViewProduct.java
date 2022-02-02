/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.otegoloss.ChangeBackgraund;
import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.R;
import com.example.otegoloss.shipping.ViewYetSoldOutHistoryFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ViewProduct extends Fragment {

    TextView productNameTextView;
    Button proNameTextView;
    TextView showWeightTextView;
    TextView priceTextView;
    TextView regionTextView;
    TextView exhibitDayTextView;
    ImageView productImage;
    TextView descriptionTextView;
    TextView urlTextView;
    TextView deliveryTextView;

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;
    String userID;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    // 出品者ID
    String sellerID;

    // 画像関連
    String user_image_url;
    Bitmap imgBmp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_product, container, false);
//        LinearLayout background_view = view.findViewById(R.id.background);
//        ChangeBackgraund.changeBackGround(background_view, userID);

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000001";
        }
        System.out.println(userID);


        // BundleでHome画面の値を受け取り
        Bundle bundle = getArguments();
        // 商品ID
        String productID = bundle.getString("PRODUCT_ID", "");
        // 画面遷移
        Boolean purchase = bundle.getBoolean("PURCHASE", true);

        // imageViewのIDを関連付けて画像を表示
        productImage = view.findViewById(R.id.productImage_imageView);

        productNameTextView = view.findViewById(R.id.productName_textView);
        proNameTextView = view.findViewById(R.id.proName_textView);
        proNameTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        showWeightTextView = view.findViewById(R.id.showWeight_textView);
        regionTextView = view.findViewById(R.id.region_textView);
        exhibitDayTextView = view.findViewById(R.id.exhibitDay_textView);
        descriptionTextView = view.findViewById(R.id.description);
        urlTextView = view.findViewById(R.id.recipe_URL);
        deliveryTextView = view.findViewById(R.id.delivery);
        priceTextView = view.findViewById(R.id.price_textView);

        // http通信
        Thread t = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/ProductDetails.php";

                    // クエリ文字列を連想配列に入れる
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("product_id", productID);
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
                    user_image_url = jsnObject.getString("product_image");

                    // phpファイルまでのリンク
                    URL img_url = null;
                    try {
                        img_url = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/" + user_image_url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    System.out.println(img_url);
                    imgBmp = ConnectionJSON.downloadImage(img_url);
                    System.out.println("connect");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(String.valueOf(str));
                            System.out.println(endTime - startTime);
                            try {

                                // Jsonのキーを指定すれば対応する値が入る
                                productNameTextView.setText(jsnObject.getString("product_name"));
                                sellerID = jsnObject.getString("user_name");
                                proNameTextView.setText(sellerID);
                                showWeightTextView.setText("重量: " + jsnObject.getString("weight") + "g");
                                regionTextView.setText("地域: " + jsnObject.getString("prefecture"));
                                exhibitDayTextView.setText("出品日: " + jsnObject.getString("listing_date"));
                                descriptionTextView.setText(jsnObject.getString("product_desc"));
                                urlTextView.setText(jsnObject.getString("recipe_url"));
                                deliveryTextView.setText("配達方法: " + jsnObject.getString("delivery_meth"));

                                priceTextView.setText(jsnObject.getString("price") + "円");

                                productImage.setImageBitmap(imgBmp);

                                //exhibitDayTextView.setText(jsnObject.getString("Listing_date"));

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Button buyButton = view.findViewById(R.id.buy_button);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle nextbundle = new Bundle();
                nextbundle.putString("USER_ID", userID);
                nextbundle.putString("PRODUCT_ID", productID);

                Navigation.findNavController(view).navigate(R.id.action_fragmentProduct_to_fragmentViewInputPayment2, nextbundle);
            }
        });


        Button commentButton = view.findViewById(R.id.comment_button);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(view).navigate(R.id.action_fragmentProduct_to_fragmentViewComment2);
            }
        });

        proNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle nextBundle = new Bundle();
                // 出品者ID
                nextBundle.putString("USER_ID", sellerID);
                Navigation.findNavController(view).navigate(R.id.action_fragmentProduct_to_exhibitProfile, nextBundle);
            }
        });


        return view;
    }


}