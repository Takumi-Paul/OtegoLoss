package com.example.otegoloss.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.otegoloss.R;

public class FavoriteUserView extends Fragment {

    private String[] favoriteUserNames = {
            "佐藤", "田中"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_view_favorite_user, container, false);

        ListView favoriteUserList = view.findViewById(R.id.favoriteUser_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1,
                favoriteUserNames);

        favoriteUserList.setAdapter(arrayAdapter);

        return view;
    }
}
