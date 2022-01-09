/*
12/29
Kobayashi
マイページ画面を生成するプログラム
 */

package com.example.otegoloss.mypage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.navigation.Navigation;
import android.widget.FrameLayout;
import com.example.otegoloss.MainActivity;

import com.example.otegoloss.R;
import com.example.otegoloss.shipping.EntryOfExhibitInfoFragment;

public class MypageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        Button purchaseHistoryButton = view.findViewById(R.id.purchaseHistory_button);
        Button favoriteUserViewButton = view.findViewById(R.id.favoriteUserView_button);

        purchaseHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigationを繋げる
                Navigation.findNavController(view).navigate(R.id.action_mypage_to_purchaseHistory);
            }
        });

        favoriteUserViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_mypage_to_favoriteUserView);
            }
        });


        return view;
    }

}