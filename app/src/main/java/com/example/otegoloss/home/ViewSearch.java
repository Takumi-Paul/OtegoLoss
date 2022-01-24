/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.ChangeBackgraund;
import com.example.otegoloss.R;

public class ViewSearch extends Fragment {

    private EditText productName;
    private EditText sellerName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_search, container, false);
        LinearLayout background_view = view.findViewById(R.id.background);
        ChangeBackgraund.changeBackGround(background_view);

        productName = (EditText)view.findViewById(R.id.kensaku_shouhin_name);
        sellerName = (EditText)view.findViewById(R.id.kensaku_shuppin_name);


        //検索ボタンが押された時の処理
        Button SearchButton  = view.findViewById(R.id.search_button);


        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productNames = productName.getText().toString();
                String sellerNames = sellerName.getText().toString();

                if (productNames.equals("")
                        || sellerNames.equals("")
                ) {
                    Toast.makeText(view.getContext(), "入力された情報が正しくありません。もう一度確認してください。", Toast.LENGTH_LONG).show();

                } else if(productNames.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠0-9a-zA-Z!\"#$%&'()*+-.,\\/:;<=>?@[\\]^_`{|}~]]*$")
                        && sellerNames.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠0-9a-zA-Z!\"#$%&'()*+-.,\\/:;<=>?@[\\]^_`{|}~]]*$")
                   /*
                    && (int_weight >= 0 && int_weight <= 30000)
                    && (int_price >= 10 && int_price <= 99999)
                    */
                ) {
                    //次のフラグメントにBundleを使ってデータを渡す
                    //タイトル
                    /*
                    Bundle bundle = new Bundle();
                    bundle.putString("PRODUCT_NAME", pro_names);
                    bundle.putString("PRODUCT_DESCRIPTION", pro_descriptions);
                    bundle.putString("PRODUCT_WEIGHT", pro_weights);
                    bundle.putString("PRODUCT_PRICE", pro_prices);
                    bundle.putString("RECIPE_URL", recipe_urls);
                    bundle.putString("PRODUCT_AREA", Product_area);
                    bundle.putString("DELIVERY_METHOD", Delivery_method);
                    */
                    //bundle.putInt("PRODUCT_INT_WEIGHT", int_weight);
                    //bundle.putInt("PRODUCT_INT_PRICE", int_price);


                    // fragmentViewExhibitInfoConfirmationに遷移させる
                    Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_fragmentresult);
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