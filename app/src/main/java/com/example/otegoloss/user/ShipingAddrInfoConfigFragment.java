package com.example.otegoloss.user;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.databinding.FragmentUserBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class ShipingAddrInfoConfigFragment extends Fragment {

    public TextView shipping_info_realname1;
    public TextView postalcode1;
    public TextView user_addr1;
    public TextView shipping_info_realname2;
    public TextView postalcode2;
    public TextView user_addr2;

    private String[] realnames;
    private String[] postalcodes;
    private String[] addrs;

    // ユーザID
    String userID;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_shipping_address_info_config, container, false);

        // ユーザID(仮定義)
        userID = "u0000001";

        shipping_info_realname1 = view.findViewById(R.id.textView_shipping_info_realname) ;
        postalcode1 = view.findViewById(R.id.textView_postalcode_first) ;
        user_addr1 = view.findViewById(R.id.textView_user_addr) ;
        shipping_info_realname2 = view.findViewById(R.id.textView_shipping_info_realname2) ;
        postalcode2 = view.findViewById(R.id.textView_postalcode_first2) ;
        user_addr2 = view.findViewById(R.id.textView_user_addr2) ;

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
                                postalcodes = postalcodeList.toArray(new String[postalcodeList.size()]);
                                postalcode1.setText(postalcodes[0]);
                                postalcode2.setText(postalcodes[3]);

                                List<String> user_addrList = ConnectionJSON.ChangeArrayJSON(str, "address");
                                addrs = user_addrList.toArray(new String[user_addrList.size()]);
                                user_addr1.setText(addrs[0]);
                                user_addr2.setText(addrs[3]);

                                List<String> shipping_info_realnameList = ConnectionJSON.ChangeArrayJSON(str, "real_name");
                                realnames = shipping_info_realnameList.toArray(new String[shipping_info_realnameList.size()]);
                                shipping_info_realname1.setText(realnames[0]);
                                shipping_info_realname2.setText(realnames[3]);
                            } catch (Exception e) {
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
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        Button registerShippingButton = view.findViewById(R.id.registerShipping_button);
        Button deleteShippingButton = view.findViewById(R.id.deleteShipping_button);

        registerShippingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_shipping_addr_info_config_to_shipping_addr_info_regist_config);
            }
        });

        deleteShippingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_shipping_addr_info_config_to_shipping_addr_info_delete);
            }
        });

        return view;
    }

}
