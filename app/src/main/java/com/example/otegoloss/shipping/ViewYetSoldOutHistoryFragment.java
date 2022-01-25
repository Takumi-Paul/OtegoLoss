//出品履歴（未完売）
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

import com.example.otegoloss.GridAdapterOfShipping;
import com.example.otegoloss.GridAdapterOfYetSoldOut;
import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.user.ProfileConfigFragment;

import java.util.ArrayList;
import java.util.List;


public class ViewYetSoldOutHistoryFragment extends Fragment {
    // 商品名配列
    private String[] productNames = {
            "carrot", "radish","tomato"
    };
    // 価格配列
    private int[] prices = {
            100, 200, 300
    };
    // 商品ID
    private String[] productID = new String[]{
            "g0000004", "g0000005", "g0000006"
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
        View view = inflater.inflate(R.layout.fragment_view_yet_sold_out_history, container, false);

        //以下Grid表示についての記述
        for (String productName: productNames){
            int imageId = getResources().getIdentifier(productName,"drawable", getActivity().getPackageName());
            imgList.add(imageId);
        }

        // GridViewのインスタンスを生成
        GridView gridview = view.findViewById(R.id.grid_view_yet_sold_out_history);
        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル grid_items.xml を
        // fragment_view_yet_sold_out_history.xml に inflate するためにGridAdapterに引数として渡す
        GridAdapterOfYetSoldOut adapter = new GridAdapterOfYetSoldOut(getActivity().getApplicationContext(),
                R.layout.grid_items_of_yet_history,
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
                bundle.putString("PRODUCT_ID", productID[position]);
                //商品名
                bundle.putString("PRODUCT_NAME",productNames[position]);

                Navigation.findNavController(view).navigate(R.id.action_navigation_yet_sold_out_history_to_navigation_yet_sold_out_product, bundle);
            }
        });

        // ボタン要素を取得
        Button buttonSoldOutHistory = view.findViewById(R.id.button_soldout_sold_out_history);
        // 完売済みボタンをクリックした時の処理
        buttonSoldOutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentShippingに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_navigation_yet_sold_out_history_to_navigation_shipping);
            }
        });
        return view;
    }
}
