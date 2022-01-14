package com.example.otegoloss.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;


import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.databinding.FragmentUserBinding;

import java.util.Objects;

public class ProfileConfigFragment extends Fragment {

    private EditText profile_username;

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

        profile_username = (EditText)view.findViewById(R.id.userName_editText) ;

        Button inputCompleteButton = view.findViewById(R.id.inputComplete_button);
        inputCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profile_usernames = profile_username.getText().toString();
                Navigation.findNavController(view).navigate(R.id.action_profile_config_to_navigation_user);
            }
        });

        return view;
    }

}
