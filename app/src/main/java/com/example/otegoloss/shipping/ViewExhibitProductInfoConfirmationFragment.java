/*
出品関連、出品情報確認画面
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


public class ViewExhibitProductInfoConfirmationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_exhibit_info_confirmation, container, false);

        // 所属している親アクティビティを取得
        MainActivity activity = (MainActivity) getActivity();
        // アクションバーにタイトルをセット
        activity.setTitle("出品情報入力確認");
        // 戻るボタンは非表示
        activity.setupBackButton(false);
        // 確認完了ボタンを取得
        Button nextButton = view.findViewById(R.id.next_button_exhibit_info_com);
        // 確認完了ボタンをクリックした時の処理
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentViewExhibitCompletedに遷移させる
                FragmentManager fm_completed = getChildFragmentManager();
                FragmentTransaction t_completed = fm_completed.beginTransaction();
                // 次のFragment
                Fragment secondFragment = new ViewExhibitCompleted();
                // fragmentManagerに次のfragmentを追加
                t_completed.add(R.id.fragmentViewExhibitInfoConfirmation, secondFragment);
                // 画面遷移戻りを設定
                t_completed.addToBackStack(null);
                // 画面遷移
                t_completed.commit();
            }
        });
        //編集へ戻るボタンを取得
        Button backButton = view.findViewById(R.id.back_button_exhibit_info_com);
        // 編集へ戻るボタンをクリックした時の処理
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentViewExhibitInfoConfirmationに遷移させる
                FragmentManager fm_shipping = getChildFragmentManager();
                FragmentTransaction t_shipping = fm_shipping.beginTransaction();
                // 次のFragment
                Fragment secondFragment = new EntryOfExhibitInfoFragment();
                // fragmentManagerに次のfragmentを追加
                t_shipping.add(R.id.fragmentViewExhibitInfoConfirmation, secondFragment);
                // 画面遷移戻りを設定
                t_shipping.addToBackStack(null);
                // 画面遷移
                t_shipping.commit();
            }
        });

        return view;
    }
}
