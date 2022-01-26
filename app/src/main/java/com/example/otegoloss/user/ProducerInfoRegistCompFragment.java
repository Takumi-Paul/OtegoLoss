package com.example.otegoloss.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.R;

public class ProducerInfoRegistCompFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_pay_info_regist_comp, container, false);

        Button nexthomeRegistButton3 = view.findViewById(R.id.nexthome_regist_button3);
        nexthomeRegistButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(view).navigate(R.id.action_producer_info_regist_comp_to_navigation_home);
            }
        });

        return view;
    }

}
