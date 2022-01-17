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

        // 文字数制限
        InputFilter[] cardNameEditTextfilters = new InputFilter[1];
        cardNameEditTextfilters[0] = new InputFilter.LengthFilter(20);

        // 半角英大文字のみ
        InputFilter cardNameEditTextinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[A-Z]+$")) {
                    cardNameEditText.setFilters(cardNameEditTextfilters);
                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        cardNameEditText.setFilters(new InputFilter[]{cardNameEditTextinputFilter});


        // クレカ番号の定義
        EditText cardNumberEditText = (EditText)view.findViewById(R.id.cardNumber_editText);

        // 文字数制限
        InputFilter[] cardNumberEditTextfilters = new InputFilter[1];
        cardNumberEditTextfilters[0] = new InputFilter.LengthFilter(16);

        // 半角数字のみ
        InputFilter cardNumberEditTextinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[0-9]+$")) {
                    cardNumberEditText.setFilters(cardNumberEditTextfilters);
                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        cardNumberEditText.setFilters(new InputFilter[]{cardNumberEditTextinputFilter});


        // セキュリティコードの定義
        EditText cardCodeEditText = (EditText)view.findViewById(R.id.cardCode_editText);

        // 文字数制限
        InputFilter[] cardCodeEditTextfilters = new InputFilter[1];
        cardCodeEditTextfilters[0] = new InputFilter.LengthFilter(4);

        // 半角数字のみ
        InputFilter cardCodeEditTextinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[0-9]+$")) {
                    cardCodeEditText.setFilters(cardCodeEditTextfilters);
                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        cardCodeEditText.setFilters(new InputFilter[]{cardCodeEditTextinputFilter});

        Button inputCreditButton = view.findViewById(R.id.inputCredit_button);
        inputCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_creditcard_regist_to_pay_info_regist_comp);
            }
        });

        return view;
    }

}
