//出品履歴（未完売）
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
import com.example.otegoloss.user.ProfileConfigFragment;


public class ViewYetSoldOutHistoryFragment extends Fragment {
    //画面表示
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_yet_sold_out_history, container, false);

        // 所属している親アクティビティを取得
        MainActivity activity = (MainActivity) getActivity();
        // アクションバーにタイトルをセット
        activity.setTitle("出品履歴");
        // 戻るボタンは表示にする
        activity.setupBackButton(true);

        // ボタン要素を取得
        Button buttonSoldOutHistory = view.findViewById(R.id.button_soldout_sold_out_history);

        // 完売済みボタンをクリックした時の処理
        buttonSoldOutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentShippingに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_navigation_yet_sold_out_history_to_navigation_shipping);
            }
        });
        return view;
    }
}
