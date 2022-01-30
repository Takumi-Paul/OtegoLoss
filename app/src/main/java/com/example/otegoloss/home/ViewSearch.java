/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.ChangeBackgraund;
import com.example.otegoloss.R;

public class ViewSearch extends AppCompatActivity {

    private EditText productName;
    private EditText sellerName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_search);

        LinearLayout background_view = findViewById(R.id.background);
        ChangeBackgraund.changeBackGround(background_view);

        productName = (EditText)findViewById(R.id.kensaku_shouhin_name);
        sellerName = (EditText)findViewById(R.id.kensaku_shuppin_name);


        //検索ボタンが押された時の処理
        Button SearchButton  = findViewById(R.id.buyCompletedButton);


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

                    System.out.println("open");
                    // fragmentViewExhibitInfoConfirmationに遷移させる
                    //Navigation.findNavController().navigate(R.id.action_searchFragment_to_fragmentresult);

                    //if (savedInstanceState == null) {
//
                        Fragment fragment = new ViewSearchResult();
                        Bundle bundle = new Bundle();
                        fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.searchFragment, fragment).commit();
                    //}
//                    Intent intent = new Intent(ViewSearch.this, ViewSearchResult.class);
//                    startActivity(intent);


//                    Fragment fr = new ViewSearchResult();
//
//                    System.out.println(fr.getId());


//                    FragmentManager fm_item = getChildFragmentManager();
//                    FragmentTransaction t_item = fm_item.beginTransaction();
//                    // 次のFragment
//                    Fragment secondFragment = new ViewSearchResult();
//                    // bundleを次のfragmentに設定
//                    // fragmentManagerに次のfragmentを追加
//                    t_item.replace(R.id.fragmentresult, secondFragment);
//                    // 画面遷移戻りを設定
//                    t_item.addToBackStack(null);
//                    // 画面遷移
//                    t_item.commit();

                } else {
                    Toast.makeText(getApplicationContext(), "入力された情報が正しくありません。もう一度確認してください。", Toast.LENGTH_LONG).show();
                }




                //商品名と出品者名を取得
                //String productNames = productName.getText().toString();
                //String sellerNames =  sellerName.getText().toString();

            }
        });
    }

}