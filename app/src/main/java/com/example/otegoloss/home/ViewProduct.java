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
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.otegoloss.R;
import com.example.otegoloss.shipping.ViewYetSoldOutHistoryFragment;

public class ViewProduct extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_product, container, false);

        // BundleでHome画面の値を受け取り
        Bundle bundle = getArguments();
        // 画像ID
        int imageId = bundle.getInt("IMAGEID", 0);
        // 商品ID
        int productID = bundle.getInt("PRODUCT_ID", 0);

        // imageViewのIDを関連付けて画像を表示
        ImageView imageView = view.findViewById(R.id.productImage_imageView);
        imageView.setImageResource(imageId);

        Button buyButton = view.findViewById(R.id.buy_button);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Navigation.findNavController(view).navigate(R.id.action_fragmentProduct_to_fragmentViewInputPayment2);

            }
        });


        Button commentButton = view.findViewById(R.id.comment_button);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(view).navigate(R.id.action_fragmentProduct_to_fragmentViewComment2);

                //FragmentManager fm_ViewComment = getParentFragmentManager();
                //FragmentTransaction t_ViewComment  =  fm_ViewComment.beginTransaction();
                // 次のFragment
                //Fragment secondFragment = new ViewComment();
                // fragmentManagerに次のfragmentを追加
                //t_ViewComment.replace(R.id.fragmentProduct, secondFragment);
                // 画面遷移戻りを設定
                //t_ViewComment.addToBackStack(null);
                // 画面遷移
                //t_ViewComment.commit();
            }
        });


        return view;
    }

}