package com.example.otegoloss.user;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

public class ProducerInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_producer_information, container, false);

        // メアドの定義
        EditText proEmailAddressEditText = (EditText) view.findViewById(R.id.proEmailAddress_editText);
        // 文字数制限(30文字)
        InputFilter[] proEmailAddressEditTextfilters = new InputFilter[1];
        proEmailAddressEditTextfilters[0] = new InputFilter.LengthFilter(30);
        // 半角英大文字のみ
        InputFilter proEmailAddressEditTextinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[a-zA-Z]+$")) {
                    proEmailAddressEditText.setFilters(proEmailAddressEditTextfilters);

                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        proEmailAddressEditText.setFilters(new InputFilter[]{proEmailAddressEditTextinputFilter});

        // 電話番号の定義
        EditText phoneNumberEditText= (EditText) view.findViewById(R.id.phoneNumber_editText);
        // 文字数制限(11桁)
        InputFilter[] phoneNumberEditTextTextfilters = new InputFilter[1];
        phoneNumberEditTextTextfilters[0] = new InputFilter.LengthFilter(11);
        // 数字のみ
        InputFilter phoneNumberEditTextinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[0-9]+$")) {
                    phoneNumberEditText.setFilters(phoneNumberEditTextTextfilters);

                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        phoneNumberEditText.setFilters(new InputFilter[]{phoneNumberEditTextinputFilter});


        // メアドの定義
        EditText accountNameEditText = (EditText) view.findViewById(R.id.accountName_editText);
        // 文字数制限(30文字)
        InputFilter[] accountNameEditTextTextfilters = new InputFilter[1];
        accountNameEditTextTextfilters[0] = new InputFilter.LengthFilter(30);
        // カタカナのみ
        InputFilter accountNameEditTextTextinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[ア-ン]+$")) {
                    accountNameEditText.setFilters(accountNameEditTextTextfilters);

                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        accountNameEditText.setFilters(new InputFilter[]{accountNameEditTextTextinputFilter});

        return view;
    }

}
