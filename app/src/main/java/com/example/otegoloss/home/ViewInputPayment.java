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

import com.example.otegoloss.R;

public class ViewInputPayment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_input_payment, container, false);


        //「次へ」ボタンが押された時の処理
        Button NextButtonPayment = view.findViewById(R.id.next_button_payment);

        NextButtonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(view).navigate(R.id.action_fragmentProduct_to_purchase_information1);

                FragmentManager fm_ViewPurchaseInformation1 = getChildFragmentManager();
                FragmentTransaction t_ViewPurchaseInformation1  =  fm_ViewPurchaseInformation1.beginTransaction();
                // 次のFragment
                Fragment secondFragment = new ViewPurchaseInformation1();
                // fragmentManagerに次のfragmentを追加
                t_ViewPurchaseInformation1.replace(R.id.fragmentViewInputPayment, secondFragment);
                // 画面遷移戻りを設定
                t_ViewPurchaseInformation1.addToBackStack(null);
                // 画面遷移
                t_ViewPurchaseInformation1.commit();
            }
        });

        return view;
    }

}