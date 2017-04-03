package com.kmema.android.movienow;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.GuestViewHolder> {

    private Context mContext;
    private String[] data;
    private String[] videoName;
    private String name;

    GuestListAdapter(Context context, Cursor cursor, String[] data, String[] Name) {
        this.mContext = context;
        this.data = data;
        this.videoName = Name;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.video_list_item, parent, false);
        return new GuestViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
// Move the mCursor to the position of the item to be displayed
        if (data == null) {
            return;
        }
        name = videoName[position];
        //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
        holder.titleTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class GuestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView;
        TextView partySizeTextView;
        Context context;

        GuestViewHolder(View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;
            titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            partySizeTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Context context = this.context;
            watchYoutubeVideo(name, position);
          //  Toast.makeText(context, " HI it works", Toast.LENGTH_SHORT).show();
        }
    }

    private void watchYoutubeVideo(String id, int position) {
        String address = data[position];
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + address));
            this.mContext.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + address));
            this.mContext.startActivity(intent);
        }
    }
}