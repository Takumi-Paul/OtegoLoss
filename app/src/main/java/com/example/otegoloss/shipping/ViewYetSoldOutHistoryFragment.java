//出品履歴（未完売）
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
import com.example.otegoloss.GridAdapterOfYetSoldOut;
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


public class ViewYetSoldOutHistoryFragment extends Fragment {
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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_yet_sold_out_history, container, false);
        this.progressDialog   = NewAccountActivity.progressDialog;

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
                    prices = Stream.of(priceString).mapToInt(Integer::parseInt).toArray();
                    List<String> produceIDList = ConnectionJSON.ChangeArrayJSON(str, "product_id");
                    productID = produceIDList.toArray(new String[produceIDList.size()]);
                    List<String> listingDateList = ConnectionJSON.ChangeArrayJSON(str, "listing_date");
                    listingDate= listingDateList.toArray(new String[produceIDList.size()]);
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
                    imgList.add(ConnectionJSON.downloadImage(img_url));
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

        public void settingUI(View view){
            // GridViewのインスタンスを生成
            GridView gridview = view.findViewById(R.id.grid_view_yet_sold_out_history);
            // BaseAdapter を継承したGridAdapterのインスタンスを生成
            // 子要素のレイアウトファイル grid_items.xml を
            // fragment_view_yet_sold_out_history.xml に inflate するためにGridAdapterに引数として渡す
            GridAdapterOfYetSoldOut adapter = new GridAdapterOfYetSoldOut(getActivity().getApplicationContext(),
                    R.layout.grid_items_of_yet_history,
                    imgList,
                    productNames,
                    prices,
                    listingDate
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

                    Navigation.findNavController(view).navigate(R.id.action_navigation_yet_sold_out_history_to_navigation_yet_sold_out_product, bundle);
                }
            });

            // ボタン要素を取得
            Button buttonSoldOutHistory = view.findViewById(R.id.button_soldout_sold_out_history);
            // 完売済みボタンをクリックした時の処理
            buttonSoldOutHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // fragmentShippingに遷移させる
                    Navigation.findNavController(view).navigate(R.id.action_navigation_yet_sold_out_history_to_navigation_shipping);
                }
            });
    }
}


