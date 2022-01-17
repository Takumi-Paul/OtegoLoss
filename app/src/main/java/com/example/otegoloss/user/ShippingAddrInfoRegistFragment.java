package com.example.otegoloss.user;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

public class ShippingAddrInfoRegistFragment extends Fragment {
    //入力情報保持用の宣言
    private String area_sap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_shipping_address_info_regist, container, false);

        // 氏名の定義
        EditText fullname = (EditText) view.findViewById(R.id.name_editText);
        // 文字数制限(20文字)
        InputFilter[] fullnameTextfilters = new InputFilter[1];
        fullnameTextfilters[0] = new InputFilter.LengthFilter(20);

        // 半角英大文字のみ
        InputFilter fullnameinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[a-zA-Z]+$")) {
                    fullname.setFilters(fullnameTextfilters);

                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        fullname.setFilters(new InputFilter[]{fullnameinputFilter});

        // 電話番号の定義
        EditText phone_number= (EditText) view.findViewById(R.id.proPhoneNumber_editText);
        // 文字数制限(11桁)
        InputFilter[] phone_numberTextfilters = new InputFilter[1];
        phone_numberTextfilters[0] = new InputFilter.LengthFilter(11);
        // 数字のみ
        InputFilter phone_numberinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[0-9]+$")) {
                    phone_number.setFilters(phone_numberTextfilters);

                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        phone_number.setFilters(new InputFilter[]{phone_numberinputFilter});

        // 郵便番号の定義
        EditText postal_code = (EditText) view.findViewById(R.id.addressNumber_editText);
        // 文字数制限(7桁)
        InputFilter[] postal_codeTextfilters = new InputFilter[1];
        postal_codeTextfilters[0] = new InputFilter.LengthFilter(7);
        // 数字のみ
        InputFilter postal_codeinputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[0-9]+$")) {
                    postal_code.setFilters(postal_codeTextfilters);

                    return source;
                } else {
                    return "";
                }
            }
        };
        // 配列をセットする
        postal_code.setFilters(new InputFilter[]{postal_codeinputFilter});

        // 市町村区の定義
        EditText area_city = (EditText) view.findViewById(R.id.postalAddress_editText);
        // 文字数制限(20文字)
        InputFilter[] area_cityTextfilters = new InputFilter[1];
        area_cityTextfilters[0] = new InputFilter.LengthFilter(20);
        area_city.setFilters(area_cityTextfilters);

        // 丁目・番地・号の定義
        EditText area_number = (EditText) view.findViewById(R.id.homeNumber_editText);
        // 文字数制限(20文字)
        InputFilter[] area_numberTextfilters = new InputFilter[1];
        area_numberTextfilters[0] = new InputFilter.LengthFilter(20);
        area_number.setFilters(area_numberTextfilters);

        //都道府県のSpinner処理
        Spinner product_area_spinner = (Spinner)view.findViewById(R.id.prefecture_spinner);
        product_area_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter,
                                       View v, int position, long id) {
                String area_sap = (String)adapter.getSelectedItem();
                area_sap = area_sap;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button inputAddressButton = view.findViewById(R.id.inputAddress_button);
        inputAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //入力情報を格納する
                //String fullname = fullname.getText().toString();
                //String phone_number = phone_number.getText().toString();
                //String postal_code = postal_code.getText().toString();
                //String area_city = area_city.getText().toString();
                //String area_number = area_number.getText().toString();

                Navigation.findNavController(view).navigate(R.id.action_shipping_addr_info_regist_config_to_shipping_addr_info_regist_comp);
            }
        });

        return view;
    }

}
