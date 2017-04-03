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


class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {

    private Context mContext;
    private String[] reviewAuthor;
    private String[] reviewContent;
    private String[] reviewURL;
    private String author;
    String content;

    ReviewListAdapter(Context context, Cursor cursor, String[] reviewAuthor, String[] reviewContent, String[] reviewURL) {
        this.mContext = context;
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
        this.reviewURL = reviewURL;

    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
// Move the mCursor to the position of the item to be displayed

        if (reviewAuthor == null) {
            return;
        }
        author = reviewAuthor[position];
        content = reviewContent[position];
   //     Toast.makeText(mContext, author, Toast.LENGTH_SHORT).show();

        holder.nameTextView.setText(author);
//            holder.contentTextView.setText(content);
    }


    @Override
    public int getItemCount() {
        return reviewAuthor.length;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Will display the guest name
        TextView nameTextView;

        // Will display the party size number
        TextView partySizeTextView;

        Context context;

        ReviewViewHolder(View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;
            nameTextView = (TextView) itemView.findViewById(R.id.text_view_author);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Context context = this.context;
            watchYoutubeVideo(reviewURL[position], position);
           // Toast.makeText(context, " HI it works", Toast.LENGTH_SHORT).show();
        }
    }

    private void watchYoutubeVideo(String id, int position) {

        String address = reviewURL[position];
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
            this.mContext.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(/*"http://www.youtube.com/watch?v="+*/address));
            this.mContext.startActivity(intent);
        }
    }
}
