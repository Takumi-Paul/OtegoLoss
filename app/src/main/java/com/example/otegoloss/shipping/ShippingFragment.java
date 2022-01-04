/*
12/29
Kobayashi
出品者画面を生成するプログラム
 */

package com.example.otegoloss.shipping;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.otegoloss.databinding.FragmentShippingBinding;

public class ShippingFragment extends Fragment {


    private ShippingViewModel shippingViewModel;
    private FragmentShippingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shippingViewModel =
                new ViewModelProvider(this).get(ShippingViewModel.class);

        binding = FragmentShippingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textShipping;
        shippingViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}