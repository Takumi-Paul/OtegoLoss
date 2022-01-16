package com.example.otegoloss.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

public class AccountDeletionAgreementFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_deletion_agreement, container, false);

        Button nextDeleteButton = view.findViewById(R.id.nextDelete_button);
        RadioButton agreeDeleteRuleRadioButton = (RadioButton)view.findViewById(R.id.agreeDeleteRule_radioButton);
        RadioButton rejectDeleteRuleRadioButton = (RadioButton)view.findViewById(R.id.rejectDeleteRule_radioButton);

        String errormessage = "同意しないのであれば、アカウント削除できません";

        nextDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (agreeDeleteRuleRadioButton.isChecked() == true) {
                    Navigation.findNavController(view).navigate(R.id.action_deletion_agreement_to_accountdelete);
                }
                if (rejectDeleteRuleRadioButton.isChecked() == true) {
                    Toast.makeText(view.getContext(), errormessage, Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

}
