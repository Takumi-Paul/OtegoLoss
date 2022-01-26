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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.GridAdapter;
import com.example.otegoloss.R;

import java.util.ArrayList;
import java.util.List;

public class ViewSearchResult extends Fragment {

    // 商品名配列
    private String[] productNames = {
            "tomato", "carrot", "radish"
    };
    // 価格配列
    private int[] prices = {
            100, 200000, 300
    };
    // 商品ID
    private int[] productID = new int[]{
            0, 1, 2
    };
    //生産者ID
    private String[] producerID = {
            "@KochiTarou", "@KawakamiSyoya", "@Iwasaki"
    };

    private List<Integer> imgList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

/*

        //商品一覧画面
        // for-each member名をR.drawable.名前としてintに変換してarrayに登録
        for (String productName: productNames){
            int imageId = getResources().getIdentifier(productName,"drawable", getActivity().getPackageName());
            imgList.add(imageId);
        }

        // GridViewのインスタンスを生成
        GridView gridview = view.findViewById(R.id.search_result_gridView);
        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル grid_items.xml を
        // fragment_home.xml に inflate するためにGridAdapterに引数として渡す
        GridAdapter adapter = new GridAdapter(getActivity().getApplicationContext(),
                R.layout.grid_items,
                imgList,
                productNames,
                prices,
                producerID
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
                // Navigation遷移
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_fragmentProduct, bundle);
            }
        });
*/
        return view;
    }


}