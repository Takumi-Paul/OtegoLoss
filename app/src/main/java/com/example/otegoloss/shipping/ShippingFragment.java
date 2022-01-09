/*
12/29
Kobayashi
出品者画面を生成するプログラム
 */

package com.example.otegoloss.shipping;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

public class ShippingFragment extends Fragment {

    //画面表示
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);

        // 所属している親アクティビティを取得
        MainActivity activity = (MainActivity) getActivity();
        // アクションバーにタイトルをセット
        activity.setTitle("出品履歴");
        // 戻るボタンは表示にする
        activity.setupBackButton(true);

        // ボタン要素を取得
        Button buttonYetSoldOutHistory = view.findViewById(R.id.button_yetsoldout_sold_out_history);
        // 完売済みボタンをクリックした時の処理
        buttonYetSoldOutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragment_view_yet_sold_out_historyに遷移させる
                FragmentManager fm_ViewYetSoldOutHistory = getChildFragmentManager();
                FragmentTransaction t_ViewYetSoldOutHistory  =  fm_ViewYetSoldOutHistory.beginTransaction();
                // 次のFragment
                Fragment secondFragment = new ViewYetSoldOutHistoryFragment();
                // fragmentManagerに次のfragmentを追加
                t_ViewYetSoldOutHistory.add(R.id.fragmentShipping, secondFragment);
                // 画面遷移戻りを設定
                t_ViewYetSoldOutHistory.addToBackStack(null);
                // 画面遷移
                t_ViewYetSoldOutHistory.commit();
            }
        });
        return view;
    }
}