package com.example.otegoloss;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ReviewListViewAdapter extends BaseAdapter{
        static class ViewHolder {
            TextView nameTextView;
            ImageView assessmentImage;
            TextView commentTextView;
        }

        private LayoutInflater inflater;
        private int itemLayoutId;
        private String[] names;
        private int[] assessment;
        private String[] comment;

        public ReviewListViewAdapter(Context context, int itemLayoutId, String[] names, int[] assessment, String[] comment) {
            super();
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.itemLayoutId = itemLayoutId;
            this.names = names;
            this.assessment = assessment;
            this.comment = comment;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            com.example.otegoloss.ReviewListViewAdapter.ViewHolder holder;
            // 最初だけ View を inflate して、それを再利用する
            if (convertView == null) {
                // activity_main.xml に list.xml を inflate して convertView とする
                convertView = inflater.inflate(itemLayoutId, parent, false);
                // ViewHolder を生成
                holder = new com.example.otegoloss.ReviewListViewAdapter.ViewHolder();
                holder.nameTextView = convertView.findViewById(R.id.nameTextView);
                holder.assessmentImage = convertView.findViewById(R.id.assessmentImage);
                holder.commentTextView = convertView.findViewById(R.id.commentTextView);
                convertView.setTag(holder);
            }
            // holder を使って再利用
            else {
                holder = (com.example.otegoloss.ReviewListViewAdapter.ViewHolder) convertView.getTag();
            }

            // holder の imageView にセット
            //holder.assessmentImage.setImageResource(R.drawable.star);
            // 現在の position にあるファイル名リストを holder の textView にセット
            holder.commentTextView.setText(comment[position]);
            holder.nameTextView.setText(names[position]);

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
