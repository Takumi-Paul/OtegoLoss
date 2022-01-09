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
import com.example.otegoloss.home.HomeFragment;

public class ViewExhibitCompletedFragment extends Fragment{
    //画面表示
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_exhibit_completed, container, false);

        // 所属している親アクティビティを取得
        MainActivity activity = (MainActivity) getActivity();
        // アクションバーにタイトルをセット
        activity.setTitle("出品完了");
        // 戻るボタンは非表示にする
        activity.setupBackButton(false);

        // ボタン要素を取得
        Button nextButton = view.findViewById(R.id.next_button_exhibit_completed);

        // 完売済みボタンをクリックした時の処理
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HomeFragmentに遷移させる
                FragmentManager fm_Home = getChildFragmentManager();
                FragmentTransaction t_Home= fm_Home.beginTransaction();
                // 次のFragment
                Fragment secondFragment = new HomeFragment();
                // fragmentManagerに次のfragmentを追加
                t_Home.add(R.id.fragmentViewExhibitCompleted, secondFragment);
                // 画面遷移戻りを設定
                t_Home.addToBackStack(null);
                // 画面遷移
                t_Home.commit();
            }
        });
        return view;
    }
}
