/*
12/29
Kobayashi
出品者画面を生成するプログラム
 */

package com.example.otegoloss.shipping;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.GridAdapter;
import com.example.otegoloss.GridAdapterOfShipping;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.NewAccountActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.home.ViewProduct;

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

public class ShippingFragment extends Fragment {
    // 商品名配列
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
    //出品日
    private String[]  listingDate;
    // 画像URL
    private String[] imgURL;
    // 画像のBitMap
    private Bitmap[] bmps = new Bitmap[7];
    private List<Bitmap> imgList = new ArrayList<>();

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;
    // ユーザID
    String userID;
    //購入済
    private String[] purchase;

    //以下Sort用配列
    //SoldOut用配列
    // 商品名配列
    private String[] productNames_S;
    // 価格配列
    private int[] prices_S;
    // 商品ID
    private String[] productID_S;
    //出品日
    private String[]  listingDate_S;
    // 画像URL
    private String[] imgURL_S;
    // 画像のBitMap
    private List<Bitmap> imgList_S = new ArrayList<>();

    //YetSoldOut用配列
    // 商品名配列
    private String[] productNames_Y;
    // 価格配列
    private int[] prices_Y;
    // 商品ID
    private String[] productID_Y;
    //出品日
    private String[]  listingDate_Y;
    // 画像URL
    private String[] imgURL_Y;
    // 画像のBitMap
    private List<Bitmap> imgList_Y = new ArrayList<>();

    //画面表示
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);
        this.progressDialog = NewAccountActivity.progressDialog;
        //ユーザ情報取得
        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        if (userID == "error") {
            userID = "u0000005";
        }
        System.out.println(userID);

        // http通信
        Thread t = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/ListingList.php";

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
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    final String str = ConnectionJSON.InputStreamToString(con.getInputStream());

                    // 終了時刻
                    endTime = System.currentTimeMillis();
                    Log.d("HTTP", str);
                    System.out.println(String.valueOf(str));
                    System.out.println(endTime - startTime);

                    // Jsonのキーを指定すれば対応する値が入る
                    //配列の取得
                    List<String> productNameList = ConnectionJSON.ChangeArrayJSON(str, "product_name");
                    productNames = productNameList.toArray(new String[productNameList.size()]);
                    List<String> priceList = ConnectionJSON.ChangeArrayJSON(str, "price");
                    String[] priceString = priceList.toArray(new String[priceList.size()]);
                    List<String> productIDList = ConnectionJSON.ChangeArrayJSON(str, "product_id");
                    productID = productIDList.toArray(new String[productIDList.size()]);
                    List<String> listingDateList = ConnectionJSON.ChangeArrayJSON(str, "listing_date");
                    listingDate= listingDateList.toArray(new String[listingDateList.size()]);
                    List<String> imgStrList = ConnectionJSON.ChangeArrayJSON(str, "product_image");
                    imgURL = imgStrList.toArray(new String[imgStrList.size()]);
                    List<String> purchaseList = ConnectionJSON.ChangeArrayJSON(str, "purchased");
                    purchase = purchaseList.toArray(new String[purchaseList.size()]);


                    List<String> productNames_SList = new ArrayList<>();
                    List<String> price_SList = new ArrayList<>();
                    List<String> productID_SList = new ArrayList<>();
                    List<String> listingDate_SList = new ArrayList<>();
                    List<String> imgStr_SList = new ArrayList<>();

                    List<String> productNames_YList = new ArrayList<>();
                    List<String> price_YList = new ArrayList<>();
                    List<String> productID_YList = new ArrayList<>();
                    List<String> listingDate_YList = new ArrayList<>();
                    List<String> imgStr_YList = new ArrayList<>();

                    System.out.println(productNames.length);
                    System.out.println(purchase.length);
                    for (int i = 0; i< purchase.length; i++){
                        if(purchase[i].equals("1")){
                            productNames_SList.add(productNames[i]);
                            // 価格配列
                            price_SList.add(priceString[i]);
                            // 商品ID
                            productID_SList.add(productID[i]);
                            //出品日
                            listingDate_SList.add(listingDate[i]);
                            // 画像URL
                            imgStr_SList.add(imgURL[i]);
                        } else {
                            productNames_YList.add(productNames[i]);
                            // 価格配列
                            price_YList.add(priceString[i]);
                            // 商品ID
                            productID_YList.add(productID[i]);
                            //出品日
                            listingDate_YList.add(listingDate[i]);
                            // 画像URL
                            imgStr_YList.add(imgURL[i]);
                        }
                    }

                    productNames_S = productNames_SList.toArray(new String[productNames_SList.size()]);
                    String[] priceString_S = price_SList.toArray(new String[price_SList.size()]);
                    prices_S = Stream.of(priceString_S).mapToInt(Integer::parseInt).toArray();
                    productID_S = productID_SList.toArray(new String[productID_SList.size()]);
                    listingDate_S = listingDate_SList.toArray(new String[listingDate_SList.size()]);
                    imgURL_S = imgStr_SList.toArray(new String[imgStr_SList.size()]);

                    productNames_Y = productNames_YList.toArray(new String[productNames_YList.size()]);
                    String[] priceString_Y = price_YList.toArray(new String[price_YList.size()]);
                    prices_Y = Stream.of(priceString_Y).mapToInt(Integer::parseInt).toArray();
                    productID_Y = productID_YList.toArray(new String[productID_YList.size()]);
                    listingDate_Y = listingDate_YList.toArray(new String[listingDate_YList.size()]);
                    imgURL_Y = imgStr_YList.toArray(new String[imgStr_YList.size()]);

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }
        });

        Thread t_img = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < imgURL_S.length; i++) {
                    // phpファイルまでのリンク
                    URL img_url = null;
                    try {
                        img_url = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/" + imgURL[i]);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    System.out.println(img_url);
                    imgList_S.add(ConnectionJSON.downloadImage(img_url));
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
        // ボタン要素を取得
        Button buttonYetSoldOutHistory = (Button) view.findViewById(R.id.button_yetsoldout_sold_out_history);
        // 完売済みボタンをクリックした時の処理
        buttonYetSoldOutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragment_view_yet_sold_out_historyに遷移させる
                Bundle bundle =new Bundle();

                bundle.putStringArray("PRODUCT_ID", productID_Y);
                bundle.putStringArray("PRODUCT_NAME", productNames_Y);
                bundle.putIntArray("PRICE", prices_Y);
                bundle.putStringArray("LISTING_DATE",listingDate_Y);
                bundle.putStringArray("IMG_URL",imgURL_Y);

                Navigation.findNavController(view).navigate(R.id.action_navigation_shipping_to_navigation_yet_sold_out_history, bundle);
            }
        });

        // GridViewのインスタンスを生成
        GridView gridview = view.findViewById(R.id.grid_view_sold_out_history);
        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル grid_items.xml を
        // fragment_home.xml に inflate するためにGridAdapterに引数として渡す
        GridAdapterOfShipping adapter = new GridAdapterOfShipping(getActivity().getApplicationContext(),
                R.layout.grid_items_of_shipping,
                imgList_S,
                productNames_S,
                prices_S,
                listingDate_S
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
                bundle.putString("PRODUCT_ID", productID_S[position]);

                Navigation.findNavController(view).navigate(R.id.action_navigation_shipping_to_navigation_sold_out_product, bundle);
            }
        });

    }
}