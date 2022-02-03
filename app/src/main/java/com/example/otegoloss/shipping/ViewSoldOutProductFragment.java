//出品商品詳細（完売済み）
package com.example.otegoloss.shipping;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


public class ViewSoldOutProductFragment extends Fragment {

    TextView productNameTextView;
    TextView productPriceTextView;
    TextView productAreaTextView;
    TextView listingDateTextView;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    // 画像関連
    String user_image_url;
    Bitmap imgBmp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_sold_out_product, container, false);

        // BundleでHome画面の値を受け取り
        Bundle bundle = getArguments();
        // 商品ID
        String productID = bundle.getString("PRODUCT_ID", "");

        // imageViewのIDを関連付けて画像を表示
        ImageView imageView = view.findViewById(R.id.product_image_view_sold_out_product);
        //imageView.setImageResource(imageId);

        //商品名を表示
        productNameTextView = view.findViewById(R.id.product_name_text_view_yet_sold_out_product);
        //価格
        productPriceTextView= view.findViewById(R.id.product_price_sold_out_product);
        //産地を表示
        productAreaTextView = view.findViewById(R.id.productArea_sold_out_product);
        //出品日を表示
        listingDateTextView = view.findViewById(R.id.listing_date_text_sold_out_product);

        // ボタンを取得
        Button nextButton = view.findViewById(R.id.delivery_procedure_button_view_sold_out_product);

        // http通信
        Thread t = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/ListingDetails.php";

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

                            JSONObject jsnObject = ConnectionJSON.ChangeJson(str);
                            try {

                                // Jsonのキーを指定すれば対応する値が入る
                                productNameTextView.setText(jsnObject.getString("product_name"));
                                productPriceTextView.setText(jsnObject.getString("price")+"円");
                                productAreaTextView.setText(jsnObject.getString("prefecture"));
                                listingDateTextView.setText(jsnObject.getString("listing_date"));
                                imageView.setImageBitmap(imgBmp);
                                if (jsnObject.getString("delivery_status").equals("1")) {
                                    nextButton.setVisibility(View.INVISIBLE);
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

        try{
            t.start();
            t.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 配達ボタンをクリックした時の処理
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // bundleに受け渡したい値を保存
                Bundle bundle = new Bundle();
                // 商品ID
                bundle.putString("PRODUCT_ID", productID);

                // fragmentDeriveryに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_navigation_sold_out_product_to_navigation_shipping_procedure, bundle);
            }
        });
        return view;
    }

}
