package com.example.otegoloss;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    private LayoutInflater inflater;
    private int itemLayoutId;
    private String[] names;
    private List<Bitmap> imgList;

    public ListViewAdapter(Context context, int itemLayoutId, String[] names, List<Bitmap> imgList) {
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        this.names = names;
        this.imgList = imgList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        com.example.otegoloss.ListViewAdapter.ViewHolder holder;
        // 最初だけ View を inflate して、それを再利用する
        if (convertView == null) {
            // activity_main.xml に list.xml を inflate して convertView とする
            convertView = inflater.inflate(itemLayoutId, parent, false);
            // ViewHolder を生成
            holder = new com.example.otegoloss.ListViewAdapter.ViewHolder();
            holder.textView = convertView.findViewById(R.id.textView);
            holder.imageView = convertView.findViewById(R.id.userImageView);
            convertView.setTag(holder);
        }
        // holder を使って再利用
        else {
            holder = (com.example.otegoloss.ListViewAdapter.ViewHolder) convertView.getTag();
        }

        // holder の imageView にセット
        holder.imageView.setImageBitmap(imgList.get(position));
        // 現在の position にあるファイル名リストを holder の textView にセット
        holder.textView.setText(names[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        // texts 配列の要素数
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
