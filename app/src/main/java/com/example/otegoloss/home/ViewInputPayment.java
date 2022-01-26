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
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ViewInputPayment extends Fragment {

    TextView creditCompany1;
    TextView creditNumber1;
    TextView creditNominee1;
    TextView creditCompany2;
    TextView creditNumber2;
    TextView creditNominee2;

    private String[] creditCompanies;
    private String[] creditNumbers;
    private String[] creditNominee;

    long startTime;
    long endTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_input_payment, container, false);

        // BundleでHome画面の値を受け取り
        Bundle bundle = getArguments();

        // ユーザID(仮定義)
        String userID = "u0000001";

        // ユーザID
        //String userID = bundle.getString("USER_ID", "");


        creditCompany1 = view.findViewById(R.id.credit_company1);
        creditNumber1 = view.findViewById(R.id.credit_number1);
        creditNominee1 = view.findViewById(R.id.credit_nominee1);
        creditCompany2 = view.findViewById(R.id.credit_company2);
        creditNumber2 = view.findViewById(R.id.credit_number2);
        creditNominee2 = view.findViewById(R.id.credit_nominee2);


        // http通信
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    String path = "http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/ListingCredit.php";

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
                                //creditCompany1.setText(jsnObject.getString("card_id"));
                                //creditNumber1.setText(jsnObject.getString("card_comp"));
                                //creditNominee1.setText(jsnObject.getString("nominee"));

                                //creditCompany2.setText(jsnObject.getString("price"));
                                //creditNumber2.setText(jsnObject.getString("prefecture"));
                                //creditNominee2.setText(jsnObject.getString("Listing_date"));

                                List<String> creditcompanyList = ConnectionJSON.ChangeArrayJSON(str, "card_comp");
                                creditCompanies = creditcompanyList.toArray(new String[creditcompanyList.size()]);
                                creditCompany1.setText(creditCompanies[0]);
                                creditCompany2.setText(creditCompanies[1]);

                                List<String> creditnumberList = ConnectionJSON.ChangeArrayJSON(str, "card_number");
                                creditNumbers = creditnumberList.toArray(new String[creditnumberList.size()]);
                                creditNumber1.setText(creditNumbers[0]);
                                creditNumber2.setText(creditNumbers[1]);

                                List<String> nomineeList = ConnectionJSON.ChangeArrayJSON(str, "nominee");
                                creditNominee = nomineeList.toArray(new String[nomineeList.size()]);
                                creditNominee1.setText(creditNominee[0]);
                                creditNominee2.setText(creditNominee[1]);

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
        }).start();





        //「決済情報を追加」ボタンが押された時の処理
        Button AddCreditButton = view.findViewById(R.id.addCredit_button);

        AddCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputPayment_to_creditcard_regist);
            }
        });


        //「決済情報を削除」ボタンが押された時の処理
        Button DeleteCreditButton = view.findViewById(R.id.deleteCredit_button);

        DeleteCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputPayment_to_pay_info_delete);
            }
        });


        //「ラジオボタン」が押された時の処理
        RadioButton paymentRadioButton1 = (RadioButton)view.findViewById(R.id.payment_radiobutton1);
        RadioButton paymentRadioButton2 = (RadioButton)view.findViewById(R.id.payment_radiobutton2);

        //「次へ」ボタンが押された時の処理
        Button NextButtonPayment = view.findViewById(R.id.review_finish_button);

        NextButtonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentRadioButton1.isChecked() == true) {

                    //ここにラジオボタン1に書かれている情報をバンドルに渡す処理を書く


                    Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputPayment_to_fragmentViewInputShippingAddress);

                }

                if (paymentRadioButton2.isChecked() == true) {

                    //ここにラジオボタン2に書かれている情報をバンドルに渡す処理を書く


                    Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputPayment_to_fragmentViewInputShippingAddress);

                }

            }
        });

        return view;
    }

}