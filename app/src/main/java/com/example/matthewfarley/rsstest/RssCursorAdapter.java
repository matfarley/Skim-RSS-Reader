package com.example.matthewfarley.rsstest;

import com.example.matthewfarley.rsstest.Data.RssContract.ArticleEntry;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by matthewfarley on 14/06/15.
 */
public class RssCursorAdapter extends CursorAdapter {

    public RssCursorAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }

    public static class ViewHolder {
        public final ImageView thumbnailView;
        public final TextView titleTextView;
        public final View readItemOverlay;

        public ViewHolder(View view){
            thumbnailView = (ImageView) view.findViewById(R.id.list_item_thumbnail);
            titleTextView = (TextView) view.findViewById(R.id.list_item_title);
            readItemOverlay = view.findViewById(R.id.read_list_item_overlay);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_rss, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        // Get the thing from the thing.
        String title = cursor.getString(ArticleEntry.COL_ARTICLE_TITLE);
        viewHolder.titleTextView.setText(title);

        String thumbnailUrl = cursor.getString(ArticleEntry.COL_ARTICLE_THUMBNAIL_URL);
        // Use picasso to asyncronosly get and cache image.
        Picasso.with(context)
                .load(thumbnailUrl)
                .noFade()
                .into(viewHolder.thumbnailView);

        boolean isRead = (cursor.getInt(ArticleEntry.COL_ARTICLE_IS_READ) != 0);
        if(isRead){
            viewHolder.readItemOverlay.setVisibility(View.VISIBLE);
        }else {
            viewHolder.readItemOverlay.setVisibility(View.GONE);
        }
    }
}
