/*
12/29
Kobayashi
ホーム画面を生成するプログラム
 */

package com.example.otegoloss.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.GridView;
import com.example.otegoloss.GridAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.user.ProfileConfigFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private String[] productNames = {
            "tomato", "carrot"
    };

    private List<Integer> imgList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // for-each member名をR.drawable.名前としてintに変換してarrayに登録
        for (String productName: productNames){
            int imageId = getResources().getIdentifier(productName,"drawable", getActivity().getPackageName());
            imgList.add(imageId);
        }

        // GridViewのインスタンスを生成
        GridView gridview = view.findViewById(R.id.product_gridView);
        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル grid_items.xml を
        // activity_main.xml に inflate するためにGridAdapterに引数として渡す
        GridAdapter adapter = new GridAdapter(getActivity().getApplicationContext(),
                R.layout.grid_items,
                imgList,
                productNames
        );
        gridview.setAdapter(adapter);


        return view;
    }


}