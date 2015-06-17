package com.example.matthewfarley.rsstest.Service;

/**
 * Created by matthewfarley on 31/05/15.
 */

import android.util.Log;

import com.example.matthewfarley.rsstest.Models.Article;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class RssSaxHandler extends DefaultHandler{
    static  final String TAG = RssSaxHandler.class.getSimpleName();

    public static final String XML_ELEMENT_TITLE = "title";
    public static final String XML_ELEMENT_DESCRIPTION = "description";
    public static final String XML_ELEMENT_PUBLISHED = "pubDate";
    public static final String XML_ELEMENT_ID = "id";
    public static final String XML_ELEMENT_AUTHOR = "creator";
    public static final String XML_ELEMENT_CONTENT = "encoded";
    public static final String XML_ELEMENT_ITEM = "item";
    public static final String XML_ELEMENT_URL = "link";
    public static final String XML_ELEMENT_GUID = "guid";
    public static final String XML_ELEMENT_THUMBNAIL = "media:content";

    // Feed and Article objects to use for temporary storage
    private Article currentArticle = new Article();
    private ArrayList<Article> articleList = new ArrayList<Article>();

    // Number of articles added so far
    private int articlesAdded = 0;

    // Number of articles to download
    private static final int ARTICLES_LIMIT = 50;

    //Current characters being accumulated
    StringBuffer chars = new StringBuffer();


    public ArrayList<Article> getArticleList() {
        return articleList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) {
        chars = new StringBuffer();
        if (qName.equals("media:content") && currentArticle != null){
            currentArticle.setThumbnailURL(atts.getValue("url"));
        }
        // log
        if ("".equals (uri))
            Log.i(TAG, "Start element: " + qName);
        else
            Log.i(TAG, "Start element: {" + uri + "}" + localName);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if ("".equals (uri))
            Log.i(TAG, "End element: " + qName);
        else
            Log.i(TAG,"End element:   {" + uri + "}" + localName);

        if (localName.equalsIgnoreCase(XML_ELEMENT_TITLE)){
            currentArticle.setTitle(chars.toString());
        } else if (localName.equalsIgnoreCase(XML_ELEMENT_GUID)){
            currentArticle.setGuid(chars.toString());
        } else if (localName.equalsIgnoreCase(XML_ELEMENT_DESCRIPTION)){
            currentArticle.setDescription(chars.toString());
        } else if (localName.equalsIgnoreCase(XML_ELEMENT_PUBLISHED)){
            currentArticle.setPubDate(chars.toString());
        } else if (localName.equalsIgnoreCase(XML_ELEMENT_ID)){
            currentArticle.setGuid(chars.toString());
        } else if (localName.equalsIgnoreCase(XML_ELEMENT_AUTHOR)){
            currentArticle.setAuthor(chars.toString());
        }else if (localName.equalsIgnoreCase(XML_ELEMENT_URL)){
            try {
                currentArticle.setUrl(chars.toString());
            }catch (MalformedURLException mfUrlEx){
                Log.e(TAG, mfUrlEx.toString());
            }
        } else if (localName.equalsIgnoreCase(XML_ELEMENT_CONTENT)){
            currentArticle.setEncodedContent(chars.toString());
        }

        // Check if looking for article, and if article is complete
        if (localName.equalsIgnoreCase(XML_ELEMENT_ITEM)) {

            articleList.add(currentArticle);

            currentArticle = new Article();

            // Lets check if we've hit our limit on number of articles
            articlesAdded++;
            if (articlesAdded >= ARTICLES_LIMIT)
            {
                throw new SAXException();
            }
        }
    }

    /*
 * This method is called when characters are found in between XML markers, however, there is no
 * guarante that this will be called at the end of the node, or that it will be called only once
 * , so we just accumulate these and then deal with them in endElement() to be sure we have all the
 * text
 *
 * (non-Javadoc)
 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
 */
    @Override
    public void characters(char ch[], int start, int length) {
        chars.append(new String(ch, start, length));
    }




}
