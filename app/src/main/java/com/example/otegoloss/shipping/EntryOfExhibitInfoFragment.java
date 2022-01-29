//出品情報入力画面
package com.example.otegoloss.shipping;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.otegoloss.MainActivity;
import com.example.otegoloss.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;


public class EntryOfExhibitInfoFragment extends Fragment {
    //入力情報保持用の宣言
    private EditText pro_name;
    private EditText pro_description;
    private EditText weight;
    private EditText price;
    private EditText recipe_url;

    // 画像の取り込み
    private ImageView input_image;
    private Button input_button;

    private String Product_area;
    private String Delivery_method;

    // 画像のURI
    Uri imgUri;

    // 画像ファイルを開く
    private ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            input_image.setImageURI(result);
            imgUri = result;
        }
    });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_entry_of_exhibit_info, container, false);

        //入力情報保持用のインスタンス生成
        pro_name = (EditText) view.findViewById(R.id.product_name_listing_info);
        pro_description= (EditText) view.findViewById(R.id.product_description_listing_info);
        weight = (EditText) view.findViewById(R.id.product_weight_listing_info);
        price = (EditText) view.findViewById(R.id.amount_listing_info);
        recipe_url = (EditText) view.findViewById(R.id.recipe_url_listing_info);
        input_image = (ImageView) view.findViewById(R.id.input_image);
        input_button = (Button) view.findViewById(R.id.input_button);

        //産地のSpinner処理
        Spinner product_area_spinner = (Spinner)view.findViewById(R.id.product_area_listing_info);
        product_area_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> adapter,
                                          View v, int position, long id) {
                   String product_area = (String)adapter.getSelectedItem();
                   Product_area = product_area;
               }

               public void onNothingSelected(AdapterView<?> parent) {

               }
           });

        //配達方法のSpinner処理
        Spinner delivery_method_spinner = (Spinner)view.findViewById(R.id.delivery_method_listing_info);
        delivery_method_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter,
                                       View v, int position, long id) {
                String delivery_method = (String)adapter.getSelectedItem();
                Delivery_method= delivery_method;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 画像ファイルを開く
        input_button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        }));

            // 入力完了ボタンを取得
        Button buttonNext= view.findViewById(R.id.next_button_entry_exhibit_product);
        // 入力完了ボタンをクリックした時の処理
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //入力情報を格納する
                String pro_names = pro_name.getText().toString();
                String pro_descriptions = pro_description.getText().toString();
                String recipe_urls = recipe_url.getText().toString();
                String pro_weights = weight.getText().toString();
                String pro_prices = price.getText().toString();


                //weightとpriceはInt型に変換
                //入力制限のためにここで変換してみる
                //正規表現を用いて条件を書くこともできそうだけど、どちらにせよInt型として取り出しておく必要がある
                //落ちるバグの原因になってるっぽいので一旦隠す
                /*
                Editable edit_weight = weight.getText();
                Editable edit_prices = price.getText();
                int int_weight = Integer.parseInt(edit_weight.toString());
                int int_price = Integer.parseInt(edit_prices.toString());
                */

                if (pro_names.equals("")
                        || pro_descriptions.equals("")
                        || recipe_urls.equals("")
                ) {
                    Toast.makeText(view.getContext(), "入力された情報が正しくありません。もう一度確認してください。", Toast.LENGTH_LONG).show();

                } else if(pro_names.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠0-9a-zA-Z!\"#$%&'()*+-.,\\/:;<=>?@[\\]^_`{|}~]]*$")
                    && pro_descriptions.matches("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠0-9a-zA-Z!\"#$%&'()*+-.,\\/:;<=>?@[\\]^_`{|}~]]*$")
                    && recipe_urls.matches("^[0-9a-zA-Z!\"#$%&'()*+-.,\\/:;<=>?@[\\]^_`{|}~]]*$")
                   /*
                    && (int_weight >= 0 && int_weight <= 30000)
                    && (int_price >= 10 && int_price <= 99999)
                    */
                ) {
                    // 画像ファイル取得
                    Bitmap bitmap = ((BitmapDrawable)input_image.getDrawable()).getBitmap();
                    String strBase64 = encodeImage(bitmap);

//                    try {
//                        Bitmap bmp = getBitmapFromUri(imgUri);
//                        imageView.setImageBitmap(bmp);
//                        new PostBmpAsyncHttpRequest(self).execute(new Param("http://xxxx.xxxx/index.php", bmp));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    //次のフラグメントにBundleを使ってデータを渡す
                    //タイトル
                    Bundle bundle = new Bundle();
                    bundle.putString("PRODUCT_NAME", pro_names);
                    bundle.putString("PRODUCT_DESCRIPTION", pro_descriptions);
                    bundle.putString("PRODUCT_WEIGHT", pro_weights);
                    bundle.putString("PRODUCT_PRICE", pro_prices);
                    bundle.putString("RECIPE_URL", recipe_urls);
                    bundle.putString("PRODUCT_AREA", Product_area);
                    bundle.putString("DELIVERY_METHOD", Delivery_method);
                    bundle.putString("PRODUCT_IMAGE", strBase64);

                    //数字
                    //bundle.putInt("PRODUCT_INT_WEIGHT", int_weight);
                    //bundle.putInt("PRODUCT_INT_PRICE", int_price);


                    // fragmentViewExhibitInfoConfirmationに遷移させる
                    Navigation.findNavController(view).navigate(R.id.action_entry_to_confirmation, bundle);
                } else {
                    Toast.makeText(view.getContext(), "入力された情報が正しくありません。もう一度確認してください。", Toast.LENGTH_LONG).show();
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
