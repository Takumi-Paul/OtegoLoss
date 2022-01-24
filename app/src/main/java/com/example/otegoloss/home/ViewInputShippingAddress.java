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
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.otegoloss.ChangeBackgraund;
import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ViewInputShippingAddress extends Fragment {

    TextView addressNumber1;
    TextView shippingAddress1;
    TextView shippingName1;
    TextView addressNumber2;
    TextView shippingAddress2;
    TextView shippingName2;

    long startTime;
    long endTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_view_input_shipping_address, container, false);


        // ユーザID(仮定義)
        String userID = "u0000001";

        // ユーザID
        //String userID = bundle.getString("USER_ID", "");

        addressNumber1 = view.findViewById(R.id.ShippingAddressNumber1_textView);
        shippingAddress1 = view.findViewById(R.id.ShippingAddress1_textView);
        shippingName1 = view.findViewById(R.id.ShippingName1_textView);
        addressNumber2 = view.findViewById(R.id.ShippingAddressNumber2_textView);
        shippingAddress2 = view.findViewById(R.id.ShippingAddress2_textView);
        shippingName2 = view.findViewById(R.id.ShippingName2_textView);

        // http通信
        new Thread(new Runnable() {
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
                                addressNumber1.setText(jsnObject.getString("postal_code"));
                                shippingAddress1.setText(jsnObject.getString("address"));
                                shippingName1.setText(jsnObject.getString("real_name"));

                                //addressNumber2.setText(jsnObject.getString("price"));
                                //shippingAddress2.setText(jsnObject.getString("prefecture"));
                                //shippingName2.setText(jsnObject.getString("Listing_date"));

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



        //「次へ」ボタンが押された時の処理
        Button NextButtonShipping = view.findViewById(R.id.next_button_shipping);

        NextButtonShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputShippingAddress_to_purchase_information1);

                //FragmentManager fm_NextButtonShipping = getChildFragmentManager();
                //FragmentTransaction t_NextButtonShipping  =  fm_NextButtonShipping.beginTransaction();
                // 次のFragment
                //Fragment secondFragment = new ViewPurchaseInformation1();
                // fragmentManagerに次のfragmentを追加
                //t_NextButtonShipping.add(R.id.fragmentViewInputShippingAddress, secondFragment);
                // 画面遷移戻りを設定
                //t_NextButtonShipping.addToBackStack(null);
                // 画面遷移
                //t_NextButtonShipping.commit();
            }
        });

        return view;
    }

}