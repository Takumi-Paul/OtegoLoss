/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.ChangeBackgraund;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

public class ViewSearch extends Fragment {

    private EditText productName;
    private EditText sellerName;

    private String Product_area = "";
    private String Category;
    private String Price = "";
    private String Delivery = "";
    private String Weight = "";
    private String pName = "";
    private String sName = "";

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_view_search, container, false);

        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");
        System.out.println(userID);

        LinearLayout background_view = view.findViewById(R.id.background);
        ChangeBackgraund.changeBackGround(background_view, userID);

        productName = (EditText)view.findViewById(R.id.kensaku_product_name);
        sellerName = (EditText)view.findViewById(R.id.kensaku_seller_name);


        //産地のSpinner処理
        Spinner product_area_spinner = (Spinner)view.findViewById(R.id.kensaku_area);
        product_area_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter,
                                       View v, int position, long id) {
                String product_area = (String)adapter.getSelectedItem();
                Product_area = product_area;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        Spinner category_spinner = (Spinner)view.findViewById(R.id.kensaku_category_name);
//        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
//                String category = (String)adapter.getSelectedItem();
//                Category = category;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        Spinner price_spinner = (Spinner)view.findViewById(R.id.kensaku_price);
        price_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String price = (String)adapter.getSelectedItem();
                Price = price;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner delivery_spinner = (Spinner)view.findViewById(R.id.kensaku_delivery);
        delivery_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String delivery = (String)adapter.getSelectedItem();
                Delivery = delivery;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner weight_spinner = (Spinner)view.findViewById(R.id.kensaku_weight);
        weight_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String weight = (String)adapter.getSelectedItem();
                Weight = weight;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //検索ボタンが押された時の処理
        Button SearchButton  = view.findViewById(R.id.buyCompletedButton);


        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productNames = productName.getText().toString();
                String sellerNames = sellerName.getText().toString();


                if (productNames.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠0-9a-zA-Z!\"#$%&'()*+-.,\\/:;<=>?@[\\]^_`{|}~]]*$")
                        && sellerNames.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠0-9a-zA-Z!\"#$%&'()*+-.,\\/:;<=>?@[\\]^_`{|}~]]*$")
                   /*
                    && (int_weight >= 0 && int_weight <= 30000)
                    && (int_price >= 10 && int_price <= 99999)
                    */
                ) {
                    //次のフラグメントにBundleを使ってデータを渡す
                    Bundle bundle = new Bundle();
                    pName = productName.getText().toString();
                    bundle.putString("PRODUCT_NAME", pName);
                    bundle.putString("PRODUCT_WEIGHT", Weight);
                    bundle.putString("PRODUCT_PRICE", Price);
                    bundle.putString("PRODUCT_AREA", Product_area);
                    bundle.putString("DELIVERY_METHOD", Delivery);
                    sName = sellerName.getText().toString();
                    bundle.putString("SELLER_NAME", sName);
                    //bundle.putInt("PRODUCT_INT_WEIGHT", int_weight);
                    //bundle.putInt("PRODUCT_INT_PRICE", int_price);

                    // fragmentViewExhibitInfoConfirmationに遷移させる
                    System.out.println("open");
                    Navigation.findNavController(view).navigate(R.id.action_search_to_result, bundle);



                } else {
                    Toast.makeText(view.getContext(), "入力された情報が正しくありません。もう一度確認してください。", Toast.LENGTH_LONG).show();
                }




                //商品名と出品者名を取得
                //String productNames = productName.getText().toString();
                //String sellerNames =  sellerName.getText().toString();

            }
        });

        return view;
    }

}