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
    private EditText config_userid;
    private EditText config_usermessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_profile_config, container, false);


        // ユーザ名の定義
        config_username = (EditText)view.findViewById(R.id.userName_editText) ;
        // 文字数制限(10文字)
        InputFilter[] config_usernamefilters = new InputFilter[1];
        config_usernamefilters[0] = new InputFilter.LengthFilter(10);
        // 配列をセットする
        config_username.setFilters(config_usernamefilters);

        // ユーザIDの定義
        config_userid = (EditText)view.findViewById(R.id.userId_editText) ;
        // 文字数制限(8文字)
        InputFilter[] config_useridfilters = new InputFilter[1];
        config_useridfilters[0] = new InputFilter.LengthFilter(8);
        // 半角英数字のみ
        InputFilter config_useridinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[a-zA-Z0-9]+$")) {
                    config_userid.setFilters(config_useridfilters);

                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        config_userid.setFilters(new InputFilter[]{config_useridinputFilter});

        // プロフィールメッセージの定義
        config_usermessage = (EditText)view.findViewById(R.id.profileMessage_editText) ;
        // 文字数制限(400文字)
        InputFilter[] config_usermessagefilters = new InputFilter[1];
        config_usermessagefilters[0] = new InputFilter.LengthFilter(200);
        // 配列をセットする
        config_usermessage.setFilters(config_usermessagefilters);

        Button inputCompleteButton = view.findViewById(R.id.inputComplete_button);
        inputCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String config_usernames = config_username.getText().toString();
                Toast.makeText(view.getContext(), config_usernames, Toast.LENGTH_LONG).show();

                //String config_userids = config_userid.getText().toString();
                //Toast.makeText(view.getContext(), config_userids, Toast.LENGTH_LONG);.show();

                //String config_usermessages = config_usermessage.getText().toString();
                //Toast.makeText(view.getContext(), config_usermessages, Toast.LENGTH_LONG);.show();

                Navigation.findNavController(view).navigate(R.id.action_profile_config_to_navigation_user);
            }
        });

        return view;
    }

}
