package com.example.matthewfarley.rsstest.Service;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import com.example.matthewfarley.rsstest.Data.RssContract;
import com.example.matthewfarley.rsstest.Data.RssDBHelper;
import com.example.matthewfarley.rsstest.Models.Article;

import com.example.matthewfarley.rsstest.Data.RssContract.ArticleEntry;
import com.example.matthewfarley.rsstest.Util;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by matthewfarley on 31/05/15.
 */
public class RssService extends IntentService {

    private static final String WORKER_THREAD_NAME = "com.example.matthewfarley.rsstest.Service.RssService.WorkerThread";
    public static final String RSS_FEED_URL_KEY = "com.example.matthewfarley.rsstest.RSS_FEED_URL_KEY";
    public static final String TAG = RssService.class.getSimpleName();

    public RssService() {
        super(WORKER_THREAD_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String feedUrl = intent.getStringExtra(RSS_FEED_URL_KEY);

        URL url = null;
        ArrayList<Article> articles = new ArrayList<Article>();
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            XMLReader reader = parser.getXMLReader();

            url = new URL(feedUrl);
            RssSaxHandler rssSaxHandler = new RssSaxHandler();

            reader.setContentHandler(rssSaxHandler);
            reader.parse(new InputSource(url.openStream()));

            articles = rssSaxHandler.getArticleList();
            Log.i(TAG, "PARSING FINISHED");

        } catch (IOException e) {
            Log.e(TAG, e.getMessage() + " >> " + e.toString());
        } catch (SAXException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            Log.e(TAG,  e.toString());
        }

        populateDatabase(articles);
    }

    private void populateDatabase(final ArrayList<Article> articles){
        // Get and insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(articles.size());
        for (Article article : articles){
            // Check if article is in db.
            String guidBase64 = Util.base64StringFromString(article.getGuid());
            if(!dbHasArticle(ArticleEntry.ROW_ID, guidBase64)){
                ContentValues articleValues = new ContentValues();
                //Use guid as primary key because it is unique.  Make base64 so it will go in db.
                articleValues.put(ArticleEntry.ROW_ID, guidBase64);
                articleValues.put(ArticleEntry.TITLE, article.getTitle());
                articleValues.put(ArticleEntry.DESCRIPTION, article.getDescription());
                articleValues.put(ArticleEntry.PUBLISH_DATE, article.getPubDate());
                articleValues.put(ArticleEntry.ARTICLE_URL, article.getUrl());
                articleValues.put(ArticleEntry.ARTICLE_IS_READ, 0); // New, so is false
                articleValues.put(ArticleEntry.THUMBNAIL_URL, article.getThumbnailURL());
                cVVector.add(articleValues);
            }
        }

        if (cVVector.size() > 0){
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            this.getContentResolver().bulkInsert(ArticleEntry.CONTENT_URI, cvArray);
        }
    }


    public boolean dbHasArticle(String dbfield, String fieldValue) {
        Cursor cursor = this.getContentResolver().query(
                ArticleEntry.CONTENT_URI,
                null,
                dbfield + " = ?",
                new String[]{fieldValue},
                null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
