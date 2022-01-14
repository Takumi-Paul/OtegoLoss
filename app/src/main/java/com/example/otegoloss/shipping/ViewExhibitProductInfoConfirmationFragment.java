/*
出品関連、出品情報確認画面
 */
package com.example.otegoloss.shipping;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;


public class ViewExhibitProductInfoConfirmationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_exhibit_info_confirmation, container, false);

        //商品名
        TextView product_name_view = view.findViewById(R.id.product_name_confirmation);
        product_name_view.setText(getArguments().getString("PRODUCT_NAME"));
        //商品説明
        TextView product_desc_view= view.findViewById(R.id.productInfo_confirmation);
        product_desc_view.setText(getArguments().getString("PRODUCT_DESCRIPTION"));
        //重さ
        TextView product_weight_view= view.findViewById(R.id.weight_confirmation);
        product_weight_view.setText(getArguments().getString("PRODUCT_WEIGHT"));
        //金額
        TextView product_price_view= view.findViewById(R.id.price_confirmation);
        product_price_view.setText(getArguments().getString("PRODUCT_PRICE"));
        //レシピURL
        TextView recipe_url_view= view.findViewById(R.id.recipe_url_confirmation);
        recipe_url_view.setText(getArguments().getString("RECIPE_URL"));
        //産地
        TextView product_area= view.findViewById(R.id.product_area_confirmation);
        product_area.setText(getArguments().getString("PRODUCT_AREA"));
        //配達方法
        TextView delivery_method= view.findViewById(R.id.delivery_confirmation);
        delivery_method.setText(getArguments().getString("DELIVERY_METHOD"));

        // 確認完了ボタンを取得
        Button nextButton = view.findViewById(R.id.next_button_exhibit_info_com);
        // 確認完了ボタンをクリックした時の処理
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentViewExhibitCompletedに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_confirmation_to_completed);
            }
        });
        //編集へ戻るボタンを取得
        Button backButton = view.findViewById(R.id.back_button_exhibit_info_com);
        // 編集へ戻るボタンをクリックした時の処理
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentViewExhibitInfoConfirmationに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_confirmation_to_entry);
            }
        });

        return view;
    }
}
