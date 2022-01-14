package com.example.otegoloss.shipping;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

public class ShippingProcedureFragment extends Fragment {
    //購入者
    private String purchaser_name = "岩田誠";
    //購入品
    private String product_name = "tomato";
    //住所
    private String address = "高知市万々111111-11111-2--1212111";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_shipping_procedure, container, false);

        //購入者
        TextView purchase_name_view = view.findViewById(R.id.purchase_name_view_shipping_product);
        purchase_name_view.setText(purchaser_name);
        //商品名
        TextView product_name_view = view.findViewById(R.id.product_name_view_shipping_product);
        product_name_view.setText(product_name);
        //住所
        TextView address_view = view.findViewById(R.id.address_view_shipping_product);
        address_view.setText(address);

        return view;
    }
}
