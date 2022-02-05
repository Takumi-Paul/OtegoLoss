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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.R;

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
    private String[] creditCardid;

    long startTime;
    long endTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_input_payment, container, false);

        // BundleでHome画面の値を受け取り
        Bundle bundle = getArguments();

        // ユーザID
        String userID = bundle.getString("USER_ID", "");

        String productID = bundle.getString("PRODUCT_ID", "");


        creditCompany1 = view.findViewById(R.id.credit_company1);
        creditNumber1 = view.findViewById(R.id.credit_number1);
        creditNominee1 = view.findViewById(R.id.credit_nominee1);
        creditCompany2 = view.findViewById(R.id.credit_company2);
        creditNumber2 = view.findViewById(R.id.credit_number2);
        creditNominee2 = view.findViewById(R.id.credit_nominee2);



        // http通信
        Thread t = new Thread(new Runnable() {
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

                                List<String> creditcompanyList = ConnectionJSON.ChangeArrayJSON(str, "card_comp");
                                List<String> creditnumberList = ConnectionJSON.ChangeArrayJSON(str, "card_number");
                                List<String> nomineeList = ConnectionJSON.ChangeArrayJSON(str, "nominee");
                                List<String> creditidList = ConnectionJSON.ChangeArrayJSON(str, "card_id");

                                creditCompanies = creditcompanyList.toArray(new String[creditcompanyList.size()]);
                                creditNumbers = creditnumberList.toArray(new String[creditnumberList.size()]);
                                creditNominee = nomineeList.toArray(new String[nomineeList.size()]);
                                creditCardid = creditidList.toArray(new String[creditidList.size()]);

                                creditCompany1.setText(creditCompanies[0]);
                                creditNumber1.setText(creditNumbers[0]);
                                System.out.println(creditNumbers[0]);
                                creditNominee1.setText(creditNominee[0]);
                                System.out.println(creditCardid[0]);

                                creditCompany2.setText(creditCompanies[1]);
                                creditNumber2.setText(creditNumbers[1]);
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
        });

        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }





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

                Bundle nextbundle = new Bundle();
                nextbundle.putString("USER_ID", userID);
                nextbundle.putString("PRODUCT_ID", productID);

                if (paymentRadioButton1.isChecked() == true) {
                    String creditCompany1s = creditCompany1.getText().toString();
                    if(creditCompany1s.equals("クレカ会社1")) {
                        Toast.makeText(view.getContext() , "登録されていません", Toast.LENGTH_LONG).show();
                    }else {

                        System.out.println(creditCardid[0]);

                        //ここにラジオボタン1に書かれている情報をバンドルに渡す処理を書く
                        nextbundle.putString("CARD_ID", creditCardid[0]);
                        nextbundle.putString("COMPANY", creditCompanies[0]);
                        nextbundle.putString("NUMBER", creditNumbers[0]);
                        nextbundle.putString("NOMINEE", creditNominee[0]);

                        Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputPayment_to_fragmentViewInputShippingAddress, nextbundle);
                    }

                }else if (paymentRadioButton2.isChecked() == true) {
                    String creditCompany2s = creditCompany2.getText().toString();
                    if(creditCompany2s.equals("クレカ会社2")) {
                        Toast.makeText(view.getContext() , "登録されていません", Toast.LENGTH_LONG).show();
                    }else {

                        //ここにラジオボタン2に書かれている情報をバンドルに渡す処理を書く
                        nextbundle.putString("CARD_ID", creditCardid[1]);
                        nextbundle.putString("COMPANY", creditCompanies[1]);
                        nextbundle.putString("NUMBER", creditNumbers[1]);
                        nextbundle.putString("NOMINEE", creditNominee[1]);

                        Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputPayment_to_fragmentViewInputShippingAddress, nextbundle);
                    }

                }

            }
        });

        return view;
    }

}