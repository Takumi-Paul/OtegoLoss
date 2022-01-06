package com.example.otegoloss.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.otegoloss.R;
import com.example.otegoloss.databinding.FragmentUserBinding;

public class ProfileConfigFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_view_profile_config, container, false);
    }

}
