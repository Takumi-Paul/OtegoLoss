package com.example.otegoloss.shipping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.R;

public class ViewChangeListingInfoCompletedFragment extends Fragment {
    //画面表示
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_change_listing_info_completed, container, false);


        // ボタン要素を取得
        Button nextButton = view.findViewById(R.id.next_button_change_completed);

        // 完売済みボタンをクリックした時の処理
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HomeFragmentに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_navigation_view_change_listing_info_completed_to_navigation_yet_sold_out_history);
            }
        });
        return view;
    }
}
