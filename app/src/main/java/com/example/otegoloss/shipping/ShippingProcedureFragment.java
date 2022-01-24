package com.example.otegoloss.shipping;

import android.os.Bundle;
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

public class ShippingProcedureFragment extends Fragment {
    //購入者
    //private String purchaser_name = "高知太郎";
    //購入品
    //private String product_name = "tomato";
    //住所
    //private String address = "高知市万々111111-11111-2--1212111";

    TextView purchaserNameTextView;
    TextView purchasedProductTextView;
    TextView addressTextView;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_shipping_procedure, container, false);

        //商品名を表示
        purchasedProductTextView = view.findViewById(R.id.purchase_name_view_shipping_product);
        //購入者名を表示
        purchaserNameTextView = view.findViewById(R.id.product_name_view_shipping_product);
        //住所を表示
        addressTextView = view.findViewById(R.id.address_view_shipping_product);

        // http通信
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://localhost/OtegoLoss_WebAPI/product&purchase/listingdetails.php?product_id=g0000001");
                    // 処理開始時刻
                    startTime = System.currentTimeMillis();
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    final String str = InputStreamToString(con.getInputStream());

                    // 終了時刻
                    endTime = System.currentTimeMillis();
                    Log.d("HTTP", str);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(String.valueOf(str));
                            System.out.println(endTime - startTime);

                            JSONObject jsnObject = ChangeJson(str);
                            try {
                                // Jsonのキーを指定すれば対応する値が入る
                                purchaserNameTextView.setText(jsnObject.getString("real_name"));
                                purchasedProductTextView.setText(jsnObject.getString("pro.product_name"));
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



        // ボタン要素を取得
        Button nextButton = view.findViewById(R.id.shipment_comp_button_view_shipping_product);

        // 完売済みボタンをクリックした時の処理
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HomeFragmentに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_navigation_shipping_procedure_to_navigation_delivery_completed);
            }
        });

        return view;
    }
    // http通信で受け取ったデータをString化する
    static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    // Jsonデータに変換
    static JSONObject ChangeJson(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            // JSONArray jsonArray = jsonObject.getJSONArray("sample");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                Log.d("Check", jsonData.getString("real_name"));
                Log.d("Check", jsonData.getString("pro.product_name"));
                Log.d("Check", jsonData.getString("address"));
                return jsonData;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
