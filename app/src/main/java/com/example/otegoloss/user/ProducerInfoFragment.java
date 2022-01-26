package com.example.otegoloss.user;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

import java.io.ByteArrayOutputStream;

public class ProducerInfoFragment extends Fragment {

    // 画像の取り込み
    private ImageView producer_image;
    private Button input_producer_button;

    // 画像ファイルを開く
    private ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            producer_image.setImageURI(result);
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_view_producer_information, container, false);

        EditText phoneNumberEditText = (EditText)view.findViewById(R.id.phoneNumber_editText);
        EditText banknameEditText = (EditText)view.findViewById(R.id.bankname_editText);
        EditText bankbranchnameEditText = (EditText)view.findViewById(R.id.bankbranchname_editText);
        EditText accountNameEditText = (EditText)view.findViewById(R.id.accountName_editText);

        producer_image = (ImageView) view.findViewById(R.id.input_image);
        input_producer_button = (Button) view.findViewById(R.id.identification_button);

        // 画像ファイルを開く
        input_producer_button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        }));

        Button proRegisterButton = view.findViewById(R.id.proRegister_button);
        proRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumberEditTexts = phoneNumberEditText.getText().toString();
                String banknameEditTexts = banknameEditText.getText().toString();
                String bankbranchnameEditTexts = bankbranchnameEditText.getText().toString();
                String accountNameEditTexts = accountNameEditText.getText().toString();

                if(phoneNumberEditTexts.equals("") || banknameEditTexts.equals("") || bankbranchnameEditTexts.equals("") || accountNameEditTexts.equals("")){
                    Toast.makeText(view.getContext(), "入力欄に不備があります", Toast.LENGTH_LONG).show();
                } else if (banknameEditTexts.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠]*$") && phoneNumberEditTexts.matches("^[0-9]*$") && bankbranchnameEditTexts.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠]*$") && accountNameEditTexts.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠]*$")) {
                    // 画像ファイル取得
                    Bitmap bitmap = ((BitmapDrawable)producer_image.getDrawable()).getBitmap();
                    String strBase64 = encodeImage(bitmap);

                    Navigation.findNavController(view).navigate(R.id.action_producer_info_to_producer_info_regist_comp);
                } else {
                    Toast.makeText(view.getContext(), "入力欄に不備があります", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

}
