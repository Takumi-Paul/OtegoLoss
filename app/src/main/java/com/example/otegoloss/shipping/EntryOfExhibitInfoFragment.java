//出品情報入力画面
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;


public class EntryOfExhibitInfoFragment extends Fragment {
    //入力情報保持用の宣言
    private EditText pro_name;
    private EditText pro_description;
    private EditText weight;
    private EditText price;
    private EditText recipe_url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_entry_of_exhibit_info, container, false);

        // 所属している親アクティビティを取得
        MainActivity activity = (MainActivity) getActivity();
        // アクションバーにタイトルをセット
        activity.setTitle("出品情報入力");
        // 戻るボタンは表示
        activity.setupBackButton(true);

        //入力情報保持用のインスタンス生成
        pro_name = (EditText) view.findViewById(R.id.product_name_entry_exhibit_product);
        pro_description= (EditText) view.findViewById(R.id.product_description_entry_exhibit_product);
        weight = (EditText) view.findViewById(R.id.product_weight_entry_exhibit_product);
        price = (EditText) view.findViewById(R.id.amount_entry_exhibit_product2);
        recipe_url = (EditText) view.findViewById(R.id.recipe_url_entry_exhibit_product);


        // 入力完了ボタンを取得
        Button buttonNext= view.findViewById(R.id.next_button_entry_exhibit_product);
        // 入力完了ボタンをクリックした時の処理
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //入力情報を格納する
                String pro_names = pro_name.getText().toString();
                String pro_descriptions = pro_description.getText().toString();
                String weights = weight.getText().toString();
                String prices = price.getText().toString();
                String recipe_urls = recipe_url.getText().toString();

                //次のフラグメントにBundleを使ってデータを渡す
                //タイトル
                Bundle bundle = new Bundle();
                bundle.putString("pro_names", pro_names);
                bundle.putString("pro_descriptions", pro_descriptions);
                bundle.putString("weights", weights);
                bundle.putString("prices", prices);
                bundle.putString("recipe_urls", recipe_urls);

                // fragmentViewExhibitInfoConfirmationに遷移させる
                Navigation.findNavController(view).navigate(R.id.action_entry_to_confirmation);
            }
        });



        return view;
    }


}
