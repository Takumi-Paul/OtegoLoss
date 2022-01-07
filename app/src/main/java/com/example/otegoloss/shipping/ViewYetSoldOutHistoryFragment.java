//出品履歴（未完売）
package com.example.otegoloss.shipping;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.otegoloss.R;



public class ViewYetSoldOutHistoryFragment extends Fragment {
    //画面表示
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_view_yet_sold_out_history, container, false);
    }


}
