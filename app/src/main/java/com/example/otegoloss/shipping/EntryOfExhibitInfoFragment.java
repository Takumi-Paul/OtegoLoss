//出品情報入力画面
package com.example.otegoloss.shipping;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;


public class EntryOfExhibitInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_entry_of_exhibit_info, container, false);

        // 所属している親アクティビティを取得
        MainActivity activity = (MainActivity) getActivity();
        // アクションバーにタイトルをセット
        activity.setTitle("出品情報入力");
        // 戻るボタンは表示
        activity.setupBackButton(true);
        // 入力完了ボタンを取得
        Button buttonNext= view.findViewById(R.id.next_button_entry_exhibit_product);
        // 入力完了ボタンをクリックした時の処理
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentViewExhibitInfoConfirmationに遷移させる
                FragmentManager fm_ViewExhibitInfoConfirmation = getChildFragmentManager();
                FragmentTransaction t_ViewExhibitInfoConfirmation= fm_ViewExhibitInfoConfirmation.beginTransaction();
                // 次のFragment
                Fragment secondFragment = new ShippingFragment();
                // fragmentManagerに次のfragmentを追加
                t_ViewExhibitInfoConfirmation.add(R.id.fragmentEntryOfExhibitInfo, secondFragment);
                // 画面遷移戻りを設定
                t_ViewExhibitInfoConfirmation.addToBackStack(null);
                // 画面遷移
                t_ViewExhibitInfoConfirmation.commit();
            }
        });

        return view;
    }


}
