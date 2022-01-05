/*
12/29
Kobayashi
出品者画面を生成するプログラム
 */

package com.example.otegoloss.shipping;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.otegoloss.R;

public class ShippingFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_shipping, container, false);
    }

}