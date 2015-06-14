package com.example.matthewfarley.rsstest.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.matthewfarley.rsstest.Data.RssContract.ArticleEntry;


import java.security.PrivateKey;

/**
 * Created by matthewfarley on 31/05/15.
 */
public class RssDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rss.db";

    public static final String TAG = RssDBHelper.class.getSimpleName();

    public RssDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ARTICLE_TABLE = "CREATE TABLE " + ArticleEntry.TABLE_NAME + " (" +
                ArticleEntry.ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ArticleEntry.TITLE + " TEXT NOT NULL, " +
                ArticleEntry.DESCRIPTION + " TEXT NOT NULL, " +
                ArticleEntry.PUBLISH_DATE + " TEXT NOT NULL, " +
//                ArticleEntry.AUTHOR + " TEXT NOT NULL, " +
                ArticleEntry.ARTICLE_URL + " TEXT NOT NULL, " +
//                ArticleEntry.ARTICLE_CONTENT + " TEXT NOT NULL, " +
                ArticleEntry.ARTICLE_IS_READ + " INTEGER DEFAULT 0, " +
                ArticleEntry.THUMBNAIL_URL + " TEXT NOT NULL);";
        Log.i(TAG, SQL_CREATE_ARTICLE_TABLE);
        db.execSQL(SQL_CREATE_ARTICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // DB is only caching online data so drop it if it exists
        db.execSQL("DROP TABLE IF EXISTS " + ArticleEntry.TABLE_NAME);
        onCreate(db);
    }
}
