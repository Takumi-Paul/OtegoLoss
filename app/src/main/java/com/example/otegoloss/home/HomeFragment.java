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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.GridView;
import com.example.otegoloss.GridAdapter;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.user.ProfileConfigFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    // 商品名配列
    private String[] productNames = {
            "tomato", "carrot", "radish"
    };
    // 価格配列
    private int[] prices = {
            100, 200, 300
    };
    // 商品ID
    private int[] productID = new int[]{
            0, 1, 2
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
        // fragment_home.xml に inflate するためにGridAdapterに引数として渡す
        GridAdapter adapter = new GridAdapter(getActivity().getApplicationContext(),
                R.layout.grid_items,
                imgList,
                productNames,
                prices
        );
        // gridViewにadapterをセット
        gridview.setAdapter(adapter);
        // item clickのListenerをセット
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction t = fm.beginTransaction();

                // bundleに受け渡したい値を保存
                Bundle bundle = new Bundle();
                // 画像ID
                bundle.putInt("IMAGEID", imgList.get(position));
                // 商品ID
                bundle.putInt("PRODUCT_ID", productID[position]);
                // 次のFragment
                Fragment secondFragment = new ViewProduct();
                // bundleを次のfragmentに設定
                secondFragment.setArguments(bundle);
                // fragmentManagerに次のfragmentを追加
                t.add(R.id.fragmentHome, secondFragment);
                // 画面遷移戻りを設定
                t.addToBackStack(null);
                // 画面遷移
                t.commit();
            }
        });

        return view;
    }


}