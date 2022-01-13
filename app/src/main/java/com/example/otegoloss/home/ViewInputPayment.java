/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.otegoloss.R;

public class ViewInputPayment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_input_payment, container, false);



        //「決済情報を追加」ボタンが押された時の処理
        Button AddCreditButton = view.findViewById(R.id.addCredit_button);

        AddCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputPayment_to_creditcard_regist);
            }
        });


        //「決済情報を削除」ボタンが押された時の処理
        Button DeleteCreditButton = view.findViewById(R.id.deleteCredit_button);

        DeleteCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputPayment_to_pay_info_delete);
            }
        });

        //「次へ」ボタンが押された時の処理
        Button NextButtonPayment = view.findViewById(R.id.next_button_payment);

        NextButtonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputPayment_to_fragmentViewInputShippingAddress);
            }
        });

        return view;
    }

}