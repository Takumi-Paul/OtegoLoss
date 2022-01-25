package com.example.otegoloss.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;


import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.databinding.FragmentUserBinding;

import java.util.Objects;

public class ProfileConfigFragment extends Fragment {

    private EditText config_username;
    private EditText config_usermessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_profile_config, container, false);


        // ユーザ名の定義
        config_username = (EditText)view.findViewById(R.id.userName_editText) ;

        // プロフィールメッセージの定義
        config_usermessage = (EditText)view.findViewById(R.id.profileMessage_editText) ;

        Button inputCompleteButton = view.findViewById(R.id.inputComplete_button);
        inputCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String config_usernames = config_username.getText().toString();

                String config_usermessages = config_usermessage.getText().toString();

                Navigation.findNavController(view).navigate(R.id.action_profile_config_to_navigation_user);
            }
        });

        return view;
    }

}
