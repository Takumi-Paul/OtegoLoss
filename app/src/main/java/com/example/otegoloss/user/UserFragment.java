/*
12/29
Kobayashi
ユーザ画面を生成するプログラム
 */

package com.example.otegoloss.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.databinding.FragmentUserBinding;
import com.example.otegoloss.shipping.EntryOfExhibitInfoFragment;

import java.util.Objects;

public class UserFragment extends Fragment {

    public TextView profile_username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_user, container, false);


        profile_username = view.findViewById(R.id.profileUserName_textView) ;


        Button nextSettingProfileButton = view.findViewById(R.id.nextSettingProfile_button);
        Button nextMeterButton = view.findViewById(R.id.nextMeter_button);
        Button nextSettingCreditButton = view.findViewById(R.id.nextSettingCredit_button);
        Button nextSettingAddressButton = view.findViewById(R.id.nextSettingAddress_button);
        Button nextProcedureRegisterButton = view.findViewById(R.id.nextProcedureRegister_button);
        Button nextInformationButton = view.findViewById(R.id.nextInformation_button);
        Button nextDeleteAccountButton = view.findViewById(R.id.nextDeleteAccount_button);

        nextSettingProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_profile_config);
            }
        });

        nextMeterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_meter);
            }
        });

        nextSettingCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_payinfoconfig);
            }
        });

        nextSettingAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_shipping_addr_info_config);
            }
        });

        nextProcedureRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_producer_agreement);
            }
        });

        nextInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_information);
            }
        });

        nextDeleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_accountdelete);
            }
        });


        return view;
    }

}