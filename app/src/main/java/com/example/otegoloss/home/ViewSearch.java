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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.ChangeBackgraund;
import com.example.otegoloss.R;

public class ViewSearch extends Fragment {

    private EditText productName;
    private EditText sellerName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_search, container, false);
        LinearLayout background_view = view.findViewById(R.id.background);
        ChangeBackgraund.changeBackGround(background_view);

        productName = (EditText)view.findViewById(R.id.kensaku_shouhin_name);
        sellerName = (EditText)view.findViewById(R.id.kensaku_shuppin_name);


        //検索ボタンが押された時の処理
        Button SearchButton  = view.findViewById(R.id.search_button);


        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //商品名と出品者名を取得
                //String productNames = productName.getText().toString();
                //String sellerNames =  sellerName.getText().toString();

                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_fragmentresult);
            }
        });



        return view;
    }

}