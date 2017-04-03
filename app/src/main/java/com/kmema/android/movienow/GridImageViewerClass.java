package com.kmema.android.movienow;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
    class GridImageViewerClass extends BaseAdapter {
        private Context context;
        private String[] numIcons;
        private int mCountCursor;
        GridImageViewerClass(Context context, String numIcons[])
        {
            super();
            this.context=context;
            this.numIcons=numIcons;
        }
        @Override
        public int getCount() {
                return numIcons.length;
         }
        @Override
        public Object getItem(int position)
        {
            return numIcons[position];
        }
        @Override
        public long getItemId(int position)
        {
        return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView == null)
            {
                imageView = new ImageView(context);
                convertView = imageView;
            }
            else
            {
                imageView=(ImageView)convertView;
            }
            Picasso.with(context).load(numIcons[position]).into(imageView);
            return convertView;
        }
    }