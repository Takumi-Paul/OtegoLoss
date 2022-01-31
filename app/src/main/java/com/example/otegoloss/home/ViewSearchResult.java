/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.GridAdapter;
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

public class ViewSearchResult extends Fragment {

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

    String str;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        System.out.println("get");

        // BundleでHome画面の値を受け取り
        Bundle bundle = getArguments();

        // 商品
        String productName = bundle.getString("PRODUCT_NAME", "");
        String weight = bundle.getString("PRODUCT_WEIGHT","");
        String price = bundle.getString("PRODUCT_PRICE", "");
        String area = bundle.getString("PRODUCT_AREA", "");
        String delivery = bundle.getString("DELIVERY_METHOD", "");
        String seller = bundle.getString("SELLER_NAME", "");

        if (area == "産地") {
            area = "";
        }

        String hPrice = "";
        String lPrice = "";

        switch (price) {
            case "選択無し":
                hPrice = "";
                lPrice = "";
            case "0 ~ 1000":
                hPrice = "1000";
                lPrice = "0";
                break;
            case "1000 ~ 2000":
                hPrice = "2000";
                lPrice = "1000";
                break;
            case "2000 ~ 3000":
                hPrice = "2000";
                lPrice = "3000";
                break;
            case "3000 ~ 4000":
                hPrice = "3000";
                lPrice = "4000";
                break;
            case "4000 ~ 5000":
                hPrice = "4000";
                lPrice = "5000";
                break;
            case "5000 ~":
                hPrice = "5000";
                break;
            default:
                break;
        }

        String hWeight = "";
        String lWeight = "";

        switch (weight) {
            case "選択無し":
                hWeight = "";
                lWeight = "";
                break;
            case "0 ~ 1000":
                hWeight = "1000";
                lWeight = "0";
                break;
            case "1000 ~ 2000":
                hWeight = "2000";
                lWeight = "1000";
                break;
            case "2000 ~ 3000":
                hWeight = "2000";
                lWeight = "3000";
                break;
            case "3000 ~ 4000":
                hWeight = "3000";
                lWeight = "4000";
                break;
            case "4000 ~ 5000":
                hWeight = "4000";
                lWeight = "5000";
                break;
            case "5000 ~":
                hWeight = "5000";
                break;
            default:
                break;
        }

        String finalLPrice = lPrice;
        String finalHPrice = hPrice;
        String finalLWeight = lWeight;
        String finalHWeight = hWeight;
        String finalArea = area;

        // http通信
        Thread t = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    URL path = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/SearchProduct.php");

                    // クエリ文字列を連想配列に入れる
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("product_name", productName);
                    map.put("lprice", finalLPrice);
                    map.put("hprice", finalHPrice);
                    map.put("delivery_meth", delivery);
                    map.put("lweight", finalLWeight);
                    map.put("hweight", finalHWeight);
                    map.put("prefecture", finalArea);
                    map.put("user_name", seller);

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
                    str = ConnectionJSON.InputStreamToString(con.getInputStream());

                    System.out.println(str);

                    // 終了時刻
                    endTime = System.currentTimeMillis();
                    Log.d("HTTP", str);

                    if (str != null) {
                    // Jsonのキーを指定すれば対応する値が入る
                    //配列の取得
                    List<String> productNameList = ConnectionJSON.ChangeArrayJSON(str, "product_name");
                    productNames = productNameList.toArray(new String[productNameList.size()]);
                    List<String> priceList = ConnectionJSON.ChangeArrayJSON(str, "price");
                    String[] priceString = priceList.toArray(new String[priceList.size()]);
                    prices = Stream.of(priceString).mapToInt(Integer::parseInt).toArray();
                    List<String> produceIDList = ConnectionJSON.ChangeArrayJSON(str, "product_id");
                    productID = produceIDList.toArray(new String[produceIDList.size()]);
                    List<String> sellerIDList = ConnectionJSON.ChangeArrayJSON(str, "seller_id");
                    producerID = sellerIDList.toArray(new String[sellerIDList.size()]);
                    List<String> imgStrList = ConnectionJSON.ChangeArrayJSON(str, "product_image");
                    imgURL = imgStrList.toArray(new String[imgStrList.size()]);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }
        });

        Thread t_img = new Thread(new Runnable() {
            @Override
            public void run() {
                if (imgURL != null) {
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
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressDialog.dismiss();
                        if (str != null) {
                            settingUI(view);
                        } else {
                            Toast.makeText(view.getContext(), "検索結果がありません", Toast.LENGTH_LONG).show();
                        }
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


        //商品一覧画面
        // GridViewのインスタンスを生成
        GridView gridview = view.findViewById(R.id.search_result_gridView);
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
                // Navigation遷移
                Navigation.findNavController(view).navigate(R.id.action_result_to_fragmentProduct, bundle);
            }
        });

    }


}