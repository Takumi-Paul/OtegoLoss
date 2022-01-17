package com.example.otegoloss.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

import java.util.Locale;
import java.util.Objects;

public class CreditcardRegistFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_creditcard_regist, container, false);


        // クレカ名義人の定義
        EditText cardNameEditText = (EditText)view.findViewById(R.id.cardName_editText);

        // クレカ番号の定義
        EditText cardNumberEditText = (EditText)view.findViewById(R.id.cardNumber_editText);

        // セキュリティコードの定義
        EditText cardCodeEditText = (EditText)view.findViewById(R.id.cardCode_editText);

        Button inputCreditButton = view.findViewById(R.id.inputCredit_button);
        inputCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNameEditTexts = cardNameEditText.getText().toString();
                if(cardNameEditTexts.matches("[A-Z]")) {
                    Navigation.findNavController(view).navigate(R.id.action_creditcard_regist_to_pay_info_regist_comp);
                } else {
                    Toast.makeText(view.getContext(), cardNameEditTexts, Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

}
