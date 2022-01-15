//出品商品詳細（未完売）
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;


public class ViewYetSoldOutProductFragment extends Fragment {
    //価格
    private String price = "100";
    //地域
    private String product_area = "高知県";
    //出品日
    private String listing_date = "20220107";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_yet_sold_out_product, container, false);

        // BundleでHome画面の値を受け取り
        Bundle bundle = getArguments();
        // 画像ID
        int imageId = bundle.getInt("IMAGEID", 0);
        // 商品ID
        int productID = bundle.getInt("PRODUCT_ID", 0);

        // imageViewのIDを関連付けて画像を表示
        ImageView imageView = view.findViewById(R.id.product_image_view_sold_out_product);
        imageView.setImageResource(imageId);

        //商品名を表示
        TextView product_name_view = view.findViewById(R.id.product_name_text_view_view_sold_out_product);
        product_name_view.setText(getArguments().getString("PRODUCT_NAME"));

        //価格を表示
        TextView prices = view.findViewById(R.id.price_yet_sold_out_product);
        prices.setText(price+"円");
        //地域を表示
        TextView product_areas = view.findViewById(R.id.product_area_yet_sold_out_product);
        product_areas.setText(product_area);
        //出品日を表示
        TextView listing_dates = view.findViewById(R.id.listing_date_yet_sold_out_product);
        listing_dates.setText(listing_date);

        // ボタンを取得
        Button changeButton = view.findViewById(R.id.content_change_button_view_yet_sold_out_product);
        // 商品情報変更ボタンをクリックした時の処理
        changeButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentChangeListingに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_navigation_yet_sold_out_product_to_navigation_change_listing_info);
            }
        });

        Button deleteButton = view.findViewById(R.id.content_delete_button_view_yet_sold_out_product);
        // 削除ボタンをクリックした時の処理
        deleteButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentDeleteProductに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_navigation_yet_sold_out_history_to_navigation_yet_sold_out_product);
            }
        });

        return view;
    }
}
