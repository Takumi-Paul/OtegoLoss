/*
12/29
Kobayashi
出品者画面を生成するプログラム
 */

package com.example.otegoloss.shipping;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.otegoloss.GridAdapter;
import com.example.otegoloss.GridAdapterOfShipping;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.home.ViewProduct;

import java.util.ArrayList;
import java.util.List;

public class ShippingFragment extends Fragment {
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
    //出品日
    private String[]  listingDate= new String[]{
            "2022/01/07", "2021/12/24", "2021/11/10"
    };
    private List<Integer> imgList = new ArrayList<>();

    //画面表示
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);

        // 所属している親アクティビティを取得
        MainActivity activity = (MainActivity) getActivity();
        // アクションバーにタイトルをセット
        activity.setTitle("出品履歴");
        // 戻るボタンは表示にする
        activity.setupBackButton(true);

        // ボタン要素を取得
        Button buttonYetSoldOutHistory = view.findViewById(R.id.button_yetsoldout_sold_out_history);
        // 完売済みボタンをクリックした時の処理
        buttonYetSoldOutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragment_view_yet_sold_out_historyに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_navigation_shipping_to_navigation_yet_sold_out_history);
            }
        });
        //以下Grid表示についての記述
        for (String productName: productNames){
            int imageId = getResources().getIdentifier(productName,"drawable", getActivity().getPackageName());
            imgList.add(imageId);
        }

        // GridViewのインスタンスを生成
        GridView gridview = view.findViewById(R.id.grid_view_sold_out_history);
        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル grid_items.xml を
        // fragment_home.xml に inflate するためにGridAdapterに引数として渡す
        GridAdapterOfShipping adapter = new GridAdapterOfShipping(getActivity().getApplicationContext(),
                R.layout.grid_items_of_shipping,
                imgList,
                productNames,
                prices,
                listingDate
        );
        // gridViewにadapterをセット
        gridview.setAdapter(adapter);
        // item clickのListenerをセット
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // bundleに受け渡したい値を保存
                Bundle bundle = new Bundle();
                // 画像ID
                bundle.putInt("IMAGEID", imgList.get(position));
                // 商品ID
                bundle.putInt("PRODUCT_ID", productID[position]);

                Navigation.findNavController(view).navigate(R.id.action_navigation_shipping_to_navigation_sold_out_product, bundle);
            }
        });
        return view;
    }
}