//出品情報入力画面
package com.example.otegoloss.shipping;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;


public class EntryOfExhibitInfoFragment extends Fragment {
    //入力情報保持用の宣言
    private EditText pro_name;
    private EditText pro_description;
    private EditText weight;
    private EditText price;
    private EditText recipe_url;
    private String Product_area;
    private String Delivery_method;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_entry_of_exhibit_info, container, false);

        //入力情報保持用のインスタンス生成
        pro_name = (EditText) view.findViewById(R.id.product_name_change_lishting_info);
        pro_description= (EditText) view.findViewById(R.id.product_description_change_lishting_info);
        weight = (EditText) view.findViewById(R.id.product_weight_change_lishting_info);
        price = (EditText) view.findViewById(R.id.amount_change_lishting_info);
        recipe_url = (EditText) view.findViewById(R.id.recipe_url_change_lishting_info);

        //産地のSpinner処理
        Spinner product_area_spinner = (Spinner)view.findViewById(R.id.product_area_change_listing_info);
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

        //配達方法のSpinner処理
        Spinner delivery_method_spinner = (Spinner)view.findViewById(R.id.delivery_method_change_lishting_info);
        delivery_method_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter,
                                       View v, int position, long id) {
                String delivery_method = (String)adapter.getSelectedItem();
                Delivery_method= delivery_method;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            // 入力完了ボタンを取得
        Button buttonNext= view.findViewById(R.id.next_button_entry_exhibit_product);
        // 入力完了ボタンをクリックした時の処理
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //入力情報を格納する
                String pro_names = pro_name.getText().toString();
                String pro_descriptions = pro_description.getText().toString();
                String weights = weight.getText().toString();
                String prices = price.getText().toString();
                String recipe_urls = recipe_url.getText().toString();

                //次のフラグメントにBundleを使ってデータを渡す
                //タイトル
                Bundle bundle = new Bundle();
                bundle.putString("PRODUCT_NAME", pro_names);
                bundle.putString("PRODUCT_DESCRIPTION", pro_descriptions);
                bundle.putString("PRODUCT_WEIGHT", weights);
                bundle.putString("PRODUCT_PRICE", prices);
                bundle.putString("RECIPE_URL", recipe_urls);
                bundle.putString("PRODUCT_AREA", Product_area);
                bundle.putString("DELIVERY_METHOD", Delivery_method);


                // fragmentViewExhibitInfoConfirmationに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_entry_to_confirmation,bundle);
            }
        });



        return view;
    }


}
