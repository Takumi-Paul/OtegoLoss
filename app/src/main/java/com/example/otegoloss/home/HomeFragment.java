/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.GridView;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.GridAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.NewAccountActivity;
import com.example.otegoloss.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class HomeFragment extends Fragment {

    ProgressDialog progressDialog;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    // 商品名配列
    private String[] productNames;
    // 価格配列
    private int[] prices;
    // 商品ID
    private String[] productID;
    //生産者ID
    private String[] producerID;
    // 画像URL
    private String[] imgURL;

    private List<Bitmap> imgList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        this.progressDialog = NewAccountActivity.progressDialog;

        // http通信
        Thread t = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    URL url = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/HomeProduct.php");

                    System.out.println(url);
                    // 処理開始時刻
                    startTime = System.currentTimeMillis();
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    final String str = ConnectionJSON.InputStreamToString(con.getInputStream());

                    // 終了時刻
                    endTime = System.currentTimeMillis();
                    Log.d("HTTP", str);

                    // Jsonのキーを指定すれば対応する値が入る
                    //配列の取得
                    List<String> productNameList = ConnectionJSON.ChangeArrayJSON(str, "product_name");
                    productNames = productNameList.toArray(new String[productNameList.size()]);
                    List<String> priceList = ConnectionJSON.ChangeArrayJSON(str, "price");
                    String[] priceString = priceList.toArray(new String[priceList.size()]);
                    prices = Stream.of(priceString).mapToInt(Integer::parseInt).toArray();
                    List<String> produceIDList = ConnectionJSON.ChangeArrayJSON(str, "product_id");
                    productID = produceIDList.toArray(new String[produceIDList.size()]);
                    List<String> sellerIDList = ConnectionJSON.ChangeArrayJSON(str, "user_name");
                    producerID = sellerIDList.toArray(new String[sellerIDList.size()]);
                    List<String> imgStrList = ConnectionJSON.ChangeArrayJSON(str, "product_image");
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

        // ThreadTestクラスの処理が終了するまで待機の指示
        try {
            t.start();
            System.out.println("start");
            t.join();
            System.out.println("join");
            t_img.start();
            t_img.join();
        } catch (InterruptedException e) {
            // 例外処理
            e.printStackTrace();
        }
            return view;
        }

        public void settingUI(View view) {

            // 出品ボタン
            ImageButton shippingButton = (ImageButton) view.findViewById(R.id.shipping_button);
            shippingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_entry_of_exhibit_info);
                }
            });

            //商品一覧画面
            // GridViewのインスタンスを生成
            GridView gridview = view.findViewById(R.id.product_gridView);
            // BaseAdapter を継承したGridAdapterのインスタンスを生成
            // 子要素のレイアウトファイル grid_items.xml を
            // fragment_home.xml に inflate するためにGridAdapterに引数として渡す
            GridAdapter adapter = new GridAdapter(getActivity().getApplicationContext(),
                    R.layout.grid_items,
                    imgList,
                    productNames,
                    prices,
                    producerID
            );

            // gridViewにadapterをセット
            gridview.setAdapter(adapter);
            // item clickのListenerをセット
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // bundleに受け渡したい値を保存
                    Bundle bundle = new Bundle();
                    // 商品ID
                    bundle.putString("PRODUCT_ID", productID[position]);
                    // 購入からの遷移
                    bundle.putBoolean("PURCHASE", true);
                    // Navigation遷移
                    Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_fragmentProduct, bundle);
                }
            });

        }

}