package com.example.matthewfarley.rsstest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matthewfarley.rsstest.Models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by matthewfarley on 3/06/15.
 */
public class RssArrayAdapter extends ArrayAdapter<Article> {

    public RssArrayAdapter(Activity activity, List<Article> articles) {
        super(activity, 0, articles);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        Article article = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_rss, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if (article != null){
            // Use picasso to asyncronosly get and cache image.
            Picasso.with(getContext())
                    .load(article.getThumbnailURL())
                    .noFade()
                    .into(viewHolder.thumbnailView);

            viewHolder.titleTextView.setText(article.getTitle());
            if(article.isRead()){
                viewHolder.readItemOverlay.setVisibility(View.VISIBLE);
            }else {
                viewHolder.readItemOverlay.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}

