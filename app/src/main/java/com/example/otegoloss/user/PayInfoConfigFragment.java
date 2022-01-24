package com.example.otegoloss.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.databinding.FragmentUserBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class PayInfoConfigFragment extends Fragment {

    public TextView creditcard_company;
    public TextView creditcard_number;

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_pay_info_config, container, false);

        creditcard_company = view.findViewById(R.id.texiview_creditcard_company) ;
        creditcard_number = view.findViewById(R.id.textView_creditcard_number) ;

        Button addCreditButton = view.findViewById(R.id.addCredit_button);
        Button deleteCreditButton = view.findViewById(R.id.deleteCredit_button);

        addCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_payinfoconfig_to_creditcard_regist);
            }
        });

        deleteCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_payinfoconfig_to_pay_info_delete);
            }
        });

        return view;
    }

}
