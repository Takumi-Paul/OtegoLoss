package com.example.otegoloss.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

public class ProducerAgreementFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_producer_agreement, container, false);

        Button nextProRegisterButton = view.findViewById(R.id.nextProRegister_button);
        RadioButton proAgreeRuleRadioButton = (RadioButton)view.findViewById(R.id.proAgreeRule_radioButton);
        RadioButton proRejectRuleRadioButton = (RadioButton)view.findViewById(R.id.proRejectRule_radioButton);

        String errormessage = "同意しないのであれば、生産者登録できません";

        nextProRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proAgreeRuleRadioButton.isChecked() == true) {
                    Navigation.findNavController(view).navigate(R.id.action_producer_agreement_to_producer_info);
                }
                if (proRejectRuleRadioButton.isChecked() == true) {
                    Toast.makeText(view.getContext(), errormessage, Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

}
