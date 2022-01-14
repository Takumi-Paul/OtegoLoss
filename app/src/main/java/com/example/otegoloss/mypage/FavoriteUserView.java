package com.example.otegoloss.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;


import com.example.otegoloss.ListViewAdapter;
import com.example.otegoloss.R;

public class FavoriteUserView extends Fragment {

    private String[] favoriteUserNames = {
            "佐藤", "田中"
    };
    private static final int[] photos = {
            R.drawable.user, R.drawable.user
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_view_favorite_user, container, false);

        ListView favoriteUserList = view.findViewById(R.id.favoriteUser_list);
        BaseAdapter arrayAdapter = new ListViewAdapter(
                getActivity().getApplicationContext(), R.layout.list, favoriteUserNames, photos);

        favoriteUserList.setAdapter(arrayAdapter);

        favoriteUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // bundleに受け渡したい値を保存
                Bundle bundle = new Bundle();
                // 画像ID
                bundle.putInt("PHOTO", photos[position]);
                //
                bundle.putString("USER_NAME", favoriteUserNames[position]);
                // Navigation遷移
                Navigation.findNavController(view).navigate(R.id.action_favoriteUserView_to_exhibitProfile, bundle);
            }
        });
        return view;
    }


}
