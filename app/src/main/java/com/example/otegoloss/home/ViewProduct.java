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
import android.content.Intent;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.otegoloss.R;

public class ViewProduct extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_product, container, false);
        Bundle bundle = getArguments();
        int imageId = bundle.getInt("IMAGEID", 0);
        System.out.println(imageId);
        ImageView imageView = view.findViewById(R.id.productImage_imageView);
        imageView.setImageResource(imageId);
        return view;
    }

}