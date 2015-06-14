package com.example.matthewfarley.rsstest.Models;

/**
 * Created by matthewfarley on 31/05/15.
 */
public class RssItem {
    private final String title;
    private final String link;
    // add icon

    public RssItem(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
