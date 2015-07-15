package com.example.matthewfarley.rsstest.Data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by matthewfarley on 31/05/15.
 */
public class RssContract {

    public static final String CONTENT_AUTHORITY = "com.example.matthewfarley.rsstest";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ARTICLES = "articles";

    // Inner class that defines the table contents of the article table
    public static final class ArticleEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI
                        .buildUpon()
                        .appendPath(PATH_ARTICLES)
                        .build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLES;

        public static final String TABLE_NAME = "articles";
        public static final String ROW_ID = "_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String PUBLISH_DATE = "pub_date";
        public static final String ARTICLE_URL = "article_url";
        public static final String ARTICLE_IS_READ = "article_is_read";
        public static final String THUMBNAIL_URL = "thumbnail_url";

        public static final int COL_ARTICLE_ID = 0;
        public static final int COL_ARTICLE_TITLE = 1;
        public static final int COL_ARTICLE_DESCRIPTION = 2;
        public static final int COL_ARTICLE_PUBLISH_DATE = 3;
        public static final int COL_ARTICLE_URL = 4;
        public static final int COL_ARTICLE_IS_READ = 5;
        public static final int COL_ARTICLE_THUMBNAIL_URL = 6;

        public static Uri buildArticleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
