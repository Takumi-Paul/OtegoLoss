package com.example.otegoloss.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.databinding.FragmentUserBinding;

import java.util.Objects;

public class ProfileConfigFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_profile_config, container, false);

        // 所属親アクティビティを取得
        MainActivity activity = (MainActivity) getActivity();
        // 戻るボタンを表示する
        activity.setupBackButton(true);

        // この記述でフラグメントでアクションバーメニューが使えるようになる
        setHasOptionsMenu(true);

        Button inputCompleteButton = view.findViewById(R.id.inputComplete_button);
        inputCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ;
            }
        });

        return view;
    }

}
