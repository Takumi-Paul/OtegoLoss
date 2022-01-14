package com.example.otegoloss.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.R;

public class ExhibitProfile extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_exhibit_profile, container, false);

        // BundleでFavoriteUser画面の値を受け取り
        Bundle bundle = getArguments();
        // 画像ID
        int userImage = bundle.getInt("PHOTO", 0);
        // 商品ID
        String userName = bundle.getString("USER_NAME", "");

        // imageViewのIDを関連付けて画像を表示
        ImageView imageView = view.findViewById(R.id.userImage);
        imageView.setImageResource(userImage);

        TextView nameText = view.findViewById(R.id.userNameTextView);
        nameText.setText(userName);


        //「レビューする」を押したときの処理
        Button ReviewButton = view.findViewById(R.id.reviewButton);

        ReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_exhibitProfile_to_reviewUser);
            }
        });

        return view;
    }
}
