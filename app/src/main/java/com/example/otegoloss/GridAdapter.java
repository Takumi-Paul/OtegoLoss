/*
1/6
Grid表示するためのクラス
*/

package com.example.otegoloss;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.content.Context;


public class GridAdapter extends BaseAdapter {

    class ViewHolder {
        ImageView imageView;
        TextView pro_textView;
        TextView pri_textView;
        TextView producerID_textView;
        View view;
    }
    private List<Bitmap> imageList = new ArrayList<>();
    private String[] pro_names;
    private int[] price;
    private LayoutInflater inflater;
    private int layoutId;
    private String[] producerIDs;

    // 引数がHomeFragmentからの設定と合わせる
    public GridAdapter(Context context,
                       int layoutId,
                       List<Bitmap> imgList,
                       String[] productNames,
                       int[] prices,
                       String[] producerID) {

        // List<Integer> imgList,

        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        imageList = imgList;
        pro_names = productNames;
        price = prices;
        producerIDs = producerID;
        System.out.println(pro_names);
    }

    @Override
    public int getCount() {
        // List<String> imgList の全要素数を返す
        return pro_names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            // main.xml の <GridView .../> に grid_items.xml を inflate して convertView とする
            convertView = inflater.inflate(layoutId, parent, false);
            // ViewHolder を生成
            holder = new ViewHolder();

            holder.imageView = convertView.findViewById(R.id.image_view);
            holder.pro_textView = convertView.findViewById(R.id.productHome_textView);
            holder.pri_textView = convertView.findViewById(R.id.priceHome_textView);
            holder.producerID_textView=convertView.findViewById(R.id.producer_id_textView);
            holder.view = convertView.findViewById(R.id.product_view);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageBitmap(imageList.get(position));
        holder.pro_textView.setText(pro_names[position]);
        String pro_price = String.valueOf(price[position]);
        holder.pri_textView.setText(pro_price+"円");
        holder.producerID_textView.setText(producerIDs[position]);

        return convertView;
    }
}
