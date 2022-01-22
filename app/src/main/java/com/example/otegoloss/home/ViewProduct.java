/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.otegoloss.ChangeBackgraund;
import com.example.otegoloss.R;
import com.example.otegoloss.shipping.ViewYetSoldOutHistoryFragment;

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

public class ViewProduct extends Fragment {

    TextView productNameTextView;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_product, container, false);
        LinearLayout background_view = view.findViewById(R.id.background);
        ChangeBackgraund.changeBackGround(background_view);

        // BundleでHome画面の値を受け取り
        Bundle bundle = getArguments();
        // 画像ID
        int imageId = bundle.getInt("IMAGEID", 0);
        // 商品ID
        int productID = bundle.getInt("PRODUCT_ID", 0);

        // imageViewのIDを関連付けて画像を表示
        ImageView imageView = view.findViewById(R.id.productImage_imageView);
        imageView.setImageResource(imageId);

        productNameTextView = view.findViewById(R.id.productName_textView);

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
                                productNameTextView.setText(jsnObject.getString("product_name"));
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

        Button buyButton = view.findViewById(R.id.buy_button);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentProduct_to_fragmentViewInputPayment2);
            }
        });


        Button commentButton = view.findViewById(R.id.comment_button);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(view).navigate(R.id.action_fragmentProduct_to_fragmentViewComment2);
                //FragmentManager fm_ViewComment = getParentFragmentManager();
                //FragmentTransaction t_ViewComment  =  fm_ViewComment.beginTransaction();
                // 次のFragment
                //Fragment secondFragment = new ViewComment();
                // fragmentManagerに次のfragmentを追加
                //t_ViewComment.replace(R.id.fragmentProduct, secondFragment);
                // 画面遷移戻りを設定
                //t_ViewComment.addToBackStack(null);
                // 画面遷移
                //t_ViewComment.commit();
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
                Log.d("Check", jsonData.getString("product_name"));
                return jsonData;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}