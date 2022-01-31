/*
出品者画面用のGridAdapter
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

public class GridAdapterOfShipping extends BaseAdapter {
    class ViewHolder {
        ImageView imageView;
        TextView pro_textView;
        TextView pri_textView;
        TextView dat_textView;
        View view;
    }
    private List<Bitmap> imageList = new ArrayList<>();
    private String[] pro_names;
    private int[] price;
    private String[] listingDate;
    private LayoutInflater inflater;
    private int layoutId;

    // 引数を出品者画面と合わせる
    public GridAdapterOfShipping(Context context,
                                 int layoutId,
                                 List<Bitmap> imgList,
                                 String[] productNames,
                                 int[] prices,
                                 String[] Date) {

        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        imageList = imgList;
        pro_names = productNames;
        price = prices;
        listingDate = Date;
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

        GridAdapterOfShipping.ViewHolder holder;
        if (convertView == null) {
            // fragment_shipping.xml の <GridView .../> に grid_items_of_shipping.xml を inflate して convertView とする
            convertView = inflater.inflate(layoutId, parent, false);
            // ViewHolder を生成
            holder = new GridAdapterOfShipping.ViewHolder();

            holder.imageView = convertView.findViewById(R.id.shipping_imageView);
            holder.pro_textView = convertView.findViewById(R.id.shipping_product_name);
            holder.pri_textView = convertView.findViewById(R.id.shipping_price);
            holder.dat_textView = convertView.findViewById(R.id.shipping_listing_date);
            holder.view = convertView.findViewById(R.id.shipping_view);

            convertView.setTag(holder);
        }
        else {
            holder = (GridAdapterOfShipping.ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageBitmap(imageList.get(position));
        holder.pro_textView.setText(pro_names[position]);
        String pro_price = String.valueOf(price[position]);
        holder.pri_textView.setText(pro_price+"円");
        holder.dat_textView.setText(listingDate[position]);

        return convertView;
    }
}
