/*
12/29
Kobayashi
ユーザ画面を生成するプログラム
 */

package com.example.otegoloss.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;
import com.example.otegoloss.databinding.FragmentUserBinding;

import java.util.Objects;

public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // 所属している親アクティビティを取得
        MainActivity activity = (MainActivity) getActivity();
        // 戻るボタンは非表示にする（MainFragmentでは戻るボタン不要）
        // ここをfalseにしておかないとサブフラグメントから戻ってきた際に戻るボタンが表示されたままになってしまう
        activity.setupBackButton(false);

        // ボタン要素を取得
        Button bt1 = view.findViewById(R.id.nextSettingProfile_button);
        Button bt2 = view.findViewById(R.id.nextMeter_button);
        Button bt3 = view.findViewById(R.id.nextSettingCredit_button);
        Button bt4 = view.findViewById(R.id.nextSettingAddress_button);
        Button bt5 = view.findViewById(R.id.nextProcedureRegister_button);
        Button bt6 = view.findViewById(R.id.nextInformation_button);
        Button bt7 = view.findViewById(R.id.nextDeleteAccount_button);

        // ①ボタンをクリックした時の処理
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SubFragment1に遷移させる
                replaceFragment(new ProfileConfigFragment());
                bt1.setVisibility(View.GONE);
                bt2.setVisibility(View.GONE);
                bt3.setVisibility(View.GONE);
                bt4.setVisibility(View.GONE);
                bt5.setVisibility(View.GONE);
                bt6.setVisibility(View.GONE);
                bt7.setVisibility(View.GONE);
            }
        });

        return view;
    }

    // 表示させるFragmentを切り替えるメソッドを定義（表示したいFragmentを引数として渡す）
    private void replaceFragment(Fragment fragment) {
        // フラグメントマネージャーの取得
        FragmentManager manager = getFragmentManager(); // アクティビティではgetSupportFragmentManager()?
        // フラグメントトランザクションの開始
        FragmentTransaction transaction = Objects.requireNonNull(manager).beginTransaction();
        // レイアウトをfragmentに置き換え（追加）
        transaction.replace(R.id.fragmentUser, fragment);
        // 置き換えのトランザクションをバックスタックに保存する
        transaction.addToBackStack(null);
        // フラグメントトランザクションをコミット
        transaction.commit();
    }

}