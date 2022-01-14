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

import com.example.otegoloss.ChangeBackgraund;
import com.example.otegoloss.R;

public class ViewInputShippingAddress extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_view_input_shipping_address, container, false);



        //「住所を追加」を押したときの処理
        Button AddAddressButton  = view.findViewById(R.id.addAddress_button);

        AddAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputShippingAddress_to_shipping_addr_info_regist_config);
            }
        });

        //「住所を削除」を押したときの処理
        Button DeleteAddressButton  = view.findViewById(R.id.deleteAddress_button);

        DeleteAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputShippingAddress_to_shipping_addr_info_delete);
            }
        });



        //「次へ」ボタンが押された時の処理
        Button NextButtonShipping = view.findViewById(R.id.next_button_shipping);

        NextButtonShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentViewInputShippingAddress_to_purchase_information1);

                //FragmentManager fm_NextButtonShipping = getChildFragmentManager();
                //FragmentTransaction t_NextButtonShipping  =  fm_NextButtonShipping.beginTransaction();
                // 次のFragment
                //Fragment secondFragment = new ViewPurchaseInformation1();
                // fragmentManagerに次のfragmentを追加
                //t_NextButtonShipping.add(R.id.fragmentViewInputShippingAddress, secondFragment);
                // 画面遷移戻りを設定
                //t_NextButtonShipping.addToBackStack(null);
                // 画面遷移
                //t_NextButtonShipping.commit();
            }
        });

        return view;
    }

}