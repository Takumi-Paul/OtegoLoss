/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ViewInputShippingAddress extends Fragment {

    TextView addressNumber1;
    TextView shippingAddress1;
    TextView shippingName1;
    TextView addressNumber2;
    TextView shippingAddress2;
    TextView shippingName2;

    private String[] postalCodes;
    private String[] addresses;
    private String[] realNames;
    private String[] daddressID;

    long startTime;
    long endTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_view_input_shipping_address, container, false);

        // BundleでHome画面の値を受け取り
        Bundle bundle = getArguments();

        // ユーザID(仮定義)
        //String userID = "u0000001";

        // ユーザID
        String userID = bundle.getString("USER_ID", "");

        String productID = bundle.getString("PRODUCT_ID", "");

        String cardID = bundle.getString("CARD_ID", "");

        //背景
//        LinearLayout background_view = view.findViewById(R.id.background);
//        ChangeBackgraund.changeBackGround(background_view, userID);

        addressNumber1 = view.findViewById(R.id.ShippingAddressNumber1_textView);
        shippingAddress1 = view.findViewById(R.id.ShippingAddress1_textView);
        shippingName1 = view.findViewById(R.id.ShippingName1_textView);
        addressNumber2 = view.findViewById(R.id.ShippingAddressNumber2_textView);
        shippingAddress2 = view.findViewById(R.id.ShippingAddress2_textView);
        shippingName2 = view.findViewById(R.id.ShippingName2_textView);

        // http通信
       Thread t = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/ListingDelivery.php";

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
                    HttpURLConnection con =(HttpURLConnection)url.openConnection();
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
                                List<String> postalcodeList = ConnectionJSON.ChangeArrayJSON(str, "postal_code");
                                List<String> addressesList = ConnectionJSON.ChangeArrayJSON(str, "address");
                                List<String> realnameList = ConnectionJSON.ChangeArrayJSON(str, "real_name");
                                List<String> daddressList = ConnectionJSON.ChangeArrayJSON(str, "d_address_id");

                                postalCodes = postalcodeList.toArray(new String[postalcodeList.size()]);
                                addresses = addressesList.toArray(new String[addressesList.size()]);
                                realNames = realnameList.toArray(new String[realnameList.size()]);
                                daddressID = daddressList.toArray(new String[daddressList.size()]);

                                addressNumber1.setText(postalCodes[0]);
                                shippingAddress1.setText(addresses[0]);
                                shippingName1.setText(realNames[0]);

                                addressNumber2.setText(postalCodes[1]);
                                shippingAddress2.setText(addresses[1]);
                                shippingName2.setText(realNames[1]);

                            } catch (Exception e) {         //修正するかも　元はcatch (JSONException e) {e.printStackTrace();}
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (IOException e) {
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


        
        //「住所を追加」を押したときの処理
        Button AddAddressButton  = view.findViewById(R.id.addAddress_button);

        AddAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputShippingAddress_to_shipping_addr_info_regist_config);
            }
        });

        //「住所を削除」を押したときの処理
        Button DeleteAddressButton  = view.findViewById(R.id.deleteAddress_button);

        DeleteAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputShippingAddress_to_shipping_addr_info_delete);
            }
        });



        //「ラジオボタン」が押された時の処理
        RadioButton Choice1Button = (RadioButton)view.findViewById(R.id.choice1_radioButton);
        RadioButton Choice2Button = (RadioButton)view.findViewById(R.id.choice2_radioButton);


        //「次へ」ボタンが押された時の処理
        Button NextButtonShipping = view.findViewById(R.id.next_button_shipping);

        NextButtonShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle nextbundle = bundle;

//                nextbundle.putString("USER_ID", userID);
//                nextbundle.putString("PRODUCT_ID", productID);
//                nextbundle.putString("CARD_ID", cardID);


                if (Choice1Button.isChecked() == true) {
                    String addressNumber1s = addressNumber1.getText().toString();
                    if(addressNumber1s.equals("郵便番号1")) {
                        Toast.makeText(view.getContext() , "登録されていません", Toast.LENGTH_LONG).show();
                    }else {

                        //ここにラジオボタン1に書かれている情報をバンドルに渡す処理を書く
                        nextbundle.putString("SHIPPING_ID", daddressID[0]);
                        nextbundle.putString("POSTAL", postalCodes[0]);
                        nextbundle.putString("ADDRESS", addresses[0]);
                        nextbundle.putString("REAL", realNames[0]);

                        Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputShippingAddress_to_purchase_information1, nextbundle);
                    }

                }

                if (Choice2Button.isChecked() == true) {
                    String addressNumber2s = addressNumber2.getText().toString();
                    if(addressNumber2s.equals("郵便番号2")) {
                        Toast.makeText(view.getContext() , "登録されていません", Toast.LENGTH_LONG).show();
                    }else {

                        //ここにラジオボタン2に書かれている情報をバンドルに渡す処理を書く
                        nextbundle.putString("SHIPPING_ID", daddressID[1]);

                        Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputShippingAddress_to_purchase_information1, nextbundle);
                    }

                }

            }
        });

        return view;

    }
}




