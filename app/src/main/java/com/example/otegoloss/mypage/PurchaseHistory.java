package com.example.otegoloss.mypage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.otegoloss.ConnectionJSON;
import com.example.otegoloss.GridAdapter;
import com.example.otegoloss.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class PurchaseHistory extends Fragment {

    // http通信の開始・終了時刻
    long startTime;
    long endTime;

    // 商品名配列
    private String[] productNames;
    // 価格配列
    private int[] prices;
    // 商品ID
    private String[] productID;
    //生産者ID
    private String[] producerID;

    // ユーザデータが保存されている変数
    private SharedPreferences userIDData;
    String userID;

    private List<Integer> imgList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_purchase_history, container, false);

        // userIDを取得
        userIDData = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        userID = userIDData.getString("userID", "error");

        if (userID == "error") {
            userID = "u0000001";
        }
        System.out.println(userID);

        // http通信
        Thread t = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    // phpファイルまでのリンク
                    URL path = new URL("http://ec2-13-114-108-27.ap-northeast-1.compute.amazonaws.com/PurchaseHistory.php");

                    // クエリ文字列を連想配列に入れる
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("user_id", userID);
                    // クエリ文字列組み立て・URL との連結
                    StringJoiner stringUrl = new StringJoiner("&", path + "?", "");
                    for (Map.Entry<String, String> param: map.entrySet()) {
                        stringUrl.add(param.getKey() + "=" + param.getValue());
                    }
                    URL url = new URL(stringUrl.toString());
                    System.out.println(url);
                    // 処理開始時刻
                    startTime = System.currentTimeMillis();
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    final String str = ConnectionJSON.InputStreamToString(con.getInputStream());

                    // 終了時刻
                    endTime = System.currentTimeMillis();
                    Log.d("HTTP", str);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(String.valueOf(str));
                            System.out.println(endTime - startTime);

                            // Jsonのキーを指定すれば対応する値が入る
                            //配列の取得
                            List<String> productNameList = ConnectionJSON.ChangeArrayJSON(str, "product_name");
                            productNames = productNameList.toArray(new String[productNameList.size()]);
                            List<String> priceList = ConnectionJSON.ChangeArrayJSON(str, "price");
                            String[] priceString = priceList.toArray(new String[priceList.size()]);
                            prices = Stream.of(priceString).mapToInt(Integer::parseInt).toArray();
                            List<String> produceIDList = ConnectionJSON.ChangeArrayJSON(str, "product_id");
                            productID = produceIDList.toArray(new String[produceIDList.size()]);
                            List<String> sellerIDList = ConnectionJSON.ChangeArrayJSON(str, "seller_id");
                            producerID = sellerIDList.toArray(new String[sellerIDList.size()]);

                            System.out.println("array");
                            //progressDialog.dismiss();
                            purchaseSettingUI(view);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }
        });

        // ThreadTestクラスの処理が終了するまで待機の指示
        try {
            t.start();
            System.out.println("start");
            t.join();
            System.out.println("join");
        } catch (InterruptedException e) {
            // 例外処理
            e.printStackTrace();
        }
        return view;
    }

    public void purchaseSettingUI(View view) {

        //商品一覧画面
        // for-each member名をR.drawable.名前としてintに変換してarrayに登録
        for (String productName: productNames){
            int imageId = getResources().getIdentifier("tomato", "drawable", getActivity().getPackageName());
            imgList.add(imageId);
        }

        // GridViewのインスタンスを生成
        GridView gridview = view.findViewById(R.id.purchaseHistory_grid);
        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル grid_items.xml を
        // fragment_home.xml に inflate するためにGridAdapterに引数として渡す
        GridAdapter adapter = new GridAdapter(getActivity().getApplicationContext(),
                R.layout.grid_items,
                imgList,
                productNames,
                prices,
                producerID
        );
        // gridViewにadapterをセット
        gridview.setAdapter(adapter);

        // item clickのListenerをセット
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // bundleに受け渡したい値を保存
                Bundle bundle = new Bundle();
                // 画像ID
                bundle.putInt("IMAGEID", imgList.get(position));
                // 商品ID
                bundle.putString("PRODUCT_ID", productID[position]);
                // Navigation遷移
                Navigation.findNavController(view).navigate(R.id.action_purchaseHistory_to_fragmentProduct, bundle);
            }
        });
    }
}
