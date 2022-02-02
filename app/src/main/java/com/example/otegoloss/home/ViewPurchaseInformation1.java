/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.home;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.ChangeBackgraund;
import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ViewPurchaseInformation1 extends Fragment {

    ProgressDialog progressDialog;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    TextView productNameTextView;
    TextView showWeightTextView;
    TextView priceTextView;
    TextView regionTextView;
    ImageView productImage;
    TextView proNameTextView;

    TextView addressNumber;
    TextView shippingAddress;
    TextView addressName;

    TextView creditCompany;
    TextView creditNumber;
    TextView creditNominee;

    // 出品者ID
    String sellerID;

    // 画像関連
    String user_image_url;
    Bitmap imgBmp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_purchase_information1, container, false);

        // BundleでHome画面の値を受け取り
        Bundle bundle = getArguments();

        // ユーザID
        String userID = bundle.getString("USER_ID", "");
        System.out.println("bundle"+userID);

        String productID = bundle.getString("PRODUCT_ID", "");

        String company = bundle.getString("COMPANY", "");
        String number = bundle.getString("NUMBER", "");
        String nominee = bundle.getString("NOMINEE", "");

        String cardID = bundle.getString("CARD_ID", "");

        String postal = bundle.getString("POSTAL", "");
        String addresses = bundle.getString("ADDRESS", "");
        String real = bundle.getString("REAL", "");

        String daddressID = bundle.getString("SHIPPING_ID", "");

        // imageViewのIDを関連付けて画像を表示
        productImage = view.findViewById(R.id.cProductImage_imageView);

        productNameTextView = view.findViewById(R.id.cProductName);
        proNameTextView = view.findViewById(R.id.cSellerName);
        showWeightTextView = view.findViewById(R.id.cWeightName);
        regionTextView = view.findViewById(R.id.cRegionName);
        priceTextView = view.findViewById(R.id.cPriceName);

        addressNumber = view.findViewById(R.id.cAddressNumber_textView);
        shippingAddress = view.findViewById(R.id.cAddress_textView);
        addressName = view.findViewById(R.id.cAddressName_textView);

        creditCompany = view.findViewById(R.id.cCreditCompany_textView);
        creditNumber = view.findViewById(R.id.cCreditNumber_textView);
        creditNominee = view.findViewById(R.id.cCreditName_textView);

        addressNumber.setText(postal);
        shippingAddress.setText(addresses);
        addressName.setText(real);
        creditNominee.setText(nominee);
        creditNumber.setText(number);
        creditCompany.setText(company);

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
                                showWeightTextView.setText(jsnObject.getString("weight") + "g");
                                regionTextView.setText(jsnObject.getString("prefecture"));
                                priceTextView.setText(jsnObject.getString("price") + "円");

                                productImage.setImageBitmap(imgBmp);


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


        //「購入確定」ボタンが押された時の処理
        Button BuyCompletedButton = view.findViewById(R.id.buyCompletedButton);


        BuyCompletedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // http通信
                Thread t = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            // phpファイルまでのリンク
                            String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/InsertPurchase.php";

                            // クエリ文字列を連想配列に入れる
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("product_id", productID);
                            map.put("purchaser_id", userID);
                            map.put("card_id", cardID);
                            map.put("address_id", daddressID);
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


                            // phpファイルまでのリンク
                            String weight_path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/AddWeight.php";
                            // クエリ文字列を連想配列に入れる
                            Map<String, String> weight_map = new HashMap<String, String>();
                            weight_map.put("product_id", productID);
                            weight_map.put("user_id", userID);
                            // クエリ文字列組み立て・URL との連結
                            StringJoiner weight_stringUrl = new StringJoiner("&", path + "?", "");
                            for (Map.Entry<String, String> param: weight_map.entrySet()) {
                                weight_stringUrl.add(param.getKey() + "=" + param.getValue());
                            }
                            URL weight_url = new URL(weight_stringUrl.toString());
                            System.out.println(weight_url);
                            // 処理開始時刻
                            startTime = System.currentTimeMillis();
                            HttpURLConnection weight_con =(HttpURLConnection)weight_url.openConnection();
                            final String weight_str = ConnectionJSON.InputStreamToString(weight_con.getInputStream());

                            // 終了時刻
                            endTime = System.currentTimeMillis();
                            Log.d("HTTP", str);


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(String.valueOf(str));
                                    System.out.println(endTime - startTime);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(e);
                        }
                    }
                });

                try{
                    t.start();
                    System.out.println("start");
                    // ダイアログ表示
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("ロード中");
                    progressDialog.setMessage("処理しています");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    t.join();
                    progressDialog.dismiss();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Navigation.findNavController(view).navigate(R.id.action_purchase_information1_to_fragmentViewPurchaseCompleted);

            }
        });

        return view;
    }

}