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
import android.widget.Toast;

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

        // 電話番号の定義
        EditText phone_number= (EditText) view.findViewById(R.id.proPhoneNumber_editText);

        // 郵便番号の定義
        EditText postal_code = (EditText) view.findViewById(R.id.addressNumber_editText);

        // 市町村区の定義
        EditText area_city = (EditText) view.findViewById(R.id.postalAddress_editText);

        // 丁目・番地・号の定義
        EditText area_number = (EditText) view.findViewById(R.id.homeNumber_editText);

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
                String fullnames = fullname.getText().toString();
                String phone_numbers = phone_number.getText().toString();
                String postal_codes = postal_code.getText().toString();
                String area_citys = area_city.getText().toString();
                String area_numbers = area_number.getText().toString();

                if (fullnames.equals("") || phone_numbers.equals("") || postal_codes.equals("") || area_citys.equals("") || area_numbers.equals("")){
                    Toast.makeText(view.getContext(), "入力欄に不備があります", Toast.LENGTH_LONG).show();
                } else if (fullnames.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠]*$") && phone_numbers.matches("^[0-9]*$")&& postal_codes.matches("^[0-9]*$")) {
                    Navigation.findNavController(view).navigate(R.id.shipping_addr_info_regist_comp);
                } else {
                    Toast.makeText(view.getContext(), "入力欄に不備があります", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

}
