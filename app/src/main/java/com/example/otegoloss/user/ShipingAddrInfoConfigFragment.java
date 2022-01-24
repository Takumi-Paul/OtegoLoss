package com.example.otegoloss.user;

import static com.example.otegoloss.user.UserFragment.InputStreamToString;

import android.os.Bundle;
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

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.databinding.FragmentUserBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class ShipingAddrInfoConfigFragment extends Fragment {

    public TextView shipping_info_realname;
    public TextView postalcode_first;
    public TextView postalcode_Last;
    public TextView user_addr;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_shipping_address_info_config, container, false);

        shipping_info_realname = view.findViewById(R.id.textView_shipping_info_realname) ;
        postalcode_first = view.findViewById(R.id.textView_postalcode_first) ;
        postalcode_Last = view.findViewById(R.id.textView_postalcode_Last) ;
        user_addr = view.findViewById(R.id.textView_user_addr) ;

        // http通信
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/ProductDetails.php?product_id=g0000001");
                    // 処理開始時刻
                    startTime = System.currentTimeMillis();
                    HttpURLConnection con =(HttpURLConnection)url.openConnection();
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
                                shipping_info_realname.setText(jsnObject.getString("real_name"));
                                //postalcode_first.setText(jsnObject.getString("card_number"));
                                //postalcode_Last.setText(jsnObject.getString("card_comp"));
                                user_addr.setText(jsnObject.getString("address"));
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

    // Jsonデータに変換
    static JSONObject ChangeJson(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            // JSONArray jsonArray = jsonObject.getJSONArray("sample");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                //Log.d("Check", jsonData.getString("user_profile_image"));
                Log.d("Check", jsonData.getString("real_name"));
                Log.d("Check", jsonData.getString("address"));
                return jsonData;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
