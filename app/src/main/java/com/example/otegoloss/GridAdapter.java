/*
1/6
Grid表示するためのクラス
*/

package com.example.otegoloss;

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
        TextView textView;
    }
    private List<Integer> imageList = new ArrayList<>();
    private String[] names;
    private LayoutInflater inflater;
    private int layoutId;

    // 引数がMainActivityからの設定と合わせる
    public GridAdapter(Context context,
                       int layoutId,
                       List<Integer> imgList,
                       String[] productNames) {

        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        imageList = imgList;
        names = productNames;
    }

    @Override
    public int getCount() {
        // List<String> imgList の全要素数を返す
        return imageList.size();
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
            holder.textView = convertView.findViewById(R.id.text_view);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(imageList.get(position));
        holder.textView.setText(names[position]);

        return convertView;
    }
}
