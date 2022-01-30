/*
出品関連、出品情報確認画面
 */
package com.example.otegoloss.shipping;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Base64;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class ViewExhibitProductInfoConfirmationFragment extends Fragment {

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    // http通信で返ってくる値
    String str;

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_exhibit_info_confirmation, container, false);

        //商品名
        TextView product_name_view = view.findViewById(R.id.product_name_confirmation);
        String productName = getArguments().getString("PRODUCT_NAME");
        product_name_view.setText(productName);
        //商品説明
        TextView product_desc_view= view.findViewById(R.id.productInfo_confirmation);
        String productDesk = getArguments().getString("PRODUCT_DESCRIPTION");
        product_desc_view.setText(productDesk);
        //重さ
        TextView product_weight_view= view.findViewById(R.id.weight_confirmation);
        String productWeight = getArguments().getString("PRODUCT_WEIGHT");
        product_weight_view.setText(productWeight);
        //金額
        TextView product_price_view= view.findViewById(R.id.price_confirmation);
        String price = getArguments().getString("PRODUCT_PRICE");
        product_price_view.setText(price);
        //レシピURL
        TextView recipe_url_view= view.findViewById(R.id.recipe_url_confirmation);
        String recipeURL = getArguments().getString("RECIPE_URL");
        recipe_url_view.setText(recipeURL);
        //産地
        TextView product_area= view.findViewById(R.id.product_area_confirmation);
        String area = getArguments().getString("PRODUCT_AREA");
        product_area.setText(area);
        //配達方法
        TextView delivery_method= view.findViewById(R.id.delivery_confirmation);
        String delivery = getArguments().getString("DELIVERY_METHOD");
        delivery_method.setText(delivery);
        // 画像
        ImageView product_img = view.findViewById(R.id.product_imageView_confirmation);
        String imageString = getArguments().getString("PRODUCT_IMAGE");
        Bitmap bitmap = decodeBase64(imageString);

        product_img.setImageBitmap(bitmap);

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000003";
        }
        System.out.println(userID);


        // 確認完了ボタンを取得
        Button nextButton = view.findViewById(R.id.next_button_exhibit_info_com);
        // 確認完了ボタンをクリックした時の処理
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // http通信
                Thread t = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            // phpファイルまでのリンク
                            URL path = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/InsertProduct.php");

                            // POSTで送るStringデータ
                            String postData = "product_name=" + productName +
                                    "&product_desc=" + productDesk +
                                    "&product_image=" + "" +
                                    "&recipe_url=" +  recipeURL+
                                    "&category=" + "野菜" +
                                    "&price=" + price +
                                     "&delivery_meth=" + delivery +
                                    "&weight=" + productWeight +
                                    "&prefecture=" + area +
                                    "&seller_id=" + userID;

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
                                System.out.println(String.valueOf(str));
                            }


                            URL imgUrl = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/upload.php");

                            uploadImage upimg = new uploadImage();
                            JSONObject jsnObject = new JSONObject(str);
                            System.out.println(jsnObject.getString("product_id"));
                            upimg.upload(bitmap, imgUrl, jsnObject.getString("product_id"));


                            // 終了時刻
                            endTime = System.currentTimeMillis();
                            Log.d("HTTP", str);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    System.out.println(endTime - startTime);

                                }
                            });

                        } catch (IOException | JSONException e) {
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
                // fragmentViewExhibitCompletedに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_confirmation_to_completed);
            }
        });
        //編集へ戻るボタンを取得
        Button backButton = view.findViewById(R.id.back_button_exhibit_info_com);
        // 編集へ戻るボタンをクリックした時の処理
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentViewExhibitInfoConfirmationに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_confirmation_to_entry);
            }
        });

        return view;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }



}