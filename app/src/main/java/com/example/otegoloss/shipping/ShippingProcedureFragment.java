package com.example.otegoloss.shipping;

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
import android.widget.Button;
import android.widget.TextView;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ShippingProcedureFragment extends Fragment {
    //購入者
    //private String purchaser_name = "高知太郎";
    //購入品
    //private String product_name = "tomato";
    //住所
    //private String address = "高知市万々111111-11111-2--1212111";

    TextView purchaserNameTextView;
    TextView telNumberTextView;
    TextView postalCodeTextView;
    TextView addressTextView;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_shipping_procedure, container, false);

        //商品名を表示
        purchaserNameTextView = view.findViewById(R.id.purchase_name_view_shipping_product);
        //電話番号を表示
        telNumberTextView = view.findViewById(R.id.tel_number_view_shipping_product);
        //住所番号を表示
        postalCodeTextView = view.findViewById(R.id.postal_number_shipping_product);
        //住所を表示
        addressTextView = view.findViewById(R.id.address_view_shipping_product);

        // ボタン要素を取得
        Button nextButton = view.findViewById(R.id.shipment_comp_button_view_shipping_product);

        // BundleでHome画面の値を受け取り
        Bundle bundle = getArguments();
        // 商品ID
        String productID = bundle.getString("PRODUCT_ID", "");

        // http通信
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/ShippingInfo.php";

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
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    final String str = ConnectionJSON.InputStreamToString(con.getInputStream());

                    // 終了時刻
                    endTime = System.currentTimeMillis();
                    Log.d("HTTP", str);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(String.valueOf(str));
                            System.out.println(endTime - startTime);

                            JSONObject jsnObject = ConnectionJSON.ChangeJson(str);
                            try {
                                // Jsonのキーを指定すれば対応する値が入る
                                purchaserNameTextView.setText(jsnObject.getString("real_name"));
                                telNumberTextView.setText(jsnObject.getString("telephone_number"));
                                postalCodeTextView.setText("〒" + jsnObject.getString("postal_code"));
                                addressTextView.setText(jsnObject.getString("address"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }
        }).start();



        // 完売済みボタンをクリックした時の処理
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // http通信
                new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            // phpファイルまでのリンク
                            String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/UpdateDelistatus.php";

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
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            final String str = ConnectionJSON.InputStreamToString(con.getInputStream());

                            // 終了時刻
                            endTime = System.currentTimeMillis();
                            Log.d("HTTP", str);

                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(e);
                        }
                    }
                }).start();

                // HomeFragmentに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_navigation_shipping_procedure_to_navigation_delivery_completed);
            }
        });

        return view;
    }
}
