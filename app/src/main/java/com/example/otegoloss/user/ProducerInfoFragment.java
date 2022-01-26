package com.example.otegoloss.user;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

public class ProducerInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_producer_information, container, false);

        EditText phoneNumberEditText = (EditText)view.findViewById(R.id.phoneNumber_editText);
        EditText banknameEditText = (EditText)view.findViewById(R.id.bankname_editText);
        EditText bankbranchnameEditText = (EditText)view.findViewById(R.id.bankbranchname_editText);
        EditText accountNameEditText = (EditText)view.findViewById(R.id.accountName_editText);

        Button proRegisterButton = view.findViewById(R.id.proRegister_button);
        proRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumberEditTexts = phoneNumberEditText.getText().toString();
                String banknameEditTexts = banknameEditText.getText().toString();
                String bankbranchnameEditTexts = bankbranchnameEditText.getText().toString();
                String accountNameEditTexts = accountNameEditText.getText().toString();

                if(phoneNumberEditTexts.equals("") || banknameEditTexts.equals("") || bankbranchnameEditTexts.equals("") || accountNameEditTexts.equals("")){
                    Toast.makeText(view.getContext(), "入力欄に不備があります", Toast.LENGTH_LONG).show();
                } else if (banknameEditTexts.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠]*$") && phoneNumberEditTexts.matches("^[0-9]*$") && bankbranchnameEditTexts.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠]*$") && accountNameEditTexts.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠]*$")) {
                    Navigation.findNavController(view).navigate(R.id.shipping_addr_info_regist_comp);
                } else {
                    Toast.makeText(view.getContext(), "入力欄に不備があります", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

}
