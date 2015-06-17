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

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by matthewfarley on 14/06/15.
 */
public class RssCursorAdapter extends CursorAdapter {

    public static final String TAG_VIEW_HOLDER = "viewHolder";
    public static final String TAG_ARTICLE_ID = "articleID";

    private static final float AlPHA_ARTICLE_READ = 0.5f;
    private static final float AlPHA_ARTICLE_NEW = 1.f;


    public RssCursorAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }

    public static class ViewHolder {
        public final ImageView thumbnailView;
        public final TextView titleTextView;
        public ViewHolder(View view){
            thumbnailView = (ImageView) view.findViewById(R.id.list_item_thumbnail);
            titleTextView = (TextView) view.findViewById(R.id.list_item_title);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_rss, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        HashMap<String, Object> tags = new HashMap<>();
        tags.put(TAG_VIEW_HOLDER, viewHolder);
        view.setTag(tags);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        HashMap<String, Object>tags = (HashMap<String, Object>)view.getTag();
        ViewHolder viewHolder = (ViewHolder)tags.get(TAG_VIEW_HOLDER);
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
            viewHolder.thumbnailView.setAlpha(AlPHA_ARTICLE_READ);
            viewHolder.titleTextView.setTextColor(context.getResources().getColor(R.color.item_list_grey));
        }else {
            viewHolder.thumbnailView.setAlpha(AlPHA_ARTICLE_NEW);
            viewHolder.titleTextView.setTextColor(context.getResources().getColor(R.color.item_list_black));
        }

        tags.put(TAG_ARTICLE_ID, cursor.getString(ArticleEntry.COL_ARTICLE_ID));
        view.setTag(tags);
    }
}
