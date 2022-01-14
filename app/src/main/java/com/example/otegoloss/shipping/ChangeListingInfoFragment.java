package com.example.otegoloss.shipping;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.otegoloss.R;

public class ChangeListingInfoFragment extends Fragment {
    //商品名
    private String product_name = "tomato";
    //商品説明
    private String product_desc = "高知県産フルーツトマトです。糖度は12度くらいです。";
    //量
    private int pro_weight = 10;
    //金額
    private int pro_price = 1000;
    //レシピURL
    private String recipe_url = "https:www.kochi-tech.ac.jp/";

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
         View view = inflater.inflate(R.layout.fragment_change_listing_info, container, false);
        /*
        クラッシュの原因になっているので隠している。どうやら、EditTextの初期化はonStart()やonResume()で行うらしい・・・やってみたけどよくわからなかった
         //以下値のセット
        //商品名
        EditText product_names = view.findViewById(R.id.product_name_change_lishting_info);
        product_names.setText(product_name, TextView.BufferType.NORMAL);
        //商品説明
        EditText product_des = view.findViewById(R.id.product_description_change_lishting_info);
        product_des.setText(product_desc, TextView.BufferType.NORMAL);
        //量
        EditText product_weights = view.findViewById(R.id.product_weight_change_lishting_info);
        product_weights.setText(pro_weight, TextView.BufferType.NORMAL);
        //金額
        EditText product_prices = view.findViewById(R.id.amount_change_lishting_info);
        product_prices.setText(pro_price, TextView.BufferType.NORMAL);
        //レシピURL
        EditText recipe_urls = view.findViewById(R.id.recipe_url_change_lishting_info);
        recipe_urls.setText(recipe_url, TextView.BufferType.NORMAL);
        */


    return view;
    }

}
