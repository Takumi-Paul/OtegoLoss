package com.example.otegoloss.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShippingAddrInfoRegistFragment extends Fragment {
    //入力情報保持用の宣言
    private String area_Sap;

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;
    String userID;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    String str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_shipping_address_info_regist, container, false);

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        if (userID == "error") {
            userID = "u0000003";
        }
        System.out.println(userID);

        // 氏名の定義
        EditText fullname = (EditText) view.findViewById(R.id.name_editText);

        // 電話番号の定義
        EditText phone_number= (EditText) view.findViewById(R.id.proPhoneNumber_editText);

        // 郵便番号の定義
        EditText postal_code = (EditText) view.findViewById(R.id.addressNumber_editText);

        // 市町村区の定義
        EditText area_city = (EditText) view.findViewById(R.id.postalAddress_editText);

        // 丁目・番地・号の定義
        EditText area_number = (EditText) view.findViewById(R.id.homeNumber_editText);

        //都道府県のSpinner処理
        Spinner product_area_spinner = (Spinner)view.findViewById(R.id.prefecture_spinner);
        product_area_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter,
                                       View v, int position, long id) {
                String area_sap = (String)adapter.getSelectedItem();
                area_Sap = area_sap;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button inputAddressButton = view.findViewById(R.id.inputAddress_button);
        inputAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullnames = fullname.getText().toString();
                String phone_numbers = phone_number.getText().toString();
                String postal_codes = postal_code.getText().toString();
                String area_citys = area_city.getText().toString();
                String area_numbers = area_number.getText().toString();

                if (fullnames.equals("") || phone_numbers.equals("") || postal_codes.equals("") || area_citys.equals("") || area_numbers.equals("")){
                    Toast.makeText(view.getContext(), "入力欄に不備があります", Toast.LENGTH_LONG).show();
                } else if (fullnames.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠a-zA-Z]*$") && phone_numbers.matches("^[0-9]*$")&& postal_codes.matches("^[0-9]*$")) {

                    // http通信
                    Thread t = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            try {
                                // phpファイルまでのリンク
                                URL path = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/InsertDelivery.php");

                                // POSTで送るStringデータ
                                String postData = "user_id=" + userID +
                                        "&real_name=" + fullnames +
                                        "&telephone_number=" + phone_numbers +
                                        "&postal_code=" + postal_codes +
                                        "&address=" + area_Sap + area_citys +area_numbers;

                                System.out.println(path);
                                // 処理開始時刻
                                startTime = System.currentTimeMillis();
                                HttpURLConnection con =(HttpURLConnection)path.openConnection();
                                con.setRequestMethod("POST");
                                con.setUseCaches(false);// キャッシュ利用
                                con.setDoOutput(true);// リクエストのボディの送信を許可(GETのときはfalse,POSTのときはtrueにする)
                                con.setDoInput(true);// レスポンスのボディの受信を許可

                                System.out.println(postData);
                                // サーバとパイプをつなぐ
                                OutputStream os = con.getOutputStream();
                                PrintStream ps = new PrintStream(os);
                                // リクエストパラメータを送信する
                                ps.write(postData.getBytes());
                                // ファイルを書き込む
                                ps.flush();
                                // ファイルを閉じる
                                ps.close();

                                // レスポンスコード確認
                                final int responseCode = con.getResponseCode();
                                System.out.println(responseCode);

                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    // レスポンスコードが200ならStringに変換
                                    str = ConnectionJSON.InputStreamToString(con.getInputStream());
                                }

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

                    try {
                        // スレッド開始
                        t.start();
                        // スレッドが終わるまで他の処理を停止
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Navigation.findNavController(view).navigate(R.id.shipping_addr_info_regist_comp);
                } else {
                    Toast.makeText(view.getContext(), "入力欄に不備があります", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

}
