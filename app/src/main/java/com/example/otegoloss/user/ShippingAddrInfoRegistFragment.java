package com.example.otegoloss.user;

import android.os.Bundle;
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
    private EditText fullname;
    private EditText phone_number;
    private EditText postal_code;
    private String area_sap;
    private EditText area_city;
    private EditText area_number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_shipping_address_info_regist, container, false);

        fullname = (EditText) view.findViewById(R.id.name_editText);
        phone_number= (EditText) view.findViewById(R.id.proPhoneNumber_editText);
        postal_code = (EditText) view.findViewById(R.id.addressNumber_editText);
        area_city = (EditText) view.findViewById(R.id.postalAddress_editText);
        area_number = (EditText) view.findViewById(R.id.homeNumber_editText);

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
