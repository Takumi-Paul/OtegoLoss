package com.example.otegoloss.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

public class ViewComment extends Fragment {

    private EditText comment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_comment, container, false);

        comment = (EditText)view.findViewById(R.id.comment_editText);

        Button SendCommentButton = view.findViewById(R.id.sendComment_button);

        SendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comments = comment.getText().toString();
                //確認用
                Toast.makeText(view.getContext(), comments, Toast.LENGTH_LONG).show();
            }
        });

        // 所属親アクティビティを取得
        MainActivity activity = (MainActivity) getActivity();
        // 戻るボタンを表示する
        activity.setupBackButton(true);

        // この記述でフラグメントでアクションバーメニューが使えるようになる
        setHasOptionsMenu(true);

        return view;
    }

}
