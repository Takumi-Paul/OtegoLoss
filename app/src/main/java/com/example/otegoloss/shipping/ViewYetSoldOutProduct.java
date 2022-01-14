//出品商品詳細（未完売）
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

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;


public class ViewYetSoldOutProduct extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_yet_sold_out_product, container, false);

        // 所属している親アクティビティを取得
        MainActivity activity = (MainActivity) getActivity();
        // アクションバーにタイトルをセット
        activity.setTitle("出品情報詳細");
        // 戻るボタンは表示
        activity.setupBackButton(true);

        // ボタンを取得
        Button changeButton = view.findViewById(R.id.content_change_button_view_yet_sold_out_product);
        // 商品情報変更ボタンをクリックした時の処理
        changeButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentChangeListingに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_navigation_yet_sold_out_history_to_navigation_yet_sold_out_product);
            }
        });

        Button deleteButton = view.findViewById(R.id.content_delete_button_view_yet_sold_out_product);
        // 削除ボタンをクリックした時の処理
        deleteButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentDeleteProductに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_navigation_yet_sold_out_history_to_navigation_yet_sold_out_product);
            }
        });

        return view;
    }
}
