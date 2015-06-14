package com.example.matthewfarley.rsstest.Service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.example.matthewfarley.rsstest.Data.RssContract;
import com.example.matthewfarley.rsstest.Models.Article;

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
            ContentValues articleValues = new ContentValues();
            articleValues.put(RssContract.ArticleEntry.TITLE, article.getTitle());
            articleValues.put(RssContract.ArticleEntry.DESCRIPTION, article.getDescription());
            articleValues.put(RssContract.ArticleEntry.PUBLISH_DATE, article.getPubDate());
//            articleValues.put(ArticleEntry.AUTHOR, article.getAuthor());
            articleValues.put(RssContract.ArticleEntry.ARTICLE_URL, article.getUrl());
//            articleValues.put(ArticleEntry.ARTICLE_CONTENT, article.getEncodedContent());
            articleValues.put(RssContract.ArticleEntry.ARTICLE_IS_READ, 0); // New so is false
            articleValues.put(RssContract.ArticleEntry.THUMBNAIL_URL, article.getThumbnailURL());
            cVVector.add(articleValues);
        }

        if (cVVector.size() > 0){
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            this.getContentResolver().bulkInsert(RssContract.ArticleEntry.CONTENT_URI, cvArray);
        }

    }
}
